package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class DetailJtActivity : AppCompatActivity() {

    private lateinit var spinnerRuangan: Spinner
    private lateinit var layoutPilihRuangan: View
    private lateinit var btnSetuju: MaterialButton
    private lateinit var btnJadwalUlang: MaterialButton
    private lateinit var btnBatal: MaterialButton
    private lateinit var btnKonfirmasi: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_jt)

        val btnBack: ImageView = findViewById(R.id.btn_back)
        spinnerRuangan = findViewById(R.id.spinner_ruangan)
        layoutPilihRuangan = findViewById(R.id.layout_pilih_ruangan)
        btnSetuju = findViewById(R.id.btn_setuju)
        btnJadwalUlang = findViewById(R.id.btn_jadwal_ulang)
        btnBatal = findViewById(R.id.btn_batal)
        btnKonfirmasi = findViewById(R.id.btn_konfirmasi)

        setupSpinner()
        setupButtonListeners()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupSpinner() {
        val ruanganList = arrayOf(
            "Pilih Ruangan",
            "Ruang Dosen 1",
            "Ruang Dosen 2",
            "Ruang Dosen 3",
            "Ruang Bimbingan 1",
            "Ruang Bimbingan 2"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ruanganList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRuangan.adapter = adapter
    }

    private fun setupButtonListeners() {
        btnSetuju.setOnClickListener {
            // Tampilkan pilihan ruangan
            layoutPilihRuangan.visibility = View.VISIBLE
            btnSetuju.visibility = View.GONE
            btnJadwalUlang.visibility = View.GONE
            btnBatal.visibility = View.VISIBLE
            btnKonfirmasi.visibility = View.VISIBLE
        }

        btnJadwalUlang.setOnClickListener {
            Toast.makeText(this, "Fitur jadwal ulang akan segera tersedia", Toast.LENGTH_SHORT).show()
            // TODO: Implementasi jadwal ulang
        }

        btnBatal.setOnClickListener {
            layoutPilihRuangan.visibility = View.GONE
            btnSetuju.visibility = View.VISIBLE
            btnJadwalUlang.visibility = View.VISIBLE
            btnBatal.visibility = View.GONE
            btnKonfirmasi.visibility = View.GONE
        }

        btnKonfirmasi.setOnClickListener {
            val selectedRuangan = spinnerRuangan.selectedItem.toString()
            if (selectedRuangan == "Pilih Ruangan") {
                Toast.makeText(this, "Silakan pilih ruangan terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Janji temu disetujui di $selectedRuangan",
                Toast.LENGTH_SHORT
            ).show()
            // TODO: Implementasi API konfirmasi
            finish()
        }
    }
}