package com.ayham.testapp.repository;

import com.ayham.testapp.domain.ProductPrice;
import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    @Query(
        "Select pp.price, pp.startDate, pp.endDate from Product p left join ProductPrice pp on p.id=pp.product.id where p.category.id =:categoryId"
    )
    List<ProductPrice> findProductPricesForCategory(@Param("categoryId") Long categoryId);

    List<ProductPrice> findByProductId(Long productId);
}
