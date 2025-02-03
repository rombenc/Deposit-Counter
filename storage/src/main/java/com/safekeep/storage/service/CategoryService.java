package com.safekeep.storage.service;

import com.safekeep.storage.model.entity.Category;
import com.safekeep.storage.model.request.CategoryRequest;
import com.safekeep.storage.model.response.CategoryResponse;
import com.safekeep.storage.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryResponse createCategory (CategoryRequest payload) {
        var category = Category.builder()
                .name(payload.getName())
                .build();
        return toCategoryResponse(repository.saveAndFlush(category));
    }

    public List<CategoryResponse> getAllCategory (String name) {
        if (name != null){
            return repository.findAllByNameLikeOrderByNameAsc("%" + name + "%")
                    .stream()
                    .map(this::toCategoryResponse)
                    .toList();
        }
        return repository.findAll().stream().map(this::toCategoryResponse).toList();
    }

    public CategoryResponse updateCategory (Long id, CategoryRequest payload){
        Category category = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Cannot find category with given name"));
        category.setName(payload.getName());
        return toCategoryResponse(repository.saveAndFlush(category));
    }

    public void delete(Long id) {
        Optional<Category> customer = repository.findById(id);

        if (customer.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Category with ID " + id + " not found.");
        }
    }

    private CategoryResponse toCategoryResponse (Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
