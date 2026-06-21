@echo off
echo ========================================
echo  Farm Manager - Kompilasi & Jalankan
echo ========================================

if not exist "lib\mssql-jdbc.jar" (
    echo.
    echo [ERROR] File lib\mssql-jdbc.jar tidak ditemukan!
    echo Download dari: https://learn.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server
    echo Rename file .jar menjadi mssql-jdbc.jar dan taruh di folder lib\
    pause
    exit /b 1
)

echo [1/3] Membuat folder output...
if not exist "out" mkdir out

echo [2/3] Mengompilasi source code...
javac -cp "lib\mssql-jdbc.jar" -d out -sourcepath src src\com\farmmanager\db\DBConnection.java src\com\farmmanager\model\*.java src\com\farmmanager\dao\*.java src\com\farmmanager\ui\*.java

if %errorlevel% neq 0 (
    echo [ERROR] Kompilasi gagal! Periksa error di atas.
    pause
    exit /b 1
)

echo [3/3] Menjalankan aplikasi...
java -cp "out;lib\mssql-jdbc.jar" com.farmmanager.ui.MainFrame

pause