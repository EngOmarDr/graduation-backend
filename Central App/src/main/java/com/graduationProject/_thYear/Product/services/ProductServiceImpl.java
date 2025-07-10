package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.Account.dtos.response.AccountResponse;
import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Group.repositories.GroupRepository;
import com.graduationProject._thYear.Product.dtos.request.*;
import com.graduationProject._thYear.Product.dtos.response.ProductBarcodeResponse;
import com.graduationProject._thYear.Product.dtos.response.ProductPriceResponse;
import com.graduationProject._thYear.Product.dtos.response.ProductResponse;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.models.ProductBarcode;
import com.graduationProject._thYear.Product.models.ProductPrice;
import com.graduationProject._thYear.Product.repositories.ProductBarcodeRepository;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.Unit.models.Unit;
import com.graduationProject._thYear.Unit.repositories.UnitRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final GroupRepository groupRepository;
    private final UnitRepository unitRepository;
    private final ProductBarcodeRepository productBarcodeRepository;
    private final ProductPriceService productPriceService;
    private final ProductBarcodeService productBarcodeService;
    private final ImageStorageService imageStorageService;
    @PersistenceContext
    private EntityManager entityManager;





    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        String tempImagePath = null;
        try {
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                tempImagePath = imageStorageService.saveToTemp(request.getImage());
            }

            // Check if product with same code or name already exists
            if (productRepository.existsByCode(request.getCode())) {
                throw new IllegalArgumentException("Product with code " + request.getCode() + " already exists");
            }
            if (productRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Product with name " + request.getName() + " already exists");
            }

            // Fetch related entities
            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + request.getGroupId()));
            Unit defaultUnit = unitRepository.findById(request.getDefaultUnitId())
                    .orElseThrow(() -> new EntityNotFoundException("Unit not found with id: " + request.getDefaultUnitId()));

            // Create and save product
            Product product = Product.builder()
                    .code(request.getCode())
                    .name(request.getName())
                    .groupId(group)
                    .type(request.getType())
                    .defaultUnit(defaultUnit)
                    .minQty(request.getMinQty())
                    .maxQty(request.getMaxQty())
                    .orderQty(request.getOrderQty())
                    .notes(request.getNotes())
                    .quantity(0f) // Initial quantity is 0
                    .build();

            Product savedProduct = productRepository.save(product);

            if (tempImagePath != null) {
                String permanentImagePath = imageStorageService.moveToPermanent(tempImagePath);
                product.setImage(permanentImagePath);
                productRepository.save(product);
            }

            // Process prices
            if (request.getPrices() != null) {
                for (CreateProductPriceRequest priceRequest : request.getPrices()) {
                    priceRequest.setProductId(savedProduct.getId());
                    productPriceService.createProductPrice(priceRequest);
                }
            }

            // Process barcodes
            if (request.getBarcodes() != null) {
                for (CreateProductBarcodeRequest barcodeRequest : request.getBarcodes()) {
                    barcodeRequest.setProductId(savedProduct.getId());
                    productBarcodeService.createProductBarcode(barcodeRequest);
                }
            }
            entityManager.refresh(savedProduct);

            return mapToProductResponse(savedProduct);

        } catch (Exception e) {
            if (tempImagePath != null) imageStorageService.deleteTemp(tempImagePath);
            throw e;
        }
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        String tempImagePath = null;

        try {
            // Update code
            if (request.getCode() != null && !product.getCode().equals(request.getCode())) {
                if (productRepository.existsByCode(request.getCode())) {
                    throw new IllegalArgumentException("Another product with code " + request.getCode() + " already exists");
                }
                product.setCode(request.getCode());
            }

            // Update name
            if (request.getName() != null && !product.getName().equals(request.getName())) {
                if (productRepository.existsByName(request.getName())) {
                    throw new IllegalArgumentException("Another product with name " + request.getName() + " already exists");
                }
                product.setName(request.getName());
            }

            // Group
            if (request.getGroupId() != null) {
                Group group = groupRepository.findById(request.getGroupId())
                        .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + request.getGroupId()));
                product.setGroupId(group);
            }

            // Default unit
            if (request.getDefaultUnitId() != null) {
                Unit defaultUnit = unitRepository.findById(request.getDefaultUnitId())
                        .orElseThrow(() -> new EntityNotFoundException("Unit not found with id: " + request.getDefaultUnitId()));
                product.setDefaultUnit(defaultUnit);
            }

            // Type
            if (request.getType() != null) {
                product.setType(request.getType());
            }

            if (request.getMinQty() != null) product.setMinQty(request.getMinQty());
            if (request.getMaxQty() != null) product.setMaxQty(request.getMaxQty());
            if (request.getOrderQty() != null) product.setOrderQty(request.getOrderQty());
            if (request.getNotes() != null) product.setNotes(request.getNotes());

            // Handle image upload
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                tempImagePath = imageStorageService.saveToTemp(request.getImage());
            }

            // Prices
            if (request.getPrices() != null) {
                for (ProductPrice price : product.getPrices()) {
                    productPriceService.deleteProductPrice(price.getId());
                }
                product.getPrices().clear();

                for (CreateProductPriceRequest priceRequest : request.getPrices()) {
                    priceRequest.setProductId(product.getId());
                    productPriceService.createProductPrice(priceRequest);
                }
            }

            // Barcodes
            if (request.getBarcodes() != null) {
                for (ProductBarcode barcode : product.getBarcodes()) {
                    productBarcodeService.deleteProductBarcode(barcode.getId());
                }
                product.getBarcodes().clear();

                for (CreateProductBarcodeRequest barcodeRequest : request.getBarcodes()) {
                    barcodeRequest.setProductId(product.getId());
                    productBarcodeService.createProductBarcode(barcodeRequest);
                }
            }

            Product saved = productRepository.save(product);

            // Move image to permanent if needed
            if (tempImagePath != null) {
                imageStorageService.deletePermanent(product.getImage());
                String finalPath = imageStorageService.moveToPermanent(tempImagePath);
                product.setImage(finalPath);
                productRepository.save(product);
            }

            entityManager.refresh(saved);
            return mapToProductResponse(saved);

        } catch (Exception ex) {
            if (tempImagePath != null) {
                imageStorageService.deleteTemp(tempImagePath);
            }
            throw ex;
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // Delete associated prices and barcodes first
        productPriceService.getProductPricesByProductId(id).forEach(price ->
                productPriceService.deleteProductPrice(price.getId()));
        productBarcodeService.getProductBarcodesByProductId(id).forEach(barcode ->
                productBarcodeService.deleteProductBarcode(barcode.getId()));

        productRepository.delete(product);
    }

    public List<ProductResponse> getByBarcode(String barcode) {
        List<ProductBarcode> matches = productBarcodeRepository.findAllByBarcode(barcode);

        if (matches.isEmpty()) {
            throw new ResourceNotFoundException("No products found with barcode: " + barcode);
        }

        return matches.stream()
                .map(ProductBarcode::getProduct)
                .distinct()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String searchTerm) {
        return productRepository.searchByNameOrCode(searchTerm).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
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
                .typeName(getTypeName(product.getType()))
                .defaultUnitId(product.getDefaultUnit().getId())
                .quantity(product.getQuantity())
                .minQty(product.getMinQty())
                .maxQty(product.getMaxQty())
                .orderQty(product.getOrderQty())
                .notes(product.getNotes())
                .prices(product.getPrices().stream()
                        .map(p -> convertPriceToResponse(p, product.getId(), product.getName()))
                        .collect(Collectors.toList()))
                .barcodes(product.getBarcodes().stream()
                        .map(b -> convertBarcodeToResponse(b,product.getId(), product.getName()))
                        .collect(Collectors.toList()))
                .build();
    }
    private String getTypeName(Byte type) {
        return switch (type) {
            case Product.TYPE_WAREHOUSE -> "TYPE_WAREHOUSE ";
            case Product.TYPE_SERVICE -> "TYPE_SERVICE ";
            default -> "anonymous";
        };
    }
//    private List<ProductPriceResponse> convertPricesToResponse(List<ProductPrice> prices) {
//        return prices.stream()
//                .map(this::convertPriceToResponse)
//                .collect(Collectors.toList());
//    }

    private ProductPriceResponse convertPriceToResponse(ProductPrice price, Integer productId, String productName) {
        return ProductPriceResponse.builder()
                .id(price.getId())
                .productId(productId)
                .productName(productName)
                .priceId(price.getPriceId().getId())
                .priceName(price.getPriceId().getName())
                .price(price.getPrice())
                .unitItemId(price.getPriceUnit().getId())
                .unitItemName(price.getPriceUnit().getName())
                .build();
    }

//    private List<ProductBarcodeResponse> convertBarcodesToResponse(List<ProductBarcode> barcodes) {
//        return barcodes.stream()
//                .map(this::convertBarcodeToResponse)
//                .collect(Collectors.toList());
//    }

    private ProductBarcodeResponse convertBarcodeToResponse(ProductBarcode barcode, Integer productId, String productName) {
        return ProductBarcodeResponse.builder()
                .id(barcode.getId())
                .productId(productId)
                .productName(productName)
                .barcode(barcode.getBarcode())
                .unitItemId(barcode.getUnitItem().getId())
                .unitItemName(barcode.getUnitItem().getName())
                .build();
    }
}
