# Farm Manager (Penugasan Mata Kuliah Basis Data)

> **Aplikasi Desktop CRUD Java Swing untuk Pengelolaan Peternakan Ayam dan Panen Telur Terintegrasi Database SQL Server.**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-007396?style=for-the-badge)
![SQL Server](https://img.shields.io/badge/Database-Microsoft%20SQL%20Server-CC292B?style=for-the-badge&logo=microsoftsqlserver&logoColor=white)
![JDBC](https://img.shields.io/badge/Driver-MSSQL%20JDBC-blue?style=for-the-badge)

---

## Tentang Proyek

**Farm Manager** adalah aplikasi desktop berbasis **Java Swing** yang dirancang untuk mempermudah operasional dan pembukuan data pada peternakan ayam petelur. Aplikasi ini mengintegrasikan seluruh alur bisnis peternakan—mulai dari manajemen lokasi farm, kandang, karyawan, batch ayam masuk, hingga pencatatan panen telur harian secara real-time ke database **Microsoft SQL Server**.

Dibangun dengan arsitektur **DAO (Data Access Object)** dan Model terstruktur untuk memisahkan logika database dari antarmuka pengguna (GUI).

---

## Fitur Utama

### 1. Manajemen Data Farm
* Tambah, edit, hapus, dan cari data lokasi farm.
* Menyimpan informasi detail seperti nama farm, wilayah, dan alamat lengkap.

### 2. Manajemen Kandang
* Pengelolaan data kandang yang terhubung langsung (*relational foreign key*) ke masing-masing farm.
* Mendukung pemilihan tipe kandang (misalnya: *Baterai* dan *Lantai*).
* Fitur pencarian dan filter cepat kandang.

### 3. Manajemen Karyawan
* Pencatatan profil karyawan: nama, kontak telepon, alamat, tanggal mulai bekerja, peran/jabatan, dan penugasan farm.
* **Fitur Nonaktifkan Karyawan**: Menonaktifkan status karyawan tanpa menghapus riwayat data transaksi yang sudah pernah dicatat.
* **Validasi Relasi**: Mencegah penghapusan karyawan yang masih terkait dengan data panen aktif.

### 4. Manajemen Batch Ayam
* Catat siklus masuk ayam per kandang.
* Melacak tanggal masuk, jumlah bibit awal, jumlah ayam hidup saat ini, dan status produktivitas batch.

### 5. Pencatatan & Monitoring Panen Telur
* Pencatatan panen telur harian yang komprehensif.
* Dropdown dinamis untuk pemilihan:
  * Batch ayam / kandang
  * Petugas karyawan yang memanen
  * Grade kualitas telur
* Kalkulasi otomatis total butir dan akumulasi total berat panen.

---

## Struktur Proyek

Aplikasi ini menerapkan pola pemisahan tanggung jawab (*Separation of Concerns*):

```text
FarmManager/
├── src/
│   └── com/farmmanager/
│       ├── db/          # Konfigurasi & koneksi JDBC SQL Server
│       ├── model/       # Data class (POJO / Entitas)
│       ├── dao/         # Query SQL & operasi CRUD database
│       └── ui/          # Komponen antarmuka GUI (Java Swing)
├── lib/                 # Tempat library eksternal (driver mssql-jdbc.jar)
├── compile.bat          # Script otomasi build & run untuk Windows
├── compile.sh           # Script otomasi build & run untuk Linux/macOS
└── README.md            # Dokumentasi proyek
```

---

## Persyaratan Sistem

* **Java Development Kit (JDK)**: JDK 8 atau versi lebih tinggi (JDK 17 / 21 LTS direkomendasikan).
* **Database**: Microsoft SQL Server (2014, 2017, 2019, 2022, atau SQL Server Express).
* **Driver JDBC**: Microsoft JDBC Driver for SQL Server (`mssql-jdbc.jar`).

---

## Panduan Instalasi & Pengaturan

### 1. Persiapan JDBC Driver

1. Unduh **Microsoft JDBC Driver for SQL Server** melalui link resmi:
   [Download Microsoft JDBC Driver for SQL Server](https://learn.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server)
2. Ekstrak file arsip yang telah diunduh.
3. Ambil file `.jar` yang sesuai dengan versi Java Anda (misal `mssql-jdbc-xx.x.x.jre11.jar` atau sejenisnya).
4. Ubah nama file (*rename*) menjadi:
   ```text
   mssql-jdbc.jar
   ```
5. Pindahkan file tersebut ke dalam folder:
   ```text
   lib/
   ```

---

### 2. Konfigurasi Database SQL Server

1. Pastikan server SQL Server Anda aktif dan TCP/IP (port `1433`) telah diaktifkan di SQL Server Configuration Manager.
2. Siapkan database dengan skema tabel yang dibutuhkan:
   * `FARM`
   * `KANDANG`
   * `KARYAWAN`
   * `BATCH_AYAM`
   * `PANEN_TELUR`
   * `DETAIL_PANEN_TELUR`
   * `GRADE_TELUR`
3. Buka file konfigurasi di:
   ```text
   src/com/farmmanager/db/DBConnection.java
   ```
4. Sesuaikan kredensial server dan database Anda:
   ```java
   SERVER   = "localhost";        // Host atau IP SQL Server Anda
   PORT     = "1433";             // Port standar SQL Server
   DATABASE = "NamaDatabase";     // Nama database Anda
   USER     = "sa";               // Username SQL Server
   PASSWORD = "PasswordAnda";     // Password SQL Server
   ```

---

## Cara Menjalankan Aplikasi

Aplikasi telah dilengkapi skrip otomatis untuk kompilasi dan eksekusi:

### Windows
1. Pastikan `lib/mssql-jdbc.jar` sudah berada di tempatnya.
2. Klik ganda pada file:
   ```text
   compile.bat
   ```
   *(Atau jalankan `.\compile.bat` melalui Command Prompt / PowerShell).*

### Linux / macOS
1. Buka terminal di direktori proyek `FarmManager`.
2. Berikan izin eksekusi jika diperlukan:
   ```bash
   chmod +x compile.sh
   ```
3. Jalankan skrip:
   ```bash
   ./compile.sh
   ```

---

## Catatan Penggunaan

* **Format Tanggal**: Masukkan tanggal dengan standar format ISO:
  ```text
  yyyy-MM-dd (Contoh: 2026-06-21)
  ```
* **Dropdown Selection**: Pengguna tidak perlu menghafal ID relasi; cukup pilih nama karyawan atau nama kandang dari menu dropdown.
* **Integritas Relasi**: Karyawan yang telah tercatat pada transaksi panen tidak dapat langsung dihapus demi menjaga integritas data (*foreign key*). Gunakan fitur **Nonaktifkan** terlebih dahulu.

---

## Troubleshooting & How to Fix

| Kendala | Penyebab Umum | Solusi |
| :--- | :--- | :--- |
| **`mssql-jdbc.jar` tidak ditemukan** | Driver belum ditaruh di folder `lib/` atau nama file salah | Pastikan file driver berada tepat di folder `lib/` dan dinamai persis `mssql-jdbc.jar`. |
| **Connection Refused / Error Koneksi** | SQL Server mati, port salah, atau kredensial keliru | Cek status SQL Server Service, pastikan TCP/IP aktif di port `1433`, dan verifikasi kredensial di `DBConnection.java`. |
| **Error `CHECK constraint`** | Nilai input tidak sesuai batasan kolom database | Pastikan tipe data sesuai (contoh: tipe kandang hanya menerima nilai yang valid pada enum/check constraint). |
| **Foreign Key Violation saat Delete** | Baris data sedang dijadikan referensi oleh tabel lain | Hapus data anak terlebih dahulu, atau gunakan opsi nonaktifkan status data terkait. |

---

## Contributors and Team
* 255150200111010	Alif Albani Siagian
* 255150200111018	Bagas Aji Rasendria
* 255150201111010	Ahmad Zaki Yasykur Pandia
* 255150207111033	Rindu Alisa
* 255150207111034	Phaksi Giring Pamungkas
