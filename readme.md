# Sistem Penitipan Barang Gratis

## 📦 Deskripsi Proyek

Sistem penitipan barang sederhana dan gratis untuk membantu pengguna menitipkan barang mereka dengan aman dan mudah.

## ✨ Fitur Utama

- Batasan 3 barang per pelanggan
- Periode penitipan maksimal 30 hari
- Manajemen kategori barang
- Pelacakan status barang

## 🛠️ Teknologi yang Digunakan

- Java Spring Boot
- MySQL
- JPA/Hibernate
- Lombok

## 📋 Prasyarat

- Java 17+
- MySQL 8.0+
- Maven

## 🔍 Endpoint API

- Open API (Swagger)
- http://localhost:8080/api/v1/swagger-ui/index.html

### Pelanggan
- `POST /customer`: Buat pelanggan baru
- `GET /customer`: Dapatkan daftar pelanggan
- `PUT /customer/update/{id}`: Perbarui pelanggan
- `DELETE /customer/delete/{id}`: Hapus pelanggan

### Kategori
- `POST /category`: Buat kategori baru
- `GET /category`: Dapatkan daftar kategori
- `PUT /category/update/{id}`: Perbarui kategori
- `DELETE /category/delete/{id}`: Hapus kategori

### Barang
- `POST /items/create`: Titipkan barang baru
- `PUT /items/update/{id}`: Perbarui barang
- `GET /items/{customerId}`: Menampilkan data barang berdasarkan id customer
- `PUT /items/{id}/status`: Perbarui status penitipan
- `DELETE /items/delete/{id}`: Hapus barang

## 🔒 Batasan

- Maksimal 3 barang per pelanggan
- Periode penitipan maks. 30 hari
- Barang hanya dapat dihapus setelah diambil
