package com.ayham.testapp.service;

import com.ayham.testapp.domain.ProductPrice;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ProductPrice}.
 */
public interface ProductPriceService {
    /**
     * Save a productPrice.
     *
     * @param productPrice the entity to save.
     * @return the persisted entity.
     */
    ProductPrice save(ProductPrice productPrice);

    /**
     * Updates a productPrice.
     *
     * @param productPrice the entity to update.
     * @return the persisted entity.
     */
    ProductPrice update(ProductPrice productPrice);

    /**
     * Partially updates a productPrice.
     *
     * @param productPrice the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductPrice> partialUpdate(ProductPrice productPrice);

    /**
     * Get all the productPrices.
     *
     * @return the list of entities.
     */
    List<ProductPrice> findAll();

    /**
     * Get the "id" productPrice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductPrice> findOne(Long id);

    /**
     * Delete the "id" productPrice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * calculates the average price for product for the specified period
     *
     * @param productId of the product to calculate for
     * @param startDate of the period
     * @param endDate of the period
     */
    double getAveragePriceForProduct(long productId, Instant startDate, Instant endDate);

    /**
     * calculates the average price for category products for the specified period
     *
     * @param categoryId to calculate for
     * @param startDate of the period
     * @param endDate of the period
     */
    double getAveragePriceForCategory(long categoryId, Instant startDate, Instant endDate);
}
