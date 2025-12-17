package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // IMPORT WAJIB 1
import kotlinx.coroutines.launch     // IMPORT WAJIB 2
import com.example.sitanduapp.api.Request
import com.example.sitanduapp.database.AppDatabase  // IMPORT WAJIB 3
import com.example.sitanduapp.database.HistoryEntity // IMPORT WAJIB 4
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

        btnKirim.setOnClickListener {
            val alasan = etAlasan.text.toString().trim()

            if (alasan.isEmpty()) {
                etAlasan.error = "Alasan revisi wajib diisi!"
                Toast.makeText(this, "Mohon isi alasan revisi", Toast.LENGTH_SHORT).show()
            } else {

                val historyBaru = HistoryEntity(
                    nama = dataRequest?.nama ?: "Mahasiswa",
                    judul = dataRequest?.judul ?: "Dokumen TTD",
                    tipe = "ttd",
                    tanggal = dataRequest?.tanggal ?: "-",
                    status = "Revisi: $alasan"
                )

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@RevisiTtdActivity)
                    db.historyDao().insert(historyBaru)

                    Toast.makeText(this@RevisiTtdActivity, "Revisi terkirim & Disimpan Offline!", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@RevisiTtdActivity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}