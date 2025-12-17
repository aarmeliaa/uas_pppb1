package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sitanduapp.api.Request
import com.google.android.material.button.MaterialButton

class DetailTtdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ttd)

        val dataRequest = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("EXTRA_REQUEST", Request::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_REQUEST")
        }

        if (dataRequest != null) {
            findViewById<TextView>(R.id.tv_judul_detail).text = dataRequest.judul
            findViewById<TextView>(R.id.tv_sub_judul_detail).text = dataRequest.keterangan
            findViewById<TextView>(R.id.tv_nama_mhs_detail).text = dataRequest.nama
            findViewById<TextView>(R.id.tv_nim_mhs_detail).text = dataRequest.nim
        }

        val btnBack: ImageView = findViewById(R.id.btn_back)
        val btnSetuju: MaterialButton = findViewById(R.id.btn_setuju)
        val btnRevisi: MaterialButton = findViewById(R.id.btn_jadwal_ulang)

        btnBack.setOnClickListener { finish() }

        btnSetuju.setOnClickListener {
            val intent = Intent(this, SetujuiTtdActivity::class.java)

            if (dataRequest != null) {
                intent.putExtra("EXTRA_REQUEST", dataRequest)
            }

            startActivity(intent)
        }

        btnRevisi.setOnClickListener {
            val intent = Intent(this, RevisiTtdActivity::class.java)

            if (dataRequest != null) {
                intent.putExtra("EXTRA_REQUEST", dataRequest)
            }

            startActivity(intent)
        }
    }
}