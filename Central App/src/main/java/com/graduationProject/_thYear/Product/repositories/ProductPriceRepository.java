    package com.graduationProject._thYear.Product.repositories;


    import com.graduationProject._thYear.Product.models.ProductPrice;
    import com.graduationProject._thYear.Unit.models.Unit;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    @Repository
    public interface ProductPriceRepository extends JpaRepository<ProductPrice,Integer> {
        // Change method names to match entity relationships
        List<ProductPrice> findByProductId_Id(Integer productId);
        List<ProductPrice> findByPriceId_Id(Integer priceId);
        boolean existsByProductId_IdAndPriceId_IdAndPriceUnit(Integer productId, Integer priceId, Unit priceUnit);
    }
