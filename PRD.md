# PRD — TransportBookingQA
**Product Requirements Document: Pengujian Aplikasi Mobile & Penjaminan Mutu (QA)**

| Atribut | Keterangan |
|---|---|
| Nama Proyek | TransportBookingQA |
| Package | `com.industri.transportqa` |
| Mata Kuliah | Pemrograman Mobile Dasar — Pertemuan 13 |
| Dosen Pengampu | Rosidin, S.Kom., M.Kom. |
| Program Studi | Sistem Informasi, STMIK IKMI Cirebon |
| Platform / Bahasa | Android Studio, Kotlin |
| Metode | Hands-on Lab (Case-Based Learning) |
| Sub-CPMK | 13 — Menyusun dan mengeksekusi rangkaian pengujian perangkat lunak mobile (Unit Test JVM, MockWebServer, In-Memory Room DB, Espresso UI) berpedoman pada piramida testing |
| Status | Draft siap eksekusi |

---

## 1. Ringkasan Eksekutif

TransportBookingQA adalah proyek latihan berbasis studi kasus **sistem reservasi tiket maskapai & kereta cepat**. Fokus utama proyek **bukan** pada fitur aplikasi, melainkan pada **rangkaian pengujian otomatis (Automated Testing Suite)** yang menjamin mutu aplikasi sesuai **Piramida Pengujian (Testing Pyramid)**:

| Lapisan | Proporsi Ideal | Kecepatan | Fokus | Direktori |
|---|---|---|---|---|
| Unit Test (JVM) | 70% | Sangat cepat (milidetik) | Logika bisnis, kalkulator, DTO, transformasi | `src/test/java/` |
| Integration Test | 20% | Menengah (detik) | Room DB, MockWebServer API, DAO | `src/androidTest/java/` atau `src/test/java/` |
| UI / End-to-End | 10% | Lambat (menit) | Alur interaksi visual pengguna, navigasi | `src/androidTest/java/` |

Mahasiswa menghasilkan: kode logika bisnis, kumpulan test (unit, jaringan, database, UI), dua tugas mandiri tambahan, serta bukti eksekusi (screenshot / video output).

---

## 2. Latar Belakang & Masalah

- Pengujian manual (klik satu per satu) lambat, rawan human error, dan tidak skalabel.
- Tanpa otomasi, **regresi** (bug lama muncul kembali setelah kode baru ditambahkan) sulit dicegah.
- Pengujian yang bergantung pada internet atau data sungguhan bersifat tidak deterministik (hasil bisa berubah-ubah).

**Solusi:** membangun suite pengujian otomatis yang cepat, deterministik, dan dapat dijalankan berulang kali.

---

## 3. Tujuan & Indikator Keberhasilan

### 3.1 Tujuan (Learning Objectives)
1. Memahami taksonomi pengujian mobile berdasarkan Testing Pyramid.
2. Membedakan pengujian lokal `src/test` (JVM murni, tanpa emulator) dengan pengujian instrumen `src/androidTest`.
3. Menulis unit test logika bisnis menggunakan **JUnit 4** dan **Google Truth**.
4. Menguji integrasi API & database secara deterministik dengan **MockWebServer** dan **In-Memory Room Database**.
5. Membangun otomasi UI dengan **AndroidX Espresso**.

### 3.2 Indikator Keberhasilan (KPI)
| KPI | Target |
|---|---|
| Seluruh test pada Tahap 1 & 2 | 100% Passed (centang hijau) |
| Tugas Mandiri 1 (2 unit test edge case) | Selesai & Passed |
| Tugas Mandiri 2 (1 Espresso negative flow) | Selesai & Passed |
| Waktu eksekusi unit test JVM | ≤ 5 detik |
| Flaky test | 0 (animasi emulator dimatikan) |
| Bukti output | Link/video/screenshot terlampir |

---

## 4. Ruang Lingkup

### 4.1 In Scope
- Pembuatan proyek Android `TransportBookingQA`.
- Kelas logika bisnis `FareCalculator`.
- Unit test JVM (`FareCalculatorTest`).
- Integration test jaringan (`MockWebServerTest`).
- Integration test database (`TicketDaoTest`, in-memory).
- UI test Espresso (`BookingActivityEspressoTest`).
- Dua tugas mandiri (edge case & negative flow).
- Dokumentasi hasil (screenshot/video output).

### 4.2 Out of Scope
- Pemesanan tiket sungguhan ke server produksi.
- Integrasi pembayaran, autentikasi, dan backend nyata.
- CI/CD pipeline dan code coverage tooling (opsional pengembangan lanjutan).
- Desain UI tingkat lanjut (cukup layout fungsional).

---

## 5. Kebutuhan Sistem & Lingkungan

| Kebutuhan | Spesifikasi |
|---|---|
| Perangkat | Laptop/PC RAM min. 8 GB (disarankan 16 GB) |
| IDE | Android Studio (versi Rabbit atau lebih baru) |
| JDK | JDK 17 atau OpenJDK bawaan Android Studio |
| Emulator | Android API 24+, **Window/Transition/Animator animation scale = Off** |
| Bahasa | Kotlin |

---

## 6. Arsitektur & Struktur Proyek

```
TransportBookingQA/
└── app/
    ├── build.gradle.kts
    └── src/
        ├── main/java/com/industri/transportqa/
        │   ├── FareCalculator.kt
        │   └── MainActivity.kt              (+ layout activity_main.xml)
        ├── test/java/com/industri/transportqa/        ← JVM lokal
        │   ├── FareCalculatorTest.kt
        │   └── MockWebServerTest.kt
        └── androidTest/java/com/industri/transportqa/ ← Instrumented
            ├── TicketDaoTest.kt
            └── BookingActivityEspressoTest.kt
```

---

## 7. Kebutuhan Fungsional (Functional Requirements)

### FR-01 — Setup Proyek & Dependensi
- Buat proyek baru **TransportBookingQA** dengan package `com.industri.transportqa`.
- Tambahkan dependensi pada `app/build.gradle.kts`:

| Konfigurasi | Dependensi |
|---|---|
| `implementation` | `androidx.room:room-runtime:2.6.1`, `androidx.room:room-ktx:2.6.1` |
| `testImplementation` | `junit:junit:4.13.2`, `com.google.truth:truth:1.4.2`, `com.squareup.okhttp3:mockwebserver:4.12.0`, `org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0` |
| `androidTestImplementation` | `androidx.test.ext:junit:1.2.1`, `androidx.test.espresso:espresso-core:3.6.1`, `androidx.test:runner:1.6.1`, `androidx.test:rules:1.6.1`, `com.google.truth:truth:1.4.2` |

> ⚠️ **Catatan penting (tambahan dari Senior Review):** modul tidak mencantumkan beberapa dependensi yang dibutuhkan kode contoh. Lihat bagian **13. Catatan Teknis & Koreksi Modul**.

### FR-02 — Kelas Logika Bisnis `FareCalculator`
| ID | Fungsi | Aturan Bisnis |
|---|---|---|
| FR-02.1 | `isValidNik(nik: String): Boolean` | Valid jika panjang **tepat 16 karakter** dan **semuanya digit** |
| FR-02.2 | `calculateTotalFare(baseFare: Double, passengerAge: Int, baggageWeightKg: Int): Double` | Lihat aturan di bawah |

**Aturan `calculateTotalFare`:**
1. `baseFare < 0` → lempar `IllegalArgumentException("Tarif dasar tidak boleh minus")`.
2. `passengerAge < 0` → lempar `IllegalArgumentException("Usia tidak valid")`.
3. **Diskon usia:** usia < 3 tahun → diskon 100%; usia ≥ 60 tahun → diskon 20%; selain itu 0%.
4. **Bagasi:** gratis hingga 20 kg; kelebihan dikenakan **Rp 25.000/kg**.
5. Hasil = `baseFare × (1 − diskon) + (kelebihan bagasi × 25.000)`.

### FR-03 — Unit Test JVM (`FareCalculatorTest`) — folder `src/test`
| ID | Skenario | Ekspektasi |
|---|---|---|
| UT-01 | NIK 16 digit angka (`3209123456780001`) | `true` |
| UT-02 | NIK kurang dari 16 digit (`32091234`) | `false` |
| UT-03 | NIK mengandung huruf (`320912345678ABCD`) | `false` |
| UT-04 | Lansia 65 th, tarif Rp 500.000, bagasi 15 kg | Rp 400.000 |
| UT-05 | Usia 30 th, tarif Rp 500.000, bagasi 25 kg | Rp 625.000 (denda 5 kg × 25.000) |

Wajib menggunakan `@Before` untuk inisialisasi, `@Test`, dan asersi Google Truth (`assertThat`).

### FR-04 — Integration Test Jaringan (`MockWebServerTest`) — folder `src/test`
- Definisikan `FlightApiService` (Retrofit) dengan endpoint `GET api/v1/flights/availabilities` bertipe `suspend fun checkAvailability(): List<String>`.
- `@Before`: start `MockWebServer`, bangun Retrofit dengan `baseUrl(mockWebServer.url("/"))` + `GsonConverterFactory`.
- `@After`: `mockWebServer.shutdown()` (**wajib**, mencegah *port already in use*).
- Skenario **NT-01**: respons HTTP 200 berisi `["GA-102 (CGK - SUB)", "GA-204 (CGK - DPS)"]` → hasil berukuran 2 dan elemen pertama mengandung `"CGK - SUB"`.

### FR-05 — Integration Test Database (`TicketDaoTest`) — folder `src/androidTest`
- Entity `TicketEntity(ticketCode [PK], passengerName, destination)` pada tabel `tickets`.
- DAO `TicketDao`: `insertTicket()` dan `getTicketByCode()`.
- Database `TestAppDatabase` (version 1, `exportSchema = false`).
- Gunakan `Room.inMemoryDatabaseBuilder(...)` + `allowMainThreadQueries()`.
- `@After`: `db.close()` (**wajib**, mencegah memory leak).
- Skenario **DB-01**: insert `TKT-8801 / Budi Santoso / Surabaya Gubeng`, lalu ambil kembali → tidak null dan `passengerName == "Budi Santoso"`.

### FR-06 — UI Test Espresso (`BookingActivityEspressoTest`) — folder `src/androidTest`
- Menggunakan `ActivityScenarioRule(MainActivity::class.java)`.
- Pola baku: `onView(matcher).perform(action).check(assertion)`.
- Skenario **UI-01 (happy path):**
  1. Ketik NIK `3209123456780001` pada `R.id.etNik`.
  2. Ketik nama `Ahmad Dahlan` pada `R.id.etName`.
  3. Klik `R.id.btnSubmitBooking`.
  4. Verifikasi `R.id.tvBookingStatus` tampil dengan teks **"Pemesanan Tiket Berhasil Diverifikasi"**.

### FR-07 — Tugas Mandiri 1 (Edge Case Unit Testing)
Tambahkan **2 skenario** pada `FareCalculatorTest`:
| ID | Skenario | Ekspektasi |
|---|---|---|
| UT-06 | `baseFare = -10000.0` | Melempar `IllegalArgumentException` |
| UT-07 | Balita usia 2 tahun (tanpa kelebihan bagasi) | Total = **Rp 0,-** (diskon 100%) |

### FR-08 — Tugas Mandiri 2 (Negative Flow UI Automation)
Tambahkan **1 skenario Espresso**:
| ID | Skenario | Ekspektasi |
|---|---|---|
| UI-02 | Kolom NIK dikosongkan, langsung klik tombol submit | Pesan error **"NIK tidak boleh kosong"** tampil di layar dan **transaksi tidak diproses** (`tvBookingStatus` tidak menampilkan teks sukses) |

### FR-09 — Aplikasi Pendukung (`MainActivity`) *(syarat agar UI test dapat berjalan)*
Layout wajib memiliki view dengan ID berikut:

| View ID | Tipe | Fungsi |
|---|---|---|
| `etNik` | `EditText` (inputType number) | Input NIK |
| `etName` | `EditText` | Input nama penumpang |
| `btnSubmitBooking` | `Button` | Submit pemesanan |
| `tvBookingStatus` | `TextView` | Status konfirmasi |

Perilaku:
- NIK kosong → tampilkan error "NIK tidak boleh kosong" (mis. `etNik.error` atau Toast/TextView), **jangan** tampilkan status sukses.
- NIK tidak valid menurut `FareCalculator.isValidNik()` → tampilkan pesan error, tidak diproses.
- Input valid → `tvBookingStatus` menampilkan "Pemesanan Tiket Berhasil Diverifikasi".

---

## 8. Kebutuhan Non-Fungsional

| ID | Kebutuhan |
|---|---|
| NFR-01 | **Cepat:** unit test JVM selesai ≤ 5 detik tanpa emulator. |
| NFR-02 | **Deterministik:** test tidak bergantung pada internet / data nyata (gunakan MockWebServer & in-memory DB). |
| NFR-03 | **Terisolasi:** tiap test bebas residu (server di-shutdown, DB di-close). |
| NFR-04 | **Stabil (non-flaky):** animasi emulator dimatikan saat Espresso. |
| NFR-05 | **Terbaca:** nama test deskriptif; pola Espresso konsisten `onView → perform → check`. |
| NFR-06 | **Proporsi piramida:** mayoritas test berupa unit test (target ideal 70/20/10). |
| NFR-07 | **Kompatibilitas:** minSdk API 24+, JDK 17. |

---

## 9. Strategi Pengujian

```
          /\            UI / E2E (Espresso)       ~10%  lambat
         /  \
        /----\          Integration (Room, MockWebServer) ~20%  menengah
       /      \
      /--------\        Unit Test JVM (JUnit + Truth)  ~70%  sangat cepat
```

| Jenis | Tool | Lokasi | Perlu Emulator? |
|---|---|---|---|
| Unit | JUnit 4, Truth | `src/test` | Tidak |
| Network Integration | MockWebServer, Retrofit, coroutines | `src/test` | Tidak |
| Database Integration | Room in-memory, AndroidJUnit4 | `src/androidTest` | **Ya** |
| UI Automation | Espresso, ActivityScenarioRule | `src/androidTest` | **Ya** |

**Cara menjalankan:**
- Klik kanan berkas test di Project panel → **Run '<NamaTest>'**.
- Semua baris pada bilah status harus bertanda **centang hijau (Passed)**.

---

## 10. Rencana Pelaksanaan (Milestone)

| Fase | Aktivitas | Output |
|---|---|---|
| 1. Setup | Buat proyek, atur dependensi, sync Gradle, matikan animasi emulator | Proyek build sukses |
| 2. Logika Bisnis | Implementasi `FareCalculator.kt` | Kelas siap diuji |
| 3. Unit Test | `FareCalculatorTest` (5 test dasar) | 5/5 Passed |
| 4. Network Test | `MockWebServerTest` | 1/1 Passed |
| 5. DB Test | `TicketDaoTest` | 1/1 Passed |
| 6. UI Layer | Buat `MainActivity` + layout sesuai FR-09 | UI siap diuji |
| 7. UI Test | `BookingActivityEspressoTest` (happy path) | 1/1 Passed |
| 8. Tugas Mandiri | Tugas 1 (2 unit test) & Tugas 2 (1 Espresso) | 3 test tambahan Passed |
| 9. Dokumentasi | Screenshot/rekaman output, unggah ke Drive, isi link | Bukti output |

---

## 11. Kriteria Penerimaan (Definition of Done)

- [ ] Proyek `TransportBookingQA` ber-package `com.industri.transportqa` dan dapat di-build.
- [ ] `FareCalculator.kt` sesuai aturan bisnis FR-02.
- [ ] `FareCalculatorTest` berisi 5 test dasar + 2 test edge case; seluruhnya **Passed**.
- [ ] `MockWebServerTest` **Passed** dan `stopServer()` ada di `@After`.
- [ ] `TicketDaoTest` **Passed** menggunakan in-memory DB dan `db.close()` di `@After`.
- [ ] `BookingActivityEspressoTest` berisi 1 happy path + 1 negative flow; seluruhnya **Passed**.
- [ ] Animasi emulator (Window, Transition, Animator) = **Off**.
- [ ] Bukti output (screenshot hasil run / video, tautan Google Drive) dilampirkan.
- [ ] Kode rapi, nama test deskriptif, tanpa test yang bergantung pada internet.

---

## 12. Risiko & Mitigasi (Troubleshooting)

| # | Risiko / Error | Penyebab | Mitigasi |
|---|---|---|---|
| 1 | `RuntimeException: Method ... not mocked` | Mengakses kelas framework Android (mis. `TextUtils`, `Log`) di test JVM | Gunakan kelas Kotlin murni atau pasang Robolectric |
| 2 | Espresso flaky / `NoMatchingViewException` | Animasi transisi aktif | Developer Options → set 3 skala animasi ke **Animation off** |
| 3 | MockWebServer *Port Already in Use* | Server tidak dimatikan | Pastikan `shutdown()` di `@After` |
| 4 | In-memory DB leak | Lupa menutup DB | Selalu `db.close()` di `@After` |
| 5 | Build gagal karena dependensi hilang | Retrofit/Gson/KSP belum ditambahkan | Lihat bagian 13 |
| 6 | Test Room/Espresso tidak jalan | Emulator/perangkat tidak aktif | Nyalakan emulator API 24+ sebelum menjalankan `androidTest` |

---

## 13. Catatan Teknis & Koreksi Modul (Senior Review)

Setelah menelaah modul secara teliti, ditemukan beberapa hal yang jika disalin mentah-mentah akan menyebabkan **error kompilasi atau test gagal**. Berikut perbaikannya.

### 13.1 Dependensi yang kurang
Kode `MockWebServerTest` memakai Retrofit & Gson, dan `TicketDaoTest` memakai anotasi Room, namun modul tidak mencantumkannya. Tambahkan:

```kotlin
// Retrofit + Gson (dibutuhkan MockWebServerTest di src/test)
testImplementation("com.squareup.retrofit2:retrofit:2.11.0")
testImplementation("com.squareup.retrofit2:converter-gson:2.11.0")

// Annotation processor Room (KSP) — wajib agar @Dao/@Database ter-generate
// plugins { id("com.google.devtools.ksp") version "<sesuaikan versi Kotlin>" }
ksp("androidx.room:room-compiler:2.6.1")
kspAndroidTest("androidx.room:room-compiler:2.6.1")
```
> Catatan: `ksp("...")` untuk source set main, `kspAndroidTest("...")` karena Room Entity/DAO/Database pada modul didefinisikan di `src/androidTest`. Sesuaikan versi KSP dengan versi Kotlin pada proyek Anda.

### 13.2 Literal JSON pada MockWebServerTest tidak valid
Pada PDF tertulis:
```kotlin
val mockJson = "["GA-102 (CGK - SUB)", "GA-204 (CGK - DPS)"]"
```
Ini **error kompilasi** karena tanda kutip tidak di-escape. Gunakan *raw string*:
```kotlin
val mockJson = """["GA-102 (CGK - SUB)", "GA-204 (CGK - DPS)"]"""
```

### 13.3 Format kode pada PDF terpotong
Beberapa baris menyatu akibat konversi PDF (mis. `... minus") if (passengerAge < 0)` dan `val discountRate = when {`). Pisahkan menjadi baris tersendiri saat mengetik.

### 13.4 Nama kelas vs ActivityRule
Komentar menyebut `BookingActivity`, tetapi kode memakai `MainActivity::class.java`. Pastikan kelas yang dipakai pada rule **benar-benar ada**.

### 13.5 Penempatan tipe MockWebServer
`FlightApiService` dan `MockWebServerTest` berada di `src/test`; pastikan coroutines (`runBlocking`) tersedia (sudah tercakup `kotlinx-coroutines-test`/stdlib).

---

## 14. Rancangan Kode Referensi

### 14.1 `FareCalculator.kt`
```kotlin
package com.industri.transportqa

class FareCalculator {

    // Validasi NIK: wajib 16 karakter angka
    fun isValidNik(nik: String): Boolean {
        if (nik.length != 16) return false
        return nik.all { it.isDigit() }
    }

    fun calculateTotalFare(
        baseFare: Double,
        passengerAge: Int,
        baggageWeightKg: Int
    ): Double {
        if (baseFare < 0) throw IllegalArgumentException("Tarif dasar tidak boleh minus")
        if (passengerAge < 0) throw IllegalArgumentException("Usia tidak valid")

        // 1. Diskon usia
        val discountRate = when {
            passengerAge < 3 -> 1.0
            passengerAge >= 60 -> 0.20
            else -> 0.0
        }
        val fareAfterDiscount = baseFare * (1.0 - discountRate)

        // 2. Kelebihan bagasi: gratis 20 kg, selebihnya Rp 25.000/kg
        val excessBaggageKg = if (baggageWeightKg > 20) baggageWeightKg - 20 else 0
        val baggageFee = excessBaggageKg * 25000.0

        return fareAfterDiscount + baggageFee
    }
}
```

### 14.2 `FareCalculatorTest.kt` (termasuk Tugas 1)
```kotlin
package com.industri.transportqa

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class FareCalculatorTest {

    private lateinit var calculator: FareCalculator

    @Before
    fun setUp() {
        calculator = FareCalculator()
    }

    @Test
    fun `isValidNik dengan 16 digit angka harus bernilai true`() {
        assertThat(calculator.isValidNik("3209123456780001")).isTrue()
    }

    @Test
    fun `isValidNik dengan kurang dari 16 digit harus bernilai false`() {
        assertThat(calculator.isValidNik("32091234")).isFalse()
    }

    @Test
    fun `isValidNik mengandung huruf harus bernilai false`() {
        assertThat(calculator.isValidNik("320912345678ABCD")).isFalse()
    }

    @Test
    fun `calculateTotalFare untuk penumpang lansia umur 65 tahun mendapat diskon 20 persen`() {
        val total = calculator.calculateTotalFare(500000.0, passengerAge = 65, baggageWeightKg = 15)
        assertThat(total).isEqualTo(400000.0)
    }

    @Test
    fun `calculateTotalFare dengan kelebihan bagasi 25 kg dikenakan denda bagasi`() {
        val total = calculator.calculateTotalFare(500000.0, passengerAge = 30, baggageWeightKg = 25)
        assertThat(total).isEqualTo(625000.0)
    }

    // ===== TUGAS 1: EDGE CASE =====

    @Test
    fun `calculateTotalFare dengan tarif dasar negatif harus melempar IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            calculator.calculateTotalFare(-10000.0, passengerAge = 30, baggageWeightKg = 10)
        }
    }

    @Test
    fun `calculateTotalFare untuk balita usia 2 tahun harus bernilai nol`() {
        val total = calculator.calculateTotalFare(500000.0, passengerAge = 2, baggageWeightKg = 0)
        assertThat(total).isEqualTo(0.0)
    }
}
```
> **Penting:** import Truth yang benar adalah `com.google.common.truth.Truth.assertThat` (bukan `com.google.truth.Truth`, seperti tertulis di modul — paket tersebut tidak ada).

### 14.3 Layout & `MainActivity` pendukung (untuk Espresso)
```kotlin
class MainActivity : AppCompatActivity() {
    private val calculator = FareCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNik = findViewById<EditText>(R.id.etNik)
        val etName = findViewById<EditText>(R.id.etName)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitBooking)
        val tvStatus = findViewById<TextView>(R.id.tvBookingStatus)

        btnSubmit.setOnClickListener {
            val nik = etNik.text.toString()
            when {
                nik.isEmpty() -> {
                    etNik.error = "NIK tidak boleh kosong"
                    tvStatus.text = ""
                }
                !calculator.isValidNik(nik) -> {
                    etNik.error = "NIK harus 16 digit angka"
                    tvStatus.text = ""
                }
                else -> {
                    tvStatus.text = "Pemesanan Tiket Berhasil Diverifikasi"
                }
            }
        }
    }
}
```

### 14.4 Tugas 2 — Negative Flow Espresso
```kotlin
@Test
fun nikKosong_laluSubmit_harusMenampilkanErrorDanTidakDiproses() {
    // 1. Biarkan NIK kosong, langsung tekan submit
    onView(withId(R.id.btnSubmitBooking)).perform(click())

    // 2. Verifikasi pesan error muncul pada kolom NIK
    onView(withId(R.id.etNik))
        .check(matches(hasErrorText("NIK tidak boleh kosong")))

    // 3. Verifikasi transaksi tidak diproses (status sukses tidak tampil)
    onView(withId(R.id.tvBookingStatus))
        .check(matches(not(withText("Pemesanan Tiket Berhasil Diverifikasi"))))
}
```
Import tambahan: `androidx.test.espresso.matcher.ViewMatchers.hasErrorText` dan `org.hamcrest.CoreMatchers.not`.
> Jika error ditampilkan lewat `TextView`/Snackbar/Toast, ganti asersi sesuai implementasi (mis. `onView(withText("NIK tidak boleh kosong")).check(matches(isDisplayed()))`).

---

## 15. Deliverables (Yang Dikumpulkan)

| # | Deliverable | Keterangan |
|---|---|---|
| 1 | Source code proyek | Folder `TransportBookingQA` (atau repositori) |
| 2 | Berkas test | `FareCalculatorTest.kt`, `MockWebServerTest.kt`, `TicketDaoTest.kt`, `BookingActivityEspressoTest.kt` |
| 3 | Bukti output | Screenshot/video hasil run (centang hijau) + tautan Google Drive |
| 4 | Dokumen PRD | Berkas `PRD.md` ini |

---

## 16. Daftar Cek Akhir Sebelum Kumpul

- [ ] Gradle sync sukses tanpa error.
- [ ] Emulator API 24+ menyala, animasi **Off**.
- [ ] Jalankan seluruh test di `src/test` → hijau.
- [ ] Jalankan seluruh test di `src/androidTest` → hijau.
- [ ] Screenshot panel hasil test disimpan.
- [ ] Tautan output diisi pada laporan.
- [ ] Nama & NIM mahasiswa dicantumkan pada laporan.
