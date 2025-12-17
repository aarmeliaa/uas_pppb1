package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sitanduapp.api.Request
import com.google.android.material.button.MaterialButton

class DetailJtActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_jt)

        val dataRequest = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("EXTRA_REQUEST", Request::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_REQUEST")
        }

        if (dataRequest != null) {
            findViewById<TextView>(R.id.tv_judul_jt_detail).text = dataRequest.judul
            findViewById<TextView>(R.id.tv_sub_judul_jt_detail).text = dataRequest.keterangan
            findViewById<TextView>(R.id.tv_nama_mhs_jt).text = dataRequest.nama
            findViewById<TextView>(R.id.tv_nim_mhs_jt).text = dataRequest.nim
            findViewById<TextView>(R.id.tv_tanggal_jt).text = dataRequest.tanggal

            findViewById<TextView>(R.id.tv_jam_jt).text = dataRequest.waktu ?: "-"
        }

        val btnBack: ImageView = findViewById(R.id.btn_back)
        val layoutPilihRuangan: LinearLayout = findViewById(R.id.layout_pilih_ruangan)
        val spinnerRuangan: Spinner = findViewById(R.id.spinner_ruangan)

        val btnSetuju: MaterialButton = findViewById(R.id.btn_setuju)
        val btnJadwalUlang: MaterialButton = findViewById(R.id.btn_jadwal_ulang)
        val btnBatal: MaterialButton = findViewById(R.id.btn_batal) // Tombol batal pas milih ruangan
        val btnKonfirmasi: MaterialButton = findViewById(R.id.btn_konfirmasi)

        btnBack.setOnClickListener { finish() }

        val daftarRuangan = listOf("Ruang Dosen 1", "Ruang Dosen 2", "Lab Komputer 1", "Lab RPL", "Online (Zoom)")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, daftarRuangan)
        spinnerRuangan.adapter = adapterSpinner

        btnSetuju.setOnClickListener {
            btnSetuju.visibility = View.GONE
            btnJadwalUlang.visibility = View.GONE

            layoutPilihRuangan.visibility = View.VISIBLE
            btnBatal.visibility = View.VISIBLE
            btnKonfirmasi.visibility = View.VISIBLE
        }

        btnBatal.setOnClickListener {
            layoutPilihRuangan.visibility = View.GONE
            btnBatal.visibility = View.GONE
            btnKonfirmasi.visibility = View.GONE

            btnSetuju.visibility = View.VISIBLE
            btnJadwalUlang.visibility = View.VISIBLE
        }

        btnKonfirmasi.setOnClickListener {
            val ruanganTerpilih = spinnerRuangan.selectedItem.toString()

            Toast.makeText(this, "Janji temu disetujui di $ruanganTerpilih", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        btnJadwalUlang.setOnClickListener {
            val intent = Intent(this, RevisiJtActivity::class.java)

            if (dataRequest != null) {
                intent.putExtra("EXTRA_REQUEST", dataRequest)
            }

            startActivity(intent)
        }
    }
}