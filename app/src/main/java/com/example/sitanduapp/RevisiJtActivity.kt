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

class RevisiJtActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revisi_jt)

        val dataRequest = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("EXTRA_REQUEST", Request::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_REQUEST")
        }

        if (dataRequest != null) {
            findViewById<TextView>(R.id.tv_judul_revisi_jt).text = dataRequest.judul
            findViewById<TextView>(R.id.tv_sub_judul_revisi_jt).text = dataRequest.keterangan
            findViewById<TextView>(R.id.tv_nama_revisi_jt).text = dataRequest.nama
            findViewById<TextView>(R.id.tv_nim_revisi_jt).text = dataRequest.nim
            findViewById<TextView>(R.id.tv_tanggal_revisi_jt).text = dataRequest.tanggal
            findViewById<TextView>(R.id.tv_jam_revisi_jt).text = dataRequest.waktu ?: "-"
        }

        val etCatatan: EditText = findViewById(R.id.et_catatan_jt)
        val btnKirim: MaterialButton = findViewById(R.id.btn_kirim_jt)
        val btnBatal: MaterialButton = findViewById(R.id.btn_batal_jt)
        val btnBack: ImageView = findViewById(R.id.btn_back)

        btnBack.setOnClickListener { finish() }
        btnBatal.setOnClickListener { finish() }

        btnKirim.setOnClickListener {
            val catatan = etCatatan.text.toString().trim()

            if (catatan.isEmpty()) {
                etCatatan.error = "Mohon isi alasan jadwal ulang"
                Toast.makeText(this, "Catatan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {

                val historyBaru = HistoryEntity(
                    nama = dataRequest?.nama ?: "Mahasiswa",
                    judul = dataRequest?.judul ?: "Janji Temu",
                    tipe = "jt",
                    tanggal = dataRequest?.tanggal ?: "-",
                    status = "Reschedule: $catatan"
                )

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@RevisiJtActivity)
                    db.historyDao().insert(historyBaru)

                    Toast.makeText(this@RevisiJtActivity, "Jadwal Ulang Terkirim & Disimpan!", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@RevisiJtActivity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}