package com.ayham.testapp.service.impl;

import com.ayham.testapp.domain.ProductPrice;
import com.ayham.testapp.repository.ProductPriceRepository;
import com.ayham.testapp.service.ProductPriceService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductPrice}.
 */
@Service
@Transactional
public class ProductPriceServiceImpl implements ProductPriceService {

    private final Logger log = LoggerFactory.getLogger(ProductPriceServiceImpl.class);

    private final ProductPriceRepository productPriceRepository;

    public ProductPriceServiceImpl(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    @Override
    public ProductPrice save(ProductPrice productPrice) {
        log.debug("Request to save ProductPrice : {}", productPrice);
        return productPriceRepository.save(productPrice);
    }

    @Override
    public ProductPrice update(ProductPrice productPrice) {
        log.debug("Request to update ProductPrice : {}", productPrice);
        return productPriceRepository.save(productPrice);
    }

    @Override
    public Optional<ProductPrice> partialUpdate(ProductPrice productPrice) {
        log.debug("Request to partially update ProductPrice : {}", productPrice);

        return productPriceRepository
            .findById(productPrice.getId())
            .map(existingProductPrice -> {
                if (productPrice.getStartDate() != null) {
                    existingProductPrice.setStartDate(productPrice.getStartDate());
                }
                if (productPrice.getEndDate() != null) {
                    existingProductPrice.setEndDate(productPrice.getEndDate());
                }
                if (productPrice.getPrice() != null) {
                    existingProductPrice.setPrice(productPrice.getPrice());
                }

                return existingProductPrice;
            })
            .map(productPriceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPrice> findAll() {
        log.debug("Request to get all ProductPrices");
        return productPriceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductPrice> findOne(Long id) {
        log.debug("Request to get ProductPrice : {}", id);
        return productPriceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductPrice : {}", id);
        productPriceRepository.deleteById(id);
    }

    @Override
    public double getAveragePriceForProduct(long productId, Instant startDate, Instant endDate) {
        List<ProductPrice> prices = productPriceRepository.findByProductId(productId);
        OptionalDouble average = calculateAverage(prices, startDate, endDate);
        return average.isPresent() ? average.getAsDouble() : 0;
    }

    @Override
    public double getAveragePriceForCategory(long categoryId, Instant startDate, Instant endDate) {
        List<ProductPrice> prices = productPriceRepository.findProductPricesForCategory(categoryId);
        OptionalDouble average = calculateAverage(prices, startDate, endDate);
        return average.isPresent() ? average.getAsDouble() : 0;
    }

    private OptionalDouble calculateAverage(List<ProductPrice> prices, Instant startDate, Instant endDate) {
        return prices.stream().filter(p -> isIncludedInRange(p, startDate, endDate)).mapToDouble(ProductPrice::getPrice).average();
    }

    private boolean isIncludedInRange(ProductPrice price, Instant startDate, Instant endDate) {
        return (
            (price.getStartDate().isAfter(startDate) && price.getEndDate().isBefore(endDate)) ||
            startDate.isAfter(price.getStartDate()) &&
            startDate.isBefore(price.getEndDate()) ||
            endDate.isAfter(price.getStartDate()) &&
            endDate.isBefore(price.getEndDate())
        );
    }
}
