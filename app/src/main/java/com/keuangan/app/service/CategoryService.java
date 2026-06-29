package com.keuangan.app.service;

import com.keuangan.app.dto.CategoryRequest;
import com.keuangan.app.model.Category;
import com.keuangan.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getCategories(String userId) {
        // 💡 Sekarang mengambil data milik user + data master dari SYSTEM
        return categoryRepository.findAllByUserIdOrSystem(userId);
    }

    public Category createCategory(CategoryRequest req, String userId) {
        if (categoryRepository.existsByNameTypeAndUserOrSystem(req.getName().trim(), req.getType(), userId)) {
            throw new IllegalArgumentException("Kategori '" + req.getName() + "' sudah tersedia");
        }
        Category category = new Category(req.getName().trim().toUpperCase(), req.getType().toUpperCase(), userId);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequest req, String userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategori tidak ditemukan"));
        
        // 💡 CEK KEPEMILIKAN
        if (!category.getUserId().equals(userId)) {
            throw new SecurityException("Akses ditolak: Ini bukan kategori Anda.");
        }
        
        category.setName(req.getName().trim().toUpperCase());
        // Tipe (INCOME/EXPENSE) sebaiknya tidak diubah agar riwayat transaksi tidak rusak
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id, String userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategori tidak ditemukan"));

        if (!category.getUserId().equals(userId)) {
            throw new SecurityException("Akses ditolak: Ini bukan kategori Anda.");
        }
        categoryRepository.deleteById(id);
    }
}