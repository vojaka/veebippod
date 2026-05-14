package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.CategoryDto;
import ee.kim.veebippod.entity.Category;
import ee.kim.veebippod.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;


    @GetMapping("categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    //lisamine
    @PostMapping("categories")
    public Category addCategory(@RequestBody CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.name());
        return categoryRepository.save(category);
    }

    //kustutamine
    @DeleteMapping("categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }

    //muutmine
    @PutMapping("categories/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow();
        existingCategory.setName(categoryDto.name());
        return categoryRepository.save(existingCategory);
    }
}
