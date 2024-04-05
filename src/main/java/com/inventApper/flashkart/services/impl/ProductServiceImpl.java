package com.inventApper.flashkart.services.impl;

import com.inventApper.flashkart.dtos.PageableResponse;
import com.inventApper.flashkart.dtos.ProductDto;
import com.inventApper.flashkart.entities.Category;
import com.inventApper.flashkart.entities.Product;
import com.inventApper.flashkart.exceptions.ResourceNotFoundException;
import com.inventApper.flashkart.helper.Helper;
import com.inventApper.flashkart.repositories.CategoryRepository;
import com.inventApper.flashkart.repositories.ProductRepository;
import com.inventApper.flashkart.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper mapper;

    @Value("${product.image.path}")
    private String imagePath;

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String id = UUID.randomUUID().toString();
        productDto.setProductId(id);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        return mapper.map(productRepo.save(product), ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());
        return mapper.map(productRepo.save(product), ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));

        // delete product image
        try {
            String fullPath = imagePath + product.getProductImageName();
            Files.delete(Paths.get(fullPath));
        } catch (NoSuchFileException ex) {
            logger.info("Product image not found in folder ");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        productRepo.delete(product);
    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        return mapper.map(productRepo.save(product), ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.findAll(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String title, int pageNumber, int pageSize, String sortBy,
            String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.findByTitleContaining(title, pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize,
            String sortBy, String sortDir) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.findByCategory(category, pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        // fetch Category
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));
        String id = UUID.randomUUID().toString();
        productDto.setProductId(id);
        Product product = mapper.map(productDto, Product.class);
        product.setAddedDate(new Date());
        product.setCategory(category);
        return mapper.map(productRepo.save(product), ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));
        product.setCategory(category);
        return mapper.map(productRepo.save(product), ProductDto.class);
    }

}
