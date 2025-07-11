package com.graduationProject._thYear.ProductStock.services;

import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.ProductStock.dtos.requests.CreateProductStockRequest;
import com.graduationProject._thYear.ProductStock.dtos.requests.UpdateProductStockRequest;
import com.graduationProject._thYear.ProductStock.dtos.responses.ProductStockResponse;
import com.graduationProject._thYear.ProductStock.models.ProductStock;
import com.graduationProject._thYear.ProductStock.repositories.ProductStockRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductStockRepository stockRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;

    @Transactional
    public ProductStockResponse create(CreateProductStockRequest req) {
        Product product = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        if (stockRepo.findByProductIdAndWarehouseId(product.getId(), warehouse.getId()).isPresent()) {
            throw new RuntimeException("Stock already exists for this product and warehouse");
        }

        ProductStock stock = ProductStock.builder()
                .product(product)
                .warehouse(warehouse)
                .quantity(req.getQuantity())
                .build();

        return toResponse(stockRepo.save(stock));
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

        if (req.getQuantity() != null) {
            stock.setQuantity(req.getQuantity());
        }

        return toResponse(stockRepo.save(stock));
    }

    public void delete(Integer id) {
        stockRepo.deleteById(id);
    }

    public List<ProductStockResponse> getByWarehouseId(Integer warehouseId) {
        return stockRepo.findByWarehouseId(warehouseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductStockResponse> getByProductId(Integer productId) {
        return stockRepo.findByProductId(productId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void increaseStock(Integer productId, Integer warehouseId, BigDecimal qtyToAdd) {
        ProductStock stock = stockRepo.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("Stock record not found"));
        stock.setQuantity(stock.getQuantity().add(qtyToAdd));
        stockRepo.save(stock);
    }

    @Transactional
    public void decreaseStock(Integer productId, Integer warehouseId, BigDecimal qtyToSubtract) {
        ProductStock stock = stockRepo.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("Stock record not found"));

        if (stock.getQuantity().compareTo(qtyToSubtract) < 0) {
            throw new RuntimeException("Not enough stock. Current quantity: " + stock.getQuantity());
        }

        stock.setQuantity(stock.getQuantity().subtract(qtyToSubtract));
        stockRepo.save(stock);
    }

    public ProductStockResponse getQuantity(Integer productId, Integer warehouseId) {
        ProductStock stock = stockRepo.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("Stock record not found"));
        // return stock.getQuantity();
        return toResponse(stock);
    }




    private ProductStockResponse toResponse(ProductStock stock) {
        return ProductStockResponse.builder()
                .id(stock.getId())
                .productId(stock.getProduct().getId())
                .productName(stock.getProduct().getName())
                .warehouseId(stock.getWarehouse().getId())
                .warehouseName(stock.getWarehouse().getName())
                .quantity(stock.getQuantity())
                .build();
    }
}
