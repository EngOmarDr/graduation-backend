package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Group.repositories.GroupRepository;
import com.graduationProject._thYear.Product.dtos.request.*;
import com.graduationProject._thYear.Product.dtos.response.ProductBarcodeResponse;
import com.graduationProject._thYear.Product.dtos.response.ProductPriceResponse;
import com.graduationProject._thYear.Product.dtos.response.ProductResponse;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.models.ProductBarcode;
import com.graduationProject._thYear.Product.models.ProductPrice;
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
    private final ProductPriceService productPriceService;
    private final ProductBarcodeService productBarcodeService;
    @PersistenceContext
    private EntityManager entityManager;




    private String saveImageToDisk(MultipartFile image) {
        if (image == null || image.isEmpty()) return null;

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/images";
            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Could not create directory: " + uploadDir);
            }

            String originalName = image.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new RuntimeException("Invalid image file name");
            }

            String extension = originalName.substring(originalName.lastIndexOf('.'));
            String uniqueFileName = UUID.randomUUID() + extension;
            String fullPath = uploadDir + File.separator + uniqueFileName;

            System.out.println("Saving to: " + fullPath);

            image.transferTo(new File(fullPath));

            return "/images/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace(); // Log the real error
            throw new RuntimeException("Failed to store image", e);
        }
    }


    private void deleteOldImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) return;

        String fullPath = System.getProperty("user.dir") + "/uploads" + imagePath;
        File file = new File(fullPath);
        if (file.exists()) file.delete();
    }

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
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
                .image(saveImageToDisk(request.getImage()))
                .groupId(group)
                .defaultUnit(defaultUnit)
                .minQty(request.getMinQty())
                .maxQty(request.getMaxQty())
                .orderQty(request.getOrderQty())
                .notes(request.getNotes())
                .quantity(0f) // Initial quantity is 0
                .build();

        Product savedProduct = productRepository.save(product);

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

        if (!product.getCode().equals(request.getCode()) && productRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Another product with code " + request.getCode() + " already exists");
        }

        if (!product.getName().equals(request.getName()) && productRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Another product with name " + request.getName() + " already exists");
        }

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + request.getGroupId()));

        Unit defaultUnit = unitRepository.findById(request.getDefaultUnitId())
                .orElseThrow(() -> new EntityNotFoundException("Unit not found with id: " + request.getDefaultUnitId()));

        // Optional: Replace image if new one is uploaded
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            deleteOldImage(product.getImage()); // Delete old image
            String newImagePath = saveImageToDisk(request.getImage());
            product.setImage(newImagePath);
        }

        // Update fields
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setGroupId(group);
        product.setDefaultUnit(defaultUnit);
        product.setMinQty(request.getMinQty());
        product.setMaxQty(request.getMaxQty());
        product.setOrderQty(request.getOrderQty());
        product.setNotes(request.getNotes());
        // ✅ Delete old prices
        for (ProductPrice price : product.getPrices()) {
            productPriceService.deleteProductPrice(price.getId());
        }
        product.getPrices().clear();

        for (CreateProductPriceRequest priceRequest : request.getPrices()) {
            priceRequest.setProductId(product.getId());
            productPriceService.createProductPrice(priceRequest);
        }


        // ✅ Delete old barcodes
        for (ProductBarcode barcode : product.getBarcodes()) {
            productBarcodeService.deleteProductBarcode(barcode.getId());
        }
        product.getBarcodes().clear();


        for (CreateProductBarcodeRequest barcodeRequest : request.getBarcodes()) {
            barcodeRequest.setProductId(product.getId());
            productBarcodeService.createProductBarcode(barcodeRequest);
        }

        Product saved = productRepository.save(product);
        entityManager.refresh(saved);
        return mapToProductResponse(saved);
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

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .image(product.getImage())
                .groupId(product.getGroupId().getId())
                .defaultUnitId(product.getDefaultUnit().getId())
                .quantity(product.getQuantity())
                .minQty(product.getMinQty())
                .maxQty(product.getMaxQty())
                .orderQty(product.getOrderQty())
                .notes(product.getNotes())
                .prices(product.getPrices().stream()
                        .map(this::convertPriceToResponse)
                        .collect(Collectors.toList()))
                .barcodes(product.getBarcodes().stream()
                        .map(this::convertBarcodeToResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private List<ProductPriceResponse> convertPricesToResponse(List<ProductPrice> prices) {
        return prices.stream()
                .map(this::convertPriceToResponse)
                .collect(Collectors.toList());
    }

    private ProductPriceResponse convertPriceToResponse(ProductPrice price) {
        return ProductPriceResponse.builder()
                .id(price.getId())
                .productId(price.getProductId().getId())
                .productName(price.getProductId().getName())
                .priceId(price.getPriceId().getId())
                .priceName(price.getPriceId().getName())
                .price(price.getPrice())
                .unitItemId(price.getPriceUnit().getId())
                .unitItemName(price.getPriceUnit().getName())
                .build();
    }

    private List<ProductBarcodeResponse> convertBarcodesToResponse(List<ProductBarcode> barcodes) {
        return barcodes.stream()
                .map(this::convertBarcodeToResponse)
                .collect(Collectors.toList());
    }

    private ProductBarcodeResponse convertBarcodeToResponse(ProductBarcode barcode) {
        return ProductBarcodeResponse.builder()
                .id(barcode.getId())
                .productId(barcode.getProduct().getId())
                .productName(barcode.getProduct().getName())
                .barcode(barcode.getBarcode())
                .unitItemId(barcode.getUnitItem().getId())
                .unitItemName(barcode.getUnitItem().getName())
                .build();
    }
}
