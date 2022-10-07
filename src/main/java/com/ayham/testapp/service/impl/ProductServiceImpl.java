package com.ayham.testapp.service.impl;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.ayham.testapp.domain.Product;
import com.ayham.testapp.domain.User;
import com.ayham.testapp.repository.ProductRepository;
import com.ayham.testapp.service.Exception.InvalidTokenException;
import com.ayham.testapp.service.Exception.LikeException;
import com.ayham.testapp.service.Exception.ProductDoesNotExistException;
import com.ayham.testapp.service.Exception.UserDoesNotExistException;
import com.ayham.testapp.service.ProductService;
import com.ayham.testapp.service.UserService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    UserService userService;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        log.debug("Request to save Product : {}", product);
        String token = request.getHeader(AUTHORIZATION);
        String currentUserId = userService.getIdFromToken(token);
        if (currentUserId != null && currentUserId.equals(product.getOwner().getId().toString())) {
            return productRepository.save(product);
        } else {
            throw new InvalidTokenException();
        }
    }

    @Override
    public Product update(Product product) {
        log.debug("Request to update Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getTitle() != null) {
                    existingProduct.setTitle(product.getTitle());
                }
                if (product.getDescription() != null) {
                    existingProduct.setDescription(product.getDescription());
                }
                if (product.getStatus() != null) {
                    existingProduct.setStatus(product.getStatus());
                }

                return existingProduct;
            })
            .map(productRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable);
    }

    public Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    @Override
    public void like(Long id) {
        Optional<Product> oProduct = findOne(id);
        if (oProduct.isEmpty()) {
            throw new ProductDoesNotExistException();
        }
        Product product = oProduct.get();
        String currentUserId = userService.getIdFromToken(request.getHeader(AUTHORIZATION));
        if (currentUserId.equals(product.getOwner().getId().toString())) {
            throw new LikeException();
        }
        Optional<User> user = userService.findOne(Long.valueOf(currentUserId));
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        product.getUserLikes().add(user.get());
        update(product);
    }

    @Override
    public void unLike(Long id) {
        Optional<Product> oProduct = findOne(id);
        if (oProduct.isEmpty()) {
            throw new ProductDoesNotExistException();
        }
        Product product = oProduct.get();
        String currentUserId = userService.getIdFromToken(request.getHeader(AUTHORIZATION));
        Optional<User> user = userService.findOne(Long.valueOf(currentUserId));
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        product.getUserLikes().add(user.get());
        update(product);
    }
}
