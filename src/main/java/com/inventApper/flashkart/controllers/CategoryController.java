package com.inventApper.flashkart.controllers;

import com.inventApper.flashkart.dtos.ApiResponseMessage;
import com.inventApper.flashkart.dtos.CategoryDto;
import com.inventApper.flashkart.dtos.ImageResponse;
import com.inventApper.flashkart.dtos.PageableResponse;
import com.inventApper.flashkart.services.CategoryService;
import com.inventApper.flashkart.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.image.path}")
    private String imageUploadPath;

    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    // create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategoryDto = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategoryDto, HttpStatus.CREATED);
    }

    // update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                                      @PathVariable String categoryId) {
        CategoryDto updatedCategoryDto = categoryService.updateCategory(categoryDto, categoryId);
        return ResponseEntity.ok(updatedCategoryDto);
    }

    // delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ApiResponseMessage("category deleted successfully", true, HttpStatus.OK),
                HttpStatus.OK);
    }

    // get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "SortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy,
                sortDir);
        return ResponseEntity.ok(pageableResponse);

    }

    // get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId) {
        CategoryDto categoryDto = categoryService.getSingleCategory(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    // upload category image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryCoverImage(@RequestParam("coverImage") MultipartFile image,
                                                                  @PathVariable("categoryId") String categoryId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        CategoryDto categoryDto = categoryService.getSingleCategory(categoryId);
        categoryDto.setCoverImage(imageName);
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(
                new ImageResponse(imageName, "Image Uploaded succesfully !!", true, HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

    // serve category image
    @GetMapping("/image/{categoryId}")
    public void serveCategoryImage(@PathVariable("categoryId") String categoryId, HttpServletResponse response) throws IOException {

        CategoryDto categoryDto = categoryService.getSingleCategory(categoryId);
        logger.info("Category cover image name : {} ", categoryDto.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}