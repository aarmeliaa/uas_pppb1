package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class RevisiTtdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.revisi_ttd)

        val btnBack: ImageView = findViewById(R.id.btn_back)
        val btnBatal: MaterialButton = findViewById(R.id.btn_batal_revisi_ttd)
        val btnKirim: MaterialButton = findViewById(R.id.btn_kirim_revisi_ttd)
        val btnUnduh: MaterialButton = findViewById(R.id.btn_unduh_dokumen_revisi)
        val etAlasan: EditText = findViewById(R.id.et_alasan_revisi)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnBatal.setOnClickListener {
            finish()
        }

        btnKirim.setOnClickListener {
            val alasan = etAlasan.text.toString().trim()

            if (alasan.isEmpty()) {
                Toast.makeText(this, "Harap isi alasan revisi", Toast.LENGTH_SHORT).show()
                etAlasan.requestFocus()
            } else {
                // Disable tombol kirim sementara
                btnKirim.isEnabled = false
                btnKirim.text = "Mengirim..."

                // Tampilkan loading
                Toast.makeText(this, "Mengirim revisi...", Toast.LENGTH_SHORT).show()

                // Simulasi proses pengiriman
                Handler(Looper.getMainLooper()).postDelayed({
                    // Setelah "pengiriman" selesai, navigasi ke dashboard
                    navigateToDashboard(alasan)
                }, 1500) // Simulasi 1.5 detik
            }
        }

        btnUnduh.setOnClickListener {
            Toast.makeText(this, "Mengunduh dokumen...", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Dokumen berhasil diunduh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDashboard(alasan: String) {
        // Buat intent untuk DashboardActivity
        val intent = Intent(this, DashboardActivity::class.java).apply {
            // Hapus semua activity di stack dan buat dashboard sebagai root baru
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            // Opsional: Tambahkan extra data jika diperlukan
            putExtra("show_success_message", true)
            putExtra("message", "Revisi berhasil dikirim!")
            putExtra("alasan_revisi", alasan)
        }

        // Navigasi ke dashboard
        startActivity(intent)
        finish() // Tutup activity ini
    }
}