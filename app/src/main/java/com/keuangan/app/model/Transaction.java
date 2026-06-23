package com.keuangan.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false, length = 20)
    private String type; // "EXPENSE" atau "INCOME"

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 50)
    private String akun; // "Gopay", "Ovo", "BCA", dll.

    public Transaction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    // Perubahan nama Getter & Setter ke Bahasa Indonesia
    public String getKategori() { return category; }
    public void setKategori(String kategori) { this.category = kategori; }
    
    public BigDecimal getNominal() { return amount; }
    public void setNominal(BigDecimal nominal) { this.amount = nominal; }
    
    public String getKeterangan() { return description; }
    public void setKeterangan(String keterangan) { this.description = keterangan; }
    
    public LocalDate getTanggal() { return date; }
    public void setTanggal(LocalDate tanggal) { this.date = tanggal; }
    
    public String getAkun() { return akun; }
    public void setAkun(String akun) { this.akun = akun; }
}