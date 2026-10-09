package com.industri.transportqa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val calculator = FareCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNik = findViewById<EditText>(R.id.etNik)
        val etName = findViewById<EditText>(R.id.etName)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitBooking)
        val tvStatus = findViewById<TextView>(R.id.tvBookingStatus)

        btnSubmit.setOnClickListener {
            val nik = etNik.text.toString()
            val name = etName.text.toString()
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
                    etNik.error = null
                    tvStatus.text = "Pemesanan Tiket Berhasil Diverifikasi"
                }
            }
        }
    }
}
