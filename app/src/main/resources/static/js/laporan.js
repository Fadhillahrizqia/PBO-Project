/**
 * File: js/laporan.js
 * Engine Pengolah Jurnal Laporan Gabungan & Statistik Keuangan
 * 💡 FITUR: Filter Tahunan (Year-to-Date), Perbandingan multi-bar Jan-Des, Donut dengan Persen, dan Warna HSL High Contrast 🎨
 */
import {
  getRiwayatPemasukan,
  getRiwayatPengeluaran,
  requireAuth,
  logout,
} from "./api.js";

requireAuth();

const btnLogout = document.getElementById("btnLogout");
if (btnLogout) {
  btnLogout.addEventListener("click", (e) => {
    e.preventDefault();
    if (confirm("Apakah Anda yakin ingin keluar dari FinanceBuddy?")) {
      logout();
    }
  });
}

const btnCetak = document.getElementById("btnCetakLaporan");
if (btnCetak) {
  btnCetak.onclick = () => {
    window.print();
  };
}

const formatRupiah = (angka) => {
  return new Intl.NumberFormat("id-ID", {
    style: "currency",
    currency: "IDR",
    maximumFractionDigits: 0,
  }).format(angka || 0);
};

// 💡 PENYELARASAN WARNA DINAMIS HSL
const mapWarnaKategori = {
  BONUS: { bg: "#059669", text: "#ffffff" },
  UANG_SAKU: { bg: "#0d9488", text: "#ffffff" },
  GAJI_PART_TIME: { bg: "#0284c7", text: "#ffffff" },
  FREELANCE: { bg: "#4f46e5", text: "#ffffff" },
  MAKANAN: { bg: "#e11d48", text: "#ffffff" },
  MAKAN: { bg: "#e11d48", text: "#ffffff" },
  TRANSPORT: { bg: "#ea580c", text: "#ffffff" },
  BELAJAR: { bg: "#2563eb", text: "#ffffff" },
  KOST: { bg: "#475569", text: "#ffffff" },
  HIBURAN: { bg: "#d97706", text: "#ffffff" },
  TAGIHAN: { bg: "#dc2626", text: "#ffffff" },
};

function ambilGayaWarnaKategori(namaKat) {
  const key = (namaKat || "LAINNYA").toUpperCase().trim().replace(/\s+/g, "_");

  if (mapWarnaKategori[key]) return mapWarnaKategori[key];

  let hash = 0;
  for (let i = 0; i < key.length; i++) {
    hash = key.charCodeAt(i) + ((hash << 5) - hash);
  }

  // 💡 FIX: Kalikan dengan 137.5 (Golden Angle) agar warna selalu melompat jauh!
  const hue = Math.floor(Math.abs(hash) * 137.5) % 360;

  return { bg: `hsl(${hue}, 75%, 45%)`, text: "#ffffff" };
}

async function initHalamanLaporan() {
  try {
    const dataPemasukan = (await getRiwayatPemasukan()) || [];
    const dataPengeluaran = (await getRiwayatPengeluaran()) || [];

    let totalIn = 0;
    let totalOut = 0;

    // Wadah penampung akumulasi 12 bulan (Jan - Des)
    const arrayBulananIn = Array(12).fill(0);
    const arrayBulananOut = Array(12).fill(0);
    const alokasiKategori = {};

    const tahunSekarang = new Date().getFullYear(); // Ambil Tahun Ini

    // 💡 1. Proses data Pemasukan (Filter Hanya Tahun Berjalan)
    dataPemasukan.forEach((item) => {
      const nominal = item.nominal || 0;
      const itemTahun = item.tanggal
        ? new Date(item.tanggal).getFullYear()
        : tahunSekarang;

      if (itemTahun === tahunSekarang) {
        totalIn += nominal;
        if (item.tanggal) {
          const bulanIdx = new Date(item.tanggal).getMonth(); // 0 = Jan, 11 = Des
          if (bulanIdx >= 0 && bulanIdx < 12) {
            arrayBulananIn[bulanIdx] += nominal;
          }
        }
      }
    });

    // 💡 2. Proses data Pengeluaran (Filter Hanya Tahun Berjalan)
    dataPengeluaran.forEach((item) => {
      const nominal = item.nominal || 0;
      const itemTahun = item.tanggal
        ? new Date(item.tanggal).getFullYear()
        : tahunSekarang;

      if (itemTahun === tahunSekarang) {
        totalOut += nominal;
        if (item.tanggal) {
          const bulanIdx = new Date(item.tanggal).getMonth();
          if (bulanIdx >= 0 && bulanIdx < 12) {
            arrayBulananOut[bulanIdx] += nominal;
          }
        }
        const kat = item.kategori
          ? item.kategori.trim().toUpperCase()
          : "LAINNYA";
        alokasiKategori[kat] = (alokasiKategori[kat] || 0) + nominal;
      }
    });

    // Suntik data angka ke kartu atas
    document.getElementById("txtTotalLapIn").innerText = formatRupiah(totalIn);
    document.getElementById("txtTotalLapOut").innerText =
      formatRupiah(totalOut);

    const selisihBersih = totalIn - totalOut;
    document.getElementById("txtTotalLapNet").innerText =
      formatRupiah(selisihBersih);

    const elBadge = document.getElementById("lblNetBadge");
    if (elBadge) {
      if (selisihBersih >= 0) {
        elBadge.innerText = "↑ Surplus";
        elBadge.className =
          "inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wide bg-emerald-50 text-emerald-700";
      } else {
        elBadge.innerText = "↓ Defisit";
        elBadge.className =
          "inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wide bg-rose-50 text-rose-700";
      }
    }

    // Set nama tahun otomatis berdasarkan tanggal perangkat (Tahun Ini)
    document.getElementById("lblTahunPemasukan").innerText =
      `Tahun ${tahunSekarang}`;
    document.getElementById("lblTahunPengeluaran").innerText =
      `Tahun ${tahunSekarang}`;

    // ====== HITUNG KELUARAN PANEL RINGKASAN STATISTIK ======
    const namaBulanIndo = [
      "Januari",
      "Februari",
      "Maret",
      "April",
      "Mei",
      "Juni",
      "Juli",
      "Agustus",
      "September",
      "Oktober",
      "November",
      "Desember",
    ];

    // 1. Cari bulan pengeluaran tertinggi
    let maxBulanIdx = 0;
    let maxBulanVal = 0;
    arrayBulananOut.forEach((val, idx) => {
      if (val > maxBulanVal) {
        maxBulanVal = val;
        maxBulanIdx = idx;
      }
    });
    document.getElementById("lblStatBulanMax").innerText =
      maxBulanVal > 0 ? namaBulanIndo[maxBulanIdx] : "-";

    // 2. Cari kategori pengeluaran terbesar
    let maxKategoriNama = "-";
    let maxKategoriVal = 0;
    for (const [key, value] of Object.entries(alokasiKategori)) {
      if (value > maxKategoriVal) {
        maxKategoriVal = value;
        maxKategoriNama = key;
      }
    }
    document.getElementById("lblStatKategoriMax").innerText = maxKategoriNama;

    // 3. Hitung rata-rata pemasukan bulanan riil (total dibagi 12 bulan)
    const rataRataPemasukan = Math.round(totalIn / 12);
    document.getElementById("lblStatAvgIn").innerText =
      formatRupiah(rataRataPemasukan);

    // Render grafik visualisasi
    renderGrafikKomparasiBulanan(arrayBulananIn, arrayBulananOut);
    renderGrafikLingkaranDistribusi(alokasiKategori);
  } catch (error) {
    console.error("Gagal memuat modul laporan tahunan:", error);
  }
}

function renderGrafikKomparasiBulanan(dataMasuk, dataKeluar) {
  const ctx = document
    .getElementById("chartPerbandinganBulanan")
    .getContext("2d");
  new Chart(ctx, {
    type: "bar",
    data: {
      labels: [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "Mei",
        "Jun",
        "Jul",
        "Ags",
        "Sep",
        "Okt",
        "Nov",
        "Des",
      ],
      datasets: [
        {
          label: "Pemasukan",
          data: dataMasuk,
          backgroundColor: "#059669", // Hijau Emerald Pekat
          borderRadius: 4,
        },
        {
          label: "Pengeluaran",
          data: dataKeluar,
          backgroundColor: "#e11d48", // Merah Rose Pekat
          borderRadius: 4,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true,
          ticks: { font: { family: "Inter", size: 10 } },
        },
        x: { ticks: { font: { family: "Inter", size: 10 } } },
      },
      plugins: {
        legend: {
          position: "top",
          align: "end",
          labels: {
            usePointStyle: true,
            pointStyle: "rectRounded",
            boxWidth: 8,
            boxHeight: 8,
            font: { family: "Inter", size: 11 },
          },
        },
      },
    },
  });
}

function renderGrafikLingkaranDistribusi(objekKategori) {
  const ctx = document.getElementById("chartStrukturGabungan").getContext("2d");

  const labelsArray = Object.keys(objekKategori);
  const dataArray = Object.values(objekKategori);
  const warnaArray = labelsArray.map((lbl) => ambilGayaWarnaKategori(lbl).bg);
  const totalDana = dataArray.reduce((sum, v) => sum + v, 0);

  const labelsWithPercent = labelsArray.map((lbl, idx) => {
    const persen =
      totalDana > 0 ? Math.round((dataArray[idx] / totalDana) * 100) : 0;
    return `${lbl.toUpperCase()} (${persen}%)`;
  });

  new Chart(ctx, {
    type: "doughnut",
    data: {
      labels:
        labelsWithPercent.length > 0 ? labelsWithPercent : ["Belum ada data"],
      datasets: [
        {
          data: dataArray.length > 0 ? dataArray : [1],
          backgroundColor: warnaArray.length > 0 ? warnaArray : ["#e5e7eb"],
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: "bottom", // Legenda ditaruh di bawah manis sesuai mockup distribusi tahunan figma
          labels: {
            usePointStyle: true,
            pointStyle: "rectRounded",
            boxWidth: 8,
            boxHeight: 8,
            padding: 14,
            font: { size: 10, family: "Inter" },
          },
        },
      },
    },
  });
}

document.addEventListener("DOMContentLoaded", initHalamanLaporan);
