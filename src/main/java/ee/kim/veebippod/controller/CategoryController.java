package ee.kim.veebippod.controller;

import ee.kim.veebippod.dto.CategoryDto;
import ee.kim.veebippod.entity.Category;
import ee.kim.veebippod.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    //lisamine
    @PostMapping("categories")
    public Category addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    //kustutamine
    @DeleteMapping("categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    //muutmine
    @PutMapping("categories/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }
}
