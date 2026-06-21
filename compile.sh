#!/bin/bash
echo "========================================"
echo " Farm Manager - Kompilasi & Jalankan"
echo "========================================"

if [ ! -f "lib/mssql-jdbc.jar" ]; then
    echo "[ERROR] File lib/mssql-jdbc.jar tidak ditemukan!"
    echo "Download dari: https://learn.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server"
    exit 1
fi

mkdir -p out

echo "[1/3] Mengompilasi..."
javac -cp "lib/mssql-jdbc.jar" -d out \
  src/com/farmmanager/db/DBConnection.java \
  src/com/farmmanager/model/*.java \
  src/com/farmmanager/dao/*.java \
  src/com/farmmanager/ui/*.java

if [ $? -ne 0 ]; then
  echo "[ERROR] Kompilasi gagal!"
  exit 1
fi

echo "[2/3] Menjalankan..."
java -cp "out:lib/mssql-jdbc.jar" com.farmmanager.ui.MainFrame
