package com.keuangan.app.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keuangan.app.dto.IncomeRequest;
import com.keuangan.app.model.Transaction;
import com.keuangan.app.repository.CategoryRepository;
import com.keuangan.app.repository.TransactionRepository;

@Service
@Transactional
public class IncomeService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Transaction> getAllIncomes(String userId) {
        return transactionRepository.findByUserIdOrderByTanggalDescIdDesc(userId).stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .collect(Collectors.toList());
    }

    public String saveIncome(IncomeRequest request, String userId) {
        if (request.getNominal() == null || request.getNominal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Nominal pemasukan harus lebih besar dari 0");
        }

        categoryRepository.findByNameTypeAndUserOrSystem(request.getKategori(), "INCOME", userId)
        .orElseThrow(() -> new IllegalArgumentException("Kategori '" + request.getKategori() + "' tidak valid untuk pemasukan"));

        Transaction t = new Transaction();
        t.setUserId(userId);
        t.setType("INCOME");
        t.setKategori(request.getKategori());
        t.setNominal(request.getNominal());
        t.setKeterangan(request.getKeterangan());
        t.setAkun(request.getAkun());

        if (request.getTanggal() != null) {
            t.setTanggal(request.getTanggal());
        } else {
            t.setTanggal(LocalDateTime.now());
        }

        transactionRepository.save(t);
        
        return "Pemasukan berhasil dicatat, saldo Anda bertambah!";
    }

    public Transaction updateIncome(Long id, IncomeRequest request, String userId) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data pemasukan tidak ditemukan"));

        if (!t.getUserId().equals(userId)) {
            throw new SecurityException("Akses ditolak: Anda tidak berhak mengubah data ini");
        }

        // 💡 PROTEKSI IMMUTABLE: Cek batas waktu 24 Jam
        if (t.getTanggal().isBefore(LocalDateTime.now().minusHours(24))) {
            throw new IllegalStateException("Transaksi ini sudah dikunci permanen (Lebih dari 24 jam) dan tidak bisa diubah.");
        }

        if (request.getNominal() == null || request.getNominal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Nominal pemasukan harus lebih besar dari 0");
        }

        categoryRepository.findByNameTypeAndUserOrSystem(request.getKategori(), "INCOME", userId)
        .orElseThrow(() -> new IllegalArgumentException("Kategori '" + request.getKategori() + "' tidak valid untuk pemasukan"));
        
        t.setNominal(request.getNominal());
        t.setKategori(request.getKategori());
        t.setKeterangan(request.getKeterangan());
        t.setAkun(request.getAkun());

        if (request.getTanggal() != null) {
            t.setTanggal(request.getTanggal());
        } else {
            t.setTanggal(LocalDateTime.now());
        }

        return transactionRepository.save(t);
    }

    public void deleteIncome(Long id, String userId) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Data pemasukan tidak ditemukan"));

        if (!t.getUserId().equals(userId)) {
            throw new SecurityException("Akses ditolak: Anda tidak berhak menghapus data ini");
        }

        // 💡 PROTEKSI IMMUTABLE: Cek batas waktu 24 Jam
        if (t.getTanggal().isBefore(LocalDateTime.now().minusHours(24))) {
            throw new IllegalStateException("Transaksi ini sudah dikunci permanen (Lebih dari 24 jam) dan tidak bisa dihapus.");
        }

        transactionRepository.delete(t);
    }
}