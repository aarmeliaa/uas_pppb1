package com.example.sitanduapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import java.io.File
import java.io.FileOutputStream

class SetujuiTtdActivity : AppCompatActivity() {

    private val REQUEST_CODE_PICK_FILE = 1001
    private val REQUEST_CODE_STORAGE_PERMISSION = 1002
    private var selectedFile: File? = null

    companion object {
        private const val TAG = "SetujuiTtdActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setujui_ttd)

        val btnBack: ImageView = findViewById(R.id.btn_back)
        val btnBatal: MaterialButton = findViewById(R.id.btn_batal_setujui_ttd)
        val btnKirim: MaterialButton = findViewById(R.id.btn_kirim_setujui_ttd)
        val btnUnduh: MaterialButton = findViewById(R.id.btn_unduh_dokumen_setujui)
        val btnUpload: MaterialButton = findViewById(R.id.btn_upload_dokumen_setujui)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnBatal.setOnClickListener {
            finish()
        }

        btnKirim.setOnClickListener {
            if (selectedFile != null) {
                uploadAndNavigateToDashboard()
            } else {
                Toast.makeText(this, "Harap upload dokumen terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        btnUnduh.setOnClickListener {
            unduhDokumen()
        }

        btnUpload.setOnClickListener {
            if (checkStoragePermission()) {
                pilihFile()
            } else {
                requestStoragePermission()
            }
        }
    }

    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_STORAGE_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pilihFile()
            } else {
                Toast.makeText(
                    this,
                    "Permission diperlukan untuk memilih file",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun unduhDokumen() {
        Toast.makeText(this, "Mengunduh dokumen...", Toast.LENGTH_SHORT).show()
        // TODO: Implementasi unduh dari server
        Toast.makeText(this, "Dokumen berhasil diunduh", Toast.LENGTH_SHORT).show()
    }

    private fun pilihFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf" // Hanya file PDF
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            startActivityForResult(
                Intent.createChooser(intent, "Pilih Dokumen PDF"),
                REQUEST_CODE_PICK_FILE
            )
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "Instal file manager untuk memilih file", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                handleSelectedPDF(uri)
            }
        }
    }

    private fun handleSelectedPDF(uri: Uri) {
        try {
            val fileName = getFileName(uri)

            // Validasi ekstensi file
            if (!fileName.endsWith(".pdf", ignoreCase = true)) {
                Toast.makeText(this, "Hanya file PDF yang diperbolehkan", Toast.LENGTH_SHORT).show()
                return
            }

            // Simpan file ke cache
            selectedFile = saveFileToCache(uri, fileName)

            if (selectedFile != null && selectedFile!!.exists()) {
                // Update UI
                val btnUpload: MaterialButton = findViewById(R.id.btn_upload_dokumen_setujui)
                val displayName = if (fileName.length > 15) {
                    "${fileName.substring(0, 12)}...pdf"
                } else {
                    fileName
                }
                btnUpload.text = "✓ $displayName"

                Toast.makeText(this, "PDF siap diunggah: $fileName", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "File PDF berhasil dipilih: ${selectedFile!!.absolutePath}")
            } else {
                Toast.makeText(this, "Gagal memproses file PDF", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error handling PDF: ${e.message}")
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "unknown.pdf"
    }

    private fun saveFileToCache(uri: Uri, fileName: String): File? {
        return try {
            // Buat direktori cache jika belum ada
            val cacheDir = File(cacheDir, "pdf_uploads")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            // Buat file di cache
            val cacheFile = File(cacheDir, "upload_${System.currentTimeMillis()}.pdf")

            // Copy isi file
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(cacheFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                }
            }

            cacheFile
        } catch (e: Exception) {
            Log.e(TAG, "Error saving file to cache: ${e.message}")
            null
        }
    }

    private fun uploadAndNavigateToDashboard() {
        selectedFile?.let { file ->
            // Tampilkan loading
            Toast.makeText(this, "Mengirim dokumen...", Toast.LENGTH_SHORT).show()

            // Disable tombol kirim sementara
            val btnKirim: MaterialButton = findViewById(R.id.btn_kirim_setujui_ttd)
            btnKirim.isEnabled = false
            btnKirim.text = "Mengirim..."

            // Simulasi proses upload
            Handler(Looper.getMainLooper()).postDelayed({
                // Setelah "upload" selesai, navigasi ke dashboard
                navigateToDashboard()
            }, 1500) // Simulasi 1.5 detik

        } ?: run {
            Toast.makeText(this, "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDashboard() {
        // Buat intent untuk DashboardActivity
        val intent = Intent(this, DashboardActivity::class.java).apply {
            // Hapus semua activity di stack dan buat dashboard sebagai root baru
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            // Opsional: Tambahkan extra data jika diperlukan
            putExtra("show_success_message", true)
            putExtra("message", "Dokumen berhasil dikirim!")
        }

        // Navigasi ke dashboard
        startActivity(intent)
        finish() // Tutup activity ini
    }

    private fun cleanupCache() {
        try {
            val cacheDir = File(cacheDir, "pdf_uploads")
            if (cacheDir.exists() && cacheDir.isDirectory) {
                cacheDir.listFiles()?.forEach { file ->
                    file.delete()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning cache: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Bersihkan cache saat activity dihancurkan
        cleanupCache()
    }
}