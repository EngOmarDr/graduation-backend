package com.graduationProject._thYear.Purchase.services;


import com.graduationProject._thYear.ProductStock.models.ProductStock;
import com.graduationProject._thYear.ProductStock.services.ProductStockService;
import com.graduationProject._thYear.Purchase.dtos.requests.CreatePurchaseHeaderRequest;
import com.graduationProject._thYear.Purchase.dtos.requests.UpdatePurchaseHeaderRequest;
import com.graduationProject._thYear.Purchase.dtos.responses.PurchaseHeaderResponse;
import com.graduationProject._thYear.Purchase.dtos.responses.PurchaseItemResponse;
import com.graduationProject._thYear.Purchase.models.PurchaseHeader;
import com.graduationProject._thYear.Purchase.models.PurchaseItem;
import com.graduationProject._thYear.Purchase.models.StatusType;
import com.graduationProject._thYear.Purchase.repositories.PurchaseHeaderRepository;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.Purchase.repositories.PurchaseItemRepository;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseHeaderRepository purchaseHeaderRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UnitItemRepository unitItemRepository;
    private final ProductStockService stockService;
    @Transactional
    public PurchaseHeaderResponse create(CreatePurchaseHeaderRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Purchase must contain at least one item");
        }

        LocalDateTime supplyDate = request.getSupplyDate() != null
                ? request.getSupplyDate()
                : LocalDateTime.now();

        PurchaseHeader header = PurchaseHeader.builder()
                .WarehouseId(warehouse)
                .supplyDate(supplyDate)
                .requestDate(null)
                .buyDate(null)
                .receiveDate(null)
                .notes(request.getNotes())
                .status(StatusType.supply) //  always start with "supply"
                .build();

        List<PurchaseItem> items = request.getItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());

            BigDecimal unitFact = itemReq.getUnitFact() != null
                    ? itemReq.getUnitFact()
                    : BigDecimal.valueOf(unitItem.getFact());

            return PurchaseItem.builder()
                    .purchaseHeader(header)
                    .productId(product)
                    .qty(itemReq.getQty())
                    .unitItemId(unitItem)
                    .unitFact(unitFact)
                    .build();
        }).collect(Collectors.toList());

        header.setItems(items);

        PurchaseHeader saved = purchaseHeaderRepository.save(header);

        return toResponse(saved);
    }

    @Transactional
    public PurchaseHeaderResponse update(Integer id, UpdatePurchaseHeaderRequest req) {
        PurchaseHeader header = purchaseHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));

        if (req.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(req.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
            header.setWarehouseId(warehouse);
        }


        if (req.getNotes() != null) {
            header.setNotes(req.getNotes());
        }

        // Handle status + system-generated dates
        if (req.getStatus() != null) {
            StatusType newStatus = StatusType.fromCode(req.getStatus());

            // Always set the status (in case it's null initially)
            header.setStatus(newStatus);

            switch (newStatus) {
                case request -> {
                        header.setRequestDate(LocalDateTime.now());

                }
                case buy -> {
                        header.setBuyDate(LocalDateTime.now());
                }
                case receive -> {
                        header.setReceiveDate(LocalDateTime.now());
                }
                case supply -> {
                    header.setSupplyDate(LocalDateTime.now());
                }
            }
        }


        if (req.getItems() != null && !req.getItems().isEmpty()) {

            // If current status is "receive" → rollback old stock first
            if (header.getStatus() == StatusType.receive) {
                for (PurchaseItem oldItem : header.getItems()) {
                    stockService.decreaseStock(
                            oldItem.getProductId().getId(),
                            header.getWarehouseId().getId(),
                            oldItem.getUnitItemId().getId(),
                            oldItem.getQty()
                    );
                }
            }


            header.getItems().clear();

            List<PurchaseItem> newItems = req.getItems().stream().map(itemReq -> {
                Product product = productRepository.findById(itemReq.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());

                BigDecimal unitFact = itemReq.getUnitFact() != null
                        ? itemReq.getUnitFact()
                        : BigDecimal.valueOf(unitItem.getFact());

                // If status = receive → increase stock with new quantities
                if (header.getStatus() == StatusType.receive) {
                    Optional<ProductStock> stock = stockService.findStock(
                            product.getId(),
                            header.getWarehouseId().getId(),
                            unitItem.getId()
                    );
                    if (stock.isEmpty()) {
                        stockService.createStock(product.getId(), header.getWarehouseId().getId(), unitItem.getId(), BigDecimal.ZERO);
                    }
                    stockService.increaseStock(product.getId(), header.getWarehouseId().getId(), unitItem.getId(), itemReq.getQty());
                }

                return PurchaseItem.builder()
                        .purchaseHeader(header)
                        .productId(product)
                        .qty(itemReq.getQty())
                        .unitItemId(unitItem)
                        .unitFact(unitFact)
                        .build();
            }).toList();

            header.getItems().addAll(newItems);
        }

        PurchaseHeader updated = purchaseHeaderRepository.save(header);

        return toResponse(updated);
    }

    public List<PurchaseHeaderResponse> getAll() {
        return purchaseHeaderRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PurchaseHeaderResponse getById(Integer id) {
        return purchaseHeaderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));
    }


    @Transactional
    public void delete(Integer id) {
        PurchaseHeader header = purchaseHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));

        if (header.getStatus() == StatusType.receive) {
            //  Reverse stock before deletion
            for (PurchaseItem item : header.getItems()) {
                Integer productId = item.getProductId().getId();
                Integer unitItemId = item.getUnitItemId().getId();
                BigDecimal qty = item.getQty();

                stockService.decreaseStock(productId, header.getWarehouseId().getId(), unitItemId, qty);
            }
        }


        purchaseHeaderRepository.delete(header);
    }



    private UnitItem resolveUnitItem(Product product, Integer unitItemId) {
        if (unitItemId != null) {
            return unitItemRepository.findById(unitItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("UnitItem not found"));
        } else {
            return product.getDefaultUnit().getUnitItems().stream()
                    .filter(UnitItem::getIsDef)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Default UnitItem not found"));
        }
    }
    private PurchaseHeaderResponse toResponse(PurchaseHeader header) {
        return PurchaseHeaderResponse.builder()
                .id(header.getId())
                .warehouseId(header.getWarehouseId().getId())
                .warehouseName(header.getWarehouseId().getName())
                .supplyDate(header.getSupplyDate())
                .requestDate(header.getRequestDate())
                .buyDate(header.getBuyDate())
                .receiveDate(header.getReceiveDate())
                .notes(header.getNotes())
                .items(header.getItems().stream().map(this::mapItem).toList())
                .build();
    }

    private PurchaseItemResponse mapItem(PurchaseItem item) {
        return PurchaseItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId().getId())
                .productName(item.getProductId().getName())
                .qty(item.getQty())
                .unitItemId(item.getUnitItemId() != null ? item.getUnitItemId().getId() : null)
                .unitFact(item.getUnitFact())
                .build();
    }
}
