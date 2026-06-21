==============================================
 FARM MANAGER
 Aplikasi CRUD Java Swing untuk Peternakan Ayam
==============================================

Farm Manager adalah aplikasi desktop sederhana untuk mengelola data
peternakan ayam dan panen telur. Aplikasi ini dibuat dengan Java Swing
dan menggunakan SQL Server sebagai database.


==============================================
 FITUR UTAMA
==============================================

1. Data Farm
   - Tambah, edit, hapus, dan cari data farm
   - Menyimpan nama farm, lokasi, dan alamat

2. Data Kandang
   - Tambah, edit, hapus, dan cari data kandang
   - Kandang terhubung ke farm
   - Tipe kandang menggunakan pilihan seperti Baterai dan Lantai

3. Data Karyawan
   - Tambah karyawan
   - Menampilkan nama, nomor telepon, alamat, tanggal mulai, status,
     peran, dan farm
   - Nonaktifkan karyawan tanpa menghapus riwayat data
   - Hapus karyawan dengan validasi agar data panen tetap aman

4. Data Batch Ayam
   - Tambah, edit, hapus, dan cari batch ayam
   - Batch terhubung ke kandang
   - Menyimpan tanggal masuk, jumlah awal, jumlah hidup, dan status batch

5. Data Panen Telur
   - Catat dan hapus panen telur
   - Pilih batch/kandang dari dropdown
   - Pilih karyawan dari dropdown
   - Pilih grade telur dari dropdown
   - Menampilkan total butir dan total berat panen


==============================================
 STRUKTUR FOLDER
==============================================

FarmManager/
  src/
    com/farmmanager/
      db/       -> Koneksi database
      model/    -> Class data/model
      dao/      -> Query dan proses CRUD database
      ui/       -> Tampilan Java Swing

  lib/          -> Tempat file mssql-jdbc.jar
  out/          -> Hasil compile, dibuat otomatis
  compile.bat   -> Compile dan jalankan aplikasi di Windows
  compile.sh    -> Compile dan jalankan aplikasi di Linux/Mac
  README.txt    -> Dokumentasi proyek


==============================================
 KEBUTUHAN
==============================================

1. Java JDK
   Disarankan JDK 8 ke atas.

2. SQL Server
   Database yang digunakan adalah SQL Server.

3. JDBC Driver SQL Server
   File driver harus bernama:

   mssql-jdbc.jar

   Simpan file tersebut di folder:

   lib/


==============================================
 SETUP JDBC DRIVER
==============================================

1. Download Microsoft JDBC Driver for SQL Server dari:

   https://learn.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server

2. Extract file hasil download.

3. Ambil file .jar yang sesuai dengan versi Java yang dipakai.

4. Rename file tersebut menjadi:

   mssql-jdbc.jar

5. Letakkan di folder:

   lib/


==============================================
 SETUP DATABASE
==============================================

Pastikan database SQL Server sudah tersedia dan memiliki tabel yang
dibutuhkan aplikasi, seperti:

- FARM
- KANDANG
- KARYAWAN
- BATCH_AYAM
- PANEN_TELUR
- DETAIL_PANEN_TELUR
- GRADE_TELUR

Kemudian buka file:

src/com/farmmanager/db/DBConnection.java

Sesuaikan konfigurasi berikut dengan SQL Server yang digunakan:

SERVER   = "LocalHostKalian"
PORT     = "1433"
DATABASE = "NamaDBKalian"
USER     = "NamaUserKalian"
PASSWORD = "PasswordKalian"

==============================================
 CARA MENJALANKAN
==============================================

Windows:

1. Pastikan file mssql-jdbc.jar sudah ada di folder lib/
2. Double click file:

   compile.bat

Linux/Mac:

1. Buka terminal di folder FarmManager
2. Jalankan:

   bash compile.sh


==============================================
 CATATAN PENGGUNAAN
==============================================

- Input tanggal menggunakan format:

  yyyy-MM-dd

  Contoh:

  2026-06-21

- Saat mencatat panen telur, user tidak perlu menghafal ID karyawan
  atau ID grade telur karena sudah tersedia pilihan dropdown.

- Karyawan yang masih memiliki data panen sebaiknya dinonaktifkan dulu
  sebelum dihapus permanen.

- Jika tombol hapus gagal karena relasi database, kemungkinan data masih
  dipakai oleh tabel lain.


==============================================
 TROUBLESHOOTING
==============================================

1. Error: mssql-jdbc.jar tidak ditemukan
   Solusi:
   Pastikan file mssql-jdbc.jar sudah ada di folder lib/

2. Error koneksi SQL Server
   Solusi:
   Periksa SERVER, PORT, DATABASE, USER, dan PASSWORD di DBConnection.java

3. Error CHECK constraint
   Solusi:
   Pastikan pilihan di aplikasi sesuai aturan database.
   Contoh: tipe kandang harus sesuai nilai yang diterima database.

4. Error foreign key saat hapus data
   Solusi:
   Data tersebut masih dipakai oleh tabel lain. Hapus data terkait atau
   gunakan fitur nonaktif jika tersedia.


==============================================
 PEMBUATAN
==============================================

Aplikasi ini dibuat sebagai proyek CRUD Java Swing dengan database SQL Server
untuk membantu pengelolaan data peternakan ayam dan panen telur.

==============================================
ANGGOTA
==============================================
1. 255150200111010	ALIF ALBANI SIAGIAN
2. 255150200111018	BAGAS AJI RASENDRIA
3. 255150201111010	AHMAD ZAKI YASYKUR PANDIA
4. 255150207111033	RINDU ALISA
5. 255150207111034	PHAKSI GIRING PAMUNGKAS

