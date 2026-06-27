package com.keuangan.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.keuangan.app.enums.UserRole;
import com.keuangan.app.enums.UserStatus;
import com.keuangan.app.model.Category;
import com.keuangan.app.model.User;
import com.keuangan.app.repository.CategoryRepository;
import com.keuangan.app.repository.UserRepository;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("DataInitializer dijalankan...");

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@financebuddy.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNamaLengkap("Administrator");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.TERVALIDASI);
            userRepository.save(admin);
            System.out.println("Admin default berhasil dibuat.");
        }

        System.out.println("Memeriksa data master kategori...");
        
        List<Category> allCategories = categoryRepository.findAll();

        // 💡 FIX: Semuanya diseragamkan pakai parameter ke-4 yaitu "admin"
        ensureCategoryExists(allCategories, "MAKANAN", "EXPENSE", "admin");
        ensureCategoryExists(allCategories, "TRANSPORTASI", "EXPENSE", "admin");
        ensureCategoryExists(allCategories, "BELAJAR", "EXPENSE", "admin");
        ensureCategoryExists(allCategories, "KOST", "EXPENSE", "admin");
        ensureCategoryExists(allCategories, "HIBURAN", "EXPENSE", "admin");
        ensureCategoryExists(allCategories, "TAGIHAN", "EXPENSE", "admin");

        ensureCategoryExists(allCategories, "UANG_SAKU", "INCOME", "admin");
        ensureCategoryExists(allCategories, "GAJI_PART_TIME", "INCOME", "admin");
        ensureCategoryExists(allCategories, "FREELANCE", "INCOME", "admin");
        ensureCategoryExists(allCategories, "BONUS", "INCOME", "admin");

        System.out.println("Data master kategori aman terkendali!");
    }

    // 💡 FIX: Tambahkan parameter `String userId` di sini
    private void ensureCategoryExists(List<Category> existingCategories, String name, String type, String userId) {
        boolean exists = existingCategories.stream()
                .anyMatch(c -> c.getName() != null && c.getName().equalsIgnoreCase(name) 
                            && c.getType() != null && c.getType().equalsIgnoreCase(type)
                            && c.getUserId() != null && c.getUserId().equals(userId));

        if (!exists) {
            // 💡 FIX: Gunakan constructor yang butuh 3 variabel (Nama, Tipe, UserId)
            categoryRepository.save(new Category(name, type, userId));
            System.out.println("Kategori baru berhasil ditambahkan: " + name + " (" + type + ")");
        }
    }
}