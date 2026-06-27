package com.keuangan.app.controller;

import com.keuangan.app.dto.CategoryRequest;
import com.keuangan.app.model.Category;
import com.keuangan.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(Authentication auth) {
        // auth.getName() akan berisi username/email JWT milik user yang sedang login
        return ResponseEntity.ok(categoryService.getCategories(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<?> addCategory(Authentication auth, @RequestBody CategoryRequest req) {
        try {
            return ResponseEntity.ok(categoryService.createCategory(req, auth.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCategory(Authentication auth, @PathVariable Long id, @RequestBody CategoryRequest req) {
        try {
            return ResponseEntity.ok(categoryService.updateCategory(id, req, auth.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(Authentication auth, @PathVariable Long id) {
        try {
            categoryService.deleteCategory(id, auth.getName());
            return ResponseEntity.ok(Map.of("message", "Kategori berhasil dihapus"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}