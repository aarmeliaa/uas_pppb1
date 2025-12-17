package com.example.sitanduapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sitanduapp.R
import com.example.sitanduapp.database.HistoryEntity

class JadwalAdapter(private var listJadwal: List<HistoryEntity>) :
    RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(R.id.tv_judul_jadwal)
        val tvNama: TextView = view.findViewById(R.id.tv_nama_jadwal)
        val tvTanggal: TextView = view.findViewById(R.id.tv_tanggal_jadwal)
        val tvLokasi: TextView = view.findViewById(R.id.tv_lokasi_jadwal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listJadwal[position]

        holder.tvJudul.text = item.judul
        holder.tvNama.text = item.nama
        holder.tvTanggal.text = item.tanggal
        holder.tvLokasi.text = item.status
    }

    override fun getItemCount() = listJadwal.size

    fun updateData(newList: List<HistoryEntity>) {
        listJadwal = newList
        notifyDataSetChanged()
    }
}