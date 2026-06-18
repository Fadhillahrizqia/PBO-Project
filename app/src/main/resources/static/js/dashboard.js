/**
 * File: dashboard.js
 * Tim Frontend (Mahasiswa 9/10/12)
 * Fungsi: Mengelola UI Dashboard, merender Chart.js, dan memuat data API.
 */

document.addEventListener('DOMContentLoaded', () => {
    setupActionButtons();
    fetchDashboardData();
});

// Konfigurasi Global Chart.js agar sesuai desain font
Chart.defaults.font.family = 'Inter, sans-serif';
Chart.defaults.color = '#707975'; 

function setupActionButtons() {
    const buttons = document.querySelectorAll('button');
    buttons.forEach(btn => {
        const text = btn.innerText.trim();
        if (text.includes('Tambah Pemasukan')) {
            btn.addEventListener('click', () => window.location.href = 'pemasukan.html');
        } else if (text.includes('Tambah Pengeluaran')) {
            btn.addEventListener('click', () => window.location.href = 'pengeluaran.html');
        } else if (text.includes('Lihat Semua')) {
            btn.addEventListener('click', () => window.location.href = 'laporan.html');
        }
    });
}

function fetchDashboardData() {
    /* * MOCK DATA JSON
     * Struktur ini mengantisipasi DTO buatan Mahasiswa 8 (DashboardResponseDTO, dll)
     */
    const mockApiResponse = {
        summary: {
            totalSaldo: 4500000,
            pemasukanBulanIni: 2000000,
            pengeluaranBulanIni: 850000,
            saldoAkun: {
                bca: 2500000,
                mandiri: 1200000,
                gopay: 350000,
                dana: 300000,
                cash: 150000
            }
        },
        // Sesuai CategoryBreakdownDTO
        categoryBreakdown: {
            labels: ['Makanan', 'Transport', 'Belajar', 'Lainnya'],
            data: [40, 25, 15, 20] // Persentase
        },
        // Sesuai MonthlyChartDTO
        monthlyTrend: {
            labels: ['Sep', 'Okt', 'Nov'],
            pemasukan: [1500000, 1800000, 2000000],
            pengeluaran: [1200000, 900000, 850000]
        }
    };

    // Jalankan fungsi update UI setelah data didapat
    updateCards(mockApiResponse.summary);
    renderDonutChart(mockApiResponse.categoryBreakdown);
    renderBarChart(mockApiResponse.monthlyTrend);
}

function updateCards(summaryData) {
    const formatRp = (angka) => {
        return new Intl.NumberFormat('id-ID', {
            style: 'currency', currency: 'IDR', maximumFractionDigits: 0
        }).format(angka);
    };

    // Update Angka Besar
    document.getElementById('totalSaldo').innerText = formatRp(summaryData.totalSaldo);
    document.getElementById('pemasukanBulanIni').innerText = formatRp(summaryData.pemasukanBulanIni);
    document.getElementById('pengeluaranBulanIni').innerText = formatRp(summaryData.pengeluaranBulanIni);

    // Update Saldo per Akun
    document.getElementById('saldoBca').innerText = formatRp(summaryData.saldoAkun.bca);
    document.getElementById('saldoMandiri').innerText = formatRp(summaryData.saldoAkun.mandiri);
    document.getElementById('saldoGopay').innerText = formatRp(summaryData.saldoAkun.gopay);
    document.getElementById('saldoDana').innerText = formatRp(summaryData.saldoAkun.dana);
    document.getElementById('saldoCash').innerText = formatRp(summaryData.saldoAkun.cash);
}

function renderDonutChart(categoryData) {
    const ctx = document.getElementById('kategoriChart').getContext('2d');
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: categoryData.labels,
            datasets: [{
                data: categoryData.data,
                backgroundColor: [
                    '#b7e7f7', // secondary-container (Transport/Biru)
                    '#d7defa', // tertiary-container (Makan/Ungu)
                    '#fef3c7', // Kuning (Belajar)
                    '#e3e2e1'  // surface-variant (Lainnya/Abu)
                ],
                borderWidth: 0,
                hoverOffset: 5
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '75%', // Ketebalan donat
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        usePointStyle: true,
                        padding: 20,
                        font: { size: 12 }
                    }
                },
                tooltip: {
                    callbacks: {
                        label: (context) => ` ${context.label}: ${context.parsed}%`
                    }
                }
            }
        }
    });
}

function renderBarChart(trendData) {
    const ctx = document.getElementById('trenChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: trendData.labels,
            datasets: [
                {
                    label: 'Pemasukan',
                    data: trendData.pemasukan,
                    backgroundColor: '#b5ead7', // primary-container (Hijau)
                    borderRadius: 4,
                    barPercentage: 0.6
                },
                {
                    label: 'Pengeluaran',
                    data: trendData.pengeluaran,
                    backgroundColor: '#ffdad6', // error-container (Merah muda)
                    borderRadius: 4,
                    barPercentage: 0.6
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        usePointStyle: true,
                        padding: 20,
                        font: { size: 12 }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: '#f4f3f2', drawBorder: false }, // surface-container-low
                    ticks: {
                        callback: (value) => 'Rp ' + (value / 1000000) + ' Jt'
                    }
                },
                x: {
                    grid: { display: false, drawBorder: false }
                }
            }
        }
    });
}