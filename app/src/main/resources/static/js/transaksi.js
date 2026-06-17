/*
 * Fungsi: Menangani interaktivitas, validasi input form, dan konversi JSON untuk Transaksi.
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Deteksi halaman yang sedang aktif berdasarkan URL
    const pathName = window.location.pathname.toLowerCase();
    const isPemasukan = pathName.includes('pemasukan');
    const isPengeluaran = pathName.includes('pengeluaran');

    // 2. Tangkap elemen form 
    // (Asumsi: Anda akan membuat <form id="formTransaksi"> di dalam HTML modal/halaman)
    const formTransaksi = document.getElementById('formTransaksi');
    const inputNominal = document.getElementById('nominal');
    const inputDeskripsi = document.getElementById('deskripsi');
    const selectKategori = document.getElementById('kategori');
    const selectAkun = document.getElementById('akun');

    if (formTransaksi) {
        formTransaksi.addEventListener('submit', function (e) {
            // Mencegah halaman refresh otomatis saat form di-submit
            e.preventDefault(); 

            // 3. Ambil dan bersihkan nilai input
            // Menghapus karakter non-angka (seperti "Rp" atau titik) jika ada
            const rawNominal = inputNominal.value.replace(/[^0-9]/g, '');
            const nominal = parseInt(rawNominal, 10);
            
            const deskripsi = inputDeskripsi.value.trim();
            const kategori = selectKategori.value;
            const akun = selectAkun.value;

            // 4. Proses Validasi (Frontend Guard)
            if (isNaN(nominal) || nominal <= 0) {
                alert('Peringatan: Nominal harus berupa angka dan lebih dari 0.');
                return;
            }

            if (deskripsi === '') {
                alert('Peringatan: Deskripsi transaksi tidak boleh kosong.');
                return;
            }

            if (kategori === '') {
                alert('Peringatan: Silakan pilih kategori transaksi.');
                return;
            }

            if (akun === '') {
                alert('Peringatan: Silakan pilih akun keuangan (BCA, Mandiri, dll).');
                return;
            }

            // Validasi lanjutan: Pastikan kategori yang dipilih sesuai dengan halaman dan Enum Backend
            const validKategoriPemasukan = ['GAJI_PART_TIME', 'UANG_SAKU', 'FREELANCE', 'BONUS'];
            const validKategoriPengeluaran = ['MAKANAN', 'TRANSPORTASI', 'BELAJAR', 'HIBURAN'];

            if (isPemasukan && !validKategoriPemasukan.includes(kategori)) {
                alert('Sistem Error: Kategori Pemasukan tidak valid atau dimanipulasi.');
                return;
            }

            if (isPengeluaran && !validKategoriPengeluaran.includes(kategori)) {
                alert('Sistem Error: Kategori Pengeluaran tidak valid atau dimanipulasi.');
                return;
            }

            // 5. Susun Data menjadi format JSON (Sesuai dengan Request DTO Spring Boot)
            const payloadTransaksi = {
                tipe_transaksi: isPemasukan ? 'PEMASUKAN' : 'PENGELUARAN',
                nominal: nominal,
                deskripsi: deskripsi,
                kategori: kategori, // Sesuai Enum Java
                akun: akun          // Misal: "BCA", "GOPAY", "CASH"
            };

            // 6. Cetak ke Console untuk Testing
            console.log('🚀 Payload siap dikirim ke API :', JSON.stringify(payloadTransaksi, null, 2));
            alert('Validasi Berhasil! Silakan cek Console (F12) untuk melihat bentuk JSON.');

            //Kosongkan form kembali setelah sukses
            formTransaksi.reset();

            // CATATAN UNTUK INTEGRASI TIM:
            // Di sini Anda memanggil fungsi dari Mahasiswa 12 (api.js) untuk mengirim data ke Backend.
            // Contoh: 
            sendTransactionToAPI(payloadTransaksi);
        });
    } else {
        console.warn('⚠️ Elemen form dengan ID "formTransaksi" belum ditemukan di HTML ini.');
    }
});