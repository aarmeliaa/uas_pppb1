package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sitanduapp.api.Request
import com.google.android.material.button.MaterialButton

class RevisiTtdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.revisi_ttd)

        val dataRequest = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("EXTRA_REQUEST", Request::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_REQUEST")
        }

        if (dataRequest != null) {
            findViewById<TextView>(R.id.tv_judul_revisi).text = dataRequest.judul
            findViewById<TextView>(R.id.tv_sub_judul_revisi).text = dataRequest.keterangan
            findViewById<TextView>(R.id.tv_nama_revisi).text = dataRequest.nama
            findViewById<TextView>(R.id.tv_nim_revisi).text = dataRequest.nim
        }

        val btnBack: ImageView = findViewById(R.id.btn_back)
        val btnUnduh: MaterialButton = findViewById(R.id.btn_unduh_dokumen_revisi)
        val etAlasan: EditText = findViewById(R.id.et_alasan_revisi)
        val btnKirim: MaterialButton = findViewById(R.id.btn_kirim_revisi_ttd)
        val btnBatal: MaterialButton = findViewById(R.id.btn_batal_revisi_ttd)

        btnBack.setOnClickListener { finish() }
        btnBatal.setOnClickListener { finish() }

        // LOGIC UNDUH (palsu...)
        btnUnduh.setOnClickListener {
            Toast.makeText(this, "Mengunduh dokumen untuk dicek...", Toast.LENGTH_SHORT).show()
        }

        // LOGIC KIRIM REVISI
        btnKirim.setOnClickListener {
            val alasan = etAlasan.text.toString().trim()

            if (alasan.isEmpty()) {
                etAlasan.error = "Alasan revisi wajib diisi!"
                Toast.makeText(this, "Mohon isi alasan revisi", Toast.LENGTH_SHORT).show()
            } else {
                // nanti akan diupdate logic integrasi dengan Room
                Toast.makeText(this, "Revisi berhasil dikirim ke mahasiswa!", Toast.LENGTH_LONG).show()

                val intent = Intent(this, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}