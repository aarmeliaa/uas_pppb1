package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class DetailTtdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ttd)

        val btnBack: ImageView = findViewById(R.id.btn_back)
        val btnSetuju: MaterialButton = findViewById(R.id.btn_setuju)
        val btnRevisi: MaterialButton = findViewById(R.id.btn_jadwal_ulang)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSetuju.setOnClickListener {
            // Navigasi ke halaman Setujui
            val intent = Intent(this, SetujuiTtdActivity::class.java)
            startActivity(intent)
        }

        btnRevisi.setOnClickListener {
            // Navigasi ke halaman Revisi
            val intent = Intent(this, RevisiTtdActivity::class.java)
            startActivity(intent)
        }
    }
}