package com.example.sitanduapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sitanduapp.api.Request
import com.google.android.material.button.MaterialButton

class SetujuiTtdActivity : AppCompatActivity() {

    private lateinit var btnUpload: MaterialButton
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setujui_ttd)

        val dataRequest = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("EXTRA_REQUEST", Request::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_REQUEST")
        }

        if (dataRequest != null) {
            findViewById<TextView>(R.id.tv_judul_setujui).text = dataRequest.judul
            findViewById<TextView>(R.id.tv_sub_judul_setujui).text = dataRequest.keterangan
            findViewById<TextView>(R.id.tv_nama_setujui).text = dataRequest.nama
            findViewById<TextView>(R.id.tv_nim_setujui).text = dataRequest.nim
        }

        val btnBack: ImageView = findViewById(R.id.btn_back)
        val btnUnduh: MaterialButton = findViewById(R.id.btn_unduh_dokumen_setujui)
        btnUpload = findViewById(R.id.btn_upload_dokumen_setujui)
        val btnKirim: MaterialButton = findViewById(R.id.btn_kirim_setujui_ttd)
        val btnBatal: MaterialButton = findViewById(R.id.btn_batal_setujui_ttd)

        btnBack.setOnClickListener { finish() }
        btnBatal.setOnClickListener { finish() }

        // LOGIC UNDUH (palsu...)
        btnUnduh.setOnClickListener {
            Toast.makeText(this, "Mengunduh dokumen mahasiswa...", Toast.LENGTH_SHORT).show()
        }

        // LOGIC UPLOAD
        val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                fileUri = data?.data

                if (fileUri != null) {
                    val path = fileUri?.path ?: "Dokumen.pdf"
                    btnUpload.text = "File Terpilih: ${path.substringAfterLast("/")}"
                    btnUpload.setIconResource(R.drawable.dokumen)
                }
            }
        }

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            pickFileLauncher.launch(intent)
        }

        // LOGIC KIRIM
        btnKirim.setOnClickListener {
            if (fileUri == null) {
                Toast.makeText(this, "Harap unggah dokumen yang sudah ditandatangani!", Toast.LENGTH_SHORT).show()
            } else {
                // nanti akan diupdate logic integrasi dengan Room
                Toast.makeText(this, "Berhasil menyetujui dokumen!", Toast.LENGTH_LONG).show()

                val intent = Intent(this, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}