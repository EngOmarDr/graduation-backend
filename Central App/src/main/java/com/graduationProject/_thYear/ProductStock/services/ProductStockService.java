package com.graduationProject._thYear.ProductStock.services;

import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Product.dtos.response.ProductResponse;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.Product.services.ProductServiceImpl;
import com.graduationProject._thYear.ProductStock.dtos.requests.CreateProductStockRequest;
import com.graduationProject._thYear.ProductStock.dtos.requests.UpdateProductStockRequest;
import com.graduationProject._thYear.ProductStock.dtos.responses.ProductStockResponse;
import com.graduationProject._thYear.ProductStock.dtos.responses.ProductWithStockResponse;
import com.graduationProject._thYear.ProductStock.models.ProductStock;
import com.graduationProject._thYear.ProductStock.repositories.ProductStockRepository;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductStockRepository stockRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;
    private final UnitItemRepository unitItemRepo;
    private final ProductServiceImpl productService;

    @Transactional
    public ProductStockResponse create(CreateProductStockRequest req) {
        Product product = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        UnitItem unitItem = unitItemRepo.findById(req.getUnitItemId())
                .orElseThrow(() -> new RuntimeException("UnitItem not found"));

        if (stockRepo.findByProductIdAndWarehouseIdAndUnitItemId(product.getId(), warehouse.getId(), unitItem.getId()).isPresent()) {
            throw new RuntimeException("Stock already exists for this product, warehouse and unit item");
        }

        ProductStock stock = ProductStock.builder()
                .product(product)
                .warehouse(warehouse)
                .unitItem(unitItem)
                .quantity(req.getQuantity())
                .build();

        return toResponse(stockRepo.save(stock));
    }

    public void createStock(Integer productId, Integer warehouseId, Integer unitItemId, BigDecimal quantity) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Warehouse warehouse = warehouseRepo.findById(warehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        UnitItem unitItem = unitItemRepo.findById(unitItemId).orElseThrow(() -> new ResourceNotFoundException("UnitItem not found"));

        ProductStock stock = ProductStock.builder()
                .product(product)
                .warehouse(warehouse)
                .unitItem(unitItem)
                .quantity(quantity)
                .build();

        stockRepo.save(stock);
    }

    public List<ProductStockResponse> getAll() {
        return stockRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductStockResponse getById(Integer id) {
        return stockRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("ProductStock not found"));
    }

    @Transactional
    public ProductStockResponse update(Integer id, UpdateProductStockRequest req) {
        ProductStock stock = stockRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductStock not found"));

        if (req.getProductId() != null) {
            Product product = productRepo.findById(req.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            stock.setProduct(product);
        }

        if (req.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            stock.setWarehouse(warehouse);
        }

        if (req.getUnitItemId() != null) {
            UnitItem unitItem = unitItemRepo.findById(req.getUnitItemId())
                    .orElseThrow(() -> new RuntimeException("UnitItem not found"));
            stock.setUnitItem(unitItem);
        }

        if (req.getQuantity() != null) {
            stock.setQuantity(req.getQuantity());
        }

        return toResponse(stockRepo.save(stock));
    }

    public void delete(Integer id) {
        ProductStock stock = stockRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product stock not found with id: " + id));
        stockRepo.delete(stock);
    }

    public List<ProductWithStockResponse> getByWarehouseId(Integer warehouseId) {
        return stockRepo.findByWarehouseId(warehouseId).stream()
                .map(stock -> ProductWithStockResponse.builder()
                        .product(mapToProductResponse(stock.getProduct())) // reuse your existing mapper!
                        .stockId(stock.getId())
                        .warehouseId(stock.getWarehouse().getId())
                        .warehouseName(stock.getWarehouse().getName())
                        .unitItemId(stock.getUnitItem().getId())
                        .unitItemName(stock.getUnitItem().getName())
                        .quantity(stock.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }


    public List<ProductStockResponse> getByProductId(Integer productId) {
        return stockRepo.findByProductId(productId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void increaseStock(Integer productId, Integer warehouseId, Integer unitItemId, BigDecimal qtyToAdd) {
        ProductStock stock = stockRepo.findByProductIdAndWarehouseIdAndUnitItemId(productId, warehouseId, unitItemId)
                .orElseThrow(() -> new RuntimeException("Stock record not found"));
        stock.setQuantity(stock.getQuantity().add(qtyToAdd));
        stockRepo.save(stock);
    }

    @Transactional
    public void decreaseStock(Integer productId, Integer warehouseId, Integer unitItemId, BigDecimal qtyToSubtract) {
        ProductStock stock = stockRepo.findByProductIdAndWarehouseIdAndUnitItemId(productId, warehouseId, unitItemId)
                .orElseThrow(() -> new RuntimeException("Stock record not found"));

        if (stock.getQuantity().compareTo(qtyToSubtract) < 0) {
            throw new RuntimeException("Not enough stock. Current quantity: " + stock.getQuantity());
        }

        stock.setQuantity(stock.getQuantity().subtract(qtyToSubtract));
        stockRepo.save(stock);
    }

    public ProductStockResponse getQuantity(Integer productId, Integer warehouseId, Integer unitItemId) {
        ProductStock stock = stockRepo.findByProductIdAndWarehouseIdAndUnitItemId(productId, warehouseId, unitItemId)
                .orElseThrow(() -> new RuntimeException("Stock record not found"));
        return toResponse(stock);
    }

    public Optional<ProductStock> findStock(Integer productId, Integer warehouseId, Integer unitItemId) {
        return stockRepo.findByProductIdAndWarehouseIdAndUnitItemId(productId, warehouseId, unitItemId);
    }


    private ProductStockResponse toResponse(ProductStock stock) {
        return ProductStockResponse.builder()
                .id(stock.getId())
                .productId(stock.getProduct().getId())
                .productName(stock.getProduct().getName())
                .warehouseId(stock.getWarehouse().getId())
                .warehouseName(stock.getWarehouse().getName())
                .unitItemId(stock.getUnitItem().getId())
                .unitItemName(stock.getUnitItem().getName())
                .quantity(stock.getQuantity())
                .build();
    }


    private ProductResponse mapToProductResponse(Product product) {
        Hibernate.initialize(product.getPrices());
        Hibernate.initialize(product.getBarcodes());

        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .image(product.getImage())
                .groupId(product.getGroupId().getId())
                .type(product.getType())
                .typeName(productService.getTypeName(product.getType()))
                .defaultUnitId(product.getDefaultUnit().getId())
                .minQty(product.getMinQty())
                .maxQty(product.getMaxQty())
                .orderQty(product.getOrderQty())
                .notes(product.getNotes())
                .prices(productService.convertPricesToResponse(product.getPrices()))
                .barcodes(productService.convertBarcodesToResponse(product.getBarcodes()))
                .build();
    }
}
