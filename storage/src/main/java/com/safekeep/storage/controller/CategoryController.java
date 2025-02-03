package com.safekeep.storage.controller;

import com.safekeep.storage.model.request.CategoryRequest;
import com.safekeep.storage.model.response.CategoryResponse;
import com.safekeep.storage.model.response.CommonResponse;
import com.safekeep.storage.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CommonResponse<CategoryResponse>> create (@RequestBody CategoryRequest request){
        var category = categoryService.createCategory(request);
        CommonResponse<CategoryResponse> response = CommonResponse.<CategoryResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully creating category")
                .data(category)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CategoryResponse>>> getAll (
            @RequestParam(name = "name", required = false) String name) {
        List<CategoryResponse> categoryResponseList = categoryService.getAllCategory(name);
        CommonResponse<List<CategoryResponse>> response = CommonResponse.<List<CategoryResponse>>builder()
                .statusCode(HttpStatus.FOUND.value())
                .message("Successfully retrieving cateory/es")
                .data(categoryResponseList)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update{id}")
    public ResponseEntity<CommonResponse<CategoryResponse>> updateCategory (
            @PathVariable Long id, @RequestBody CategoryRequest request) {
        CategoryResponse updatedCategory = categoryService.updateCategory(id, request);
        CommonResponse<CategoryResponse> response = CommonResponse.<CategoryResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success updating category")
                .data(updatedCategory)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        CommonResponse<Void> response = CommonResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer deleted successfully!")
                .build();
        return ResponseEntity.ok(response);
    }

}
