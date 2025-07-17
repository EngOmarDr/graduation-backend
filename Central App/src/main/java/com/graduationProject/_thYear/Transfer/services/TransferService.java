package com.graduationProject._thYear.Transfer.services;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.ProductStock.models.ProductStock;
import com.graduationProject._thYear.ProductStock.services.ProductStockService;
import com.graduationProject._thYear.Transfer.dtos.requests.CreateTransferRequest;
import com.graduationProject._thYear.Transfer.dtos.requests.CreateTransferItemRequest;
import com.graduationProject._thYear.Transfer.dtos.requests.UpdateTransferRequest;
import com.graduationProject._thYear.Transfer.dtos.responses.TransferItemResponse;
import com.graduationProject._thYear.Transfer.dtos.responses.TransferResponse;
import com.graduationProject._thYear.Transfer.models.Transfer;
import com.graduationProject._thYear.Transfer.models.TransferItem;
import com.graduationProject._thYear.Transfer.repositories.TransferItemRepository;
import com.graduationProject._thYear.Transfer.repositories.TransferRepository;
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
public class TransferService {

    private final TransferRepository transferRepository;
    private final TransferItemRepository transferItemRepository;
    private final WarehouseRepository warehouseRepo;
    private final AccountRepository accountRepo;
    private final ProductRepository productRepo;
    private final UnitItemRepository unitItemRepo;
    private final ProductStockService stockService;

    @Transactional
    public TransferResponse create(CreateTransferRequest request) {
        Warehouse fromWarehouse = warehouseRepo.findById(request.getFromWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("From warehouse not found"));
        Warehouse toWarehouse = warehouseRepo.findById(request.getToWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("To warehouse not found"));
        Account cashAccount = accountRepo.findById(request.getCashAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Cash account not found"));
        Account expenseAccount = accountRepo.findById(request.getExpenseAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Expense account not found"));

        Transfer transfer = Transfer.builder()
                .fromWarehouseId(fromWarehouse)
                .toWarehouseId(toWarehouse)
                .cashAccountId(cashAccount)
                .expenseAccountId(expenseAccount)
                .expenseValue(request.getExpenseValue())
                .date(request.getDate())
                .driverName(request.getDriverName())
                .notes(request.getNotes())
                .build();

        List<TransferItem> items = request.getItems().stream().map(itemReq -> {
            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());

            Optional<ProductStock> sourceStock = stockService.findStock(product.getId(), fromWarehouse.getId(), unitItem.getId());
            if (sourceStock.isEmpty()) {
                stockService.createStock(product.getId(), fromWarehouse.getId(), unitItem.getId(), BigDecimal.ZERO);
            }

            Optional<ProductStock> destStock = stockService.findStock(product.getId(), toWarehouse.getId(), unitItem.getId());
            if (destStock.isEmpty()) {
                stockService.createStock(product.getId(), toWarehouse.getId(), unitItem.getId(), BigDecimal.ZERO);
            }


            stockService.decreaseStock(product.getId(), fromWarehouse.getId(), unitItem.getId(), itemReq.getQty());
            stockService.increaseStock(product.getId(), toWarehouse.getId(), unitItem.getId(), itemReq.getQty());

            return TransferItem.builder()
                    .transfer(transfer)
                    .productId(product)
                    .qty(itemReq.getQty())
                    .unitItemId(unitItem)
                    .unitFact(itemReq.getUnitFact())
                    .build();
        }).collect(Collectors.toList());

        transfer.setItems(items);
        Transfer saved = transferRepository.save(transfer);
        return toResponse(saved);
    }



    @Transactional
    public TransferResponse update(Integer id, UpdateTransferRequest req) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));

        if (req.getFromWarehouseId() != null) {
            Warehouse fromWarehouse = warehouseRepo.findById(req.getFromWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("From warehouse not found"));
            transfer.setFromWarehouseId(fromWarehouse);
        }

        if (req.getToWarehouseId() != null) {
            Warehouse toWarehouse = warehouseRepo.findById(req.getToWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("To warehouse not found"));
            transfer.setToWarehouseId(toWarehouse);
        }

        if (req.getCashAccountId() != null) {
            Account cash = accountRepo.findById(req.getCashAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cash account not found"));
            transfer.setCashAccountId(cash);
        }

        if (req.getExpenseAccountId() != null) {
            Account exp = accountRepo.findById(req.getExpenseAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Expense account not found"));
            transfer.setExpenseAccountId(exp);
        }

        if (req.getExpenseValue() != null)
            transfer.setExpenseValue(req.getExpenseValue());

        if (req.getDate() != null)
            transfer.setDate(req.getDate());

        if (req.getDriverName() != null)
            transfer.setDriverName(req.getDriverName());

        if (req.getNotes() != null)
            transfer.setNotes(req.getNotes());

        // If items are provided → fully replace with stock rollback and reapply
        if (req.getItems() != null && !req.getItems().isEmpty()) {
            // 1. Rollback old stock
            for (TransferItem item : transfer.getItems()) {
                stockService.increaseStock(item.getProductId().getId(), transfer.getFromWarehouseId().getId(), item.getUnitItemId().getId(), item.getQty());
                stockService.decreaseStock(item.getProductId().getId(), transfer.getToWarehouseId().getId(), item.getUnitItemId().getId(), item.getQty());
            }

            transfer.getItems().clear();

            // 2. Add new stock
            List<TransferItem> newItems = req.getItems().stream().map(itemReq -> {
                Product product = productRepo.findById(itemReq.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());

                stockService.decreaseStock(product.getId(), transfer.getFromWarehouseId().getId(), unitItem.getId(), itemReq.getQty());
                stockService.increaseStock(product.getId(), transfer.getToWarehouseId().getId(), unitItem.getId(), itemReq.getQty());

                return TransferItem.builder()
                        .transfer(transfer)
                        .productId(product)
                        .qty(itemReq.getQty())
                        .unitItemId(unitItem)
                        .unitFact(itemReq.getUnitFact())
                        .build();
            }).toList();

            transfer.getItems().addAll(newItems);
        }

        return toResponse(transferRepository.save(transfer));
    }



    public List<TransferResponse> getAll() {
        return transferRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TransferResponse getById(Integer id) {
        return transferRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));
    }

    public List<TransferResponse> filterByWarehouseAndDate(Integer warehouseId, LocalDateTime start, LocalDateTime end) {
        return transferRepository.findAll().stream()
                .filter(t -> (warehouseId == null || t.getFromWarehouseId().getId().equals(warehouseId)
                        || t.getToWarehouseId().getId().equals(warehouseId)) &&
                        (t.getDate().isAfter(start.minusSeconds(1)) && t.getDate().isBefore(end.plusSeconds(1)))
                )
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Integer id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));

        //  Reverse stock before deletion
        for (TransferItem item : transfer.getItems()) {
            Integer productId = item.getProductId().getId();
            Integer unitItemId = item.getUnitItemId().getId();
            BigDecimal qty = item.getQty();

            stockService.increaseStock(productId, transfer.getFromWarehouseId().getId(), unitItemId, qty);
            stockService.decreaseStock(productId, transfer.getToWarehouseId().getId(), unitItemId, qty);
        }

        transferRepository.delete(transfer);
    }


    private UnitItem resolveUnitItem(Product product, Integer unitItemId) {
        if (unitItemId != null) {
            return unitItemRepo.findById(unitItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("UnitItem not found"));
        } else {
            return product.getDefaultUnit().getUnitItems().stream()
                    .filter(UnitItem::getIsDef)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Default UnitItem not found"));
        }
    }

    private TransferResponse toResponse(Transfer t) {
        return TransferResponse.builder()
                .id(t.getId())
                .fromWarehouseId(t.getFromWarehouseId().getId())
                .fromWarehouseName(t.getFromWarehouseId().getName())
                .toWarehouseId(t.getToWarehouseId().getId())
                .toWarehouseName(t.getToWarehouseId().getName())
                .cashAccountId(t.getCashAccountId().getId())
                .expenseAccountId(t.getExpenseAccountId().getId())
                .expenseValue(t.getExpenseValue())
                .date(t.getDate())
                .driverName(t.getDriverName())
                .notes(t.getNotes())
                .items(t.getItems().stream().map(this::mapItem).toList())
                .build();
    }

    private TransferItemResponse mapItem(TransferItem i) {
        return TransferItemResponse.builder()
                .id(i.getId())
                .productId(i.getProductId().getId())
                .productName(i.getProductId().getName())
                .qty(i.getQty())
                .unitItemId(i.getUnitItemId() != null ? i.getUnitItemId().getId() : null)
                .unitFact(i.getUnitFact())
                .build();
    }
}
