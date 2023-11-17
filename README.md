# EatsNow SOAP Service



## Deskripsi

Eatsnow soap service merupakan backend secara keseluruhan yang mengimplementasikan SOAP protocol. Aplikasi ini dibuat dengan JAX-WS sebagai library dari Java.

## Skema Basis Data 

Basis data terdiri dari tabel logging dan review. tabel logging berguna untuk melihat log dari setiap service yang dilakukan oleh eatsnow-soap-service. tabel review menyimpan review pengguna terhadap restoran. 

## Daftar endpoint

Semua endpoint berada di /ws/review
- addReview berfungsi untuk menambahkan review
- getReview berfungsi mendapatkan review berdasarkan restoran
- getUserReview berfungsi mendapatkan review berdasarkan user
- updateReview berfungsi untuk mengubah review yang telah dibuat

## Pembagian Kerja

- (13521018) Syarifa Dwi Purnamasari [updateReview]
- (13521023) Kenny Benaya Nathan [GetReview, GetUserReview]
- (13521027) Agsha Athalla Nurkareem [setup, addReview]