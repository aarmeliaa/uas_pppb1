package com.example.sitanduapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sitanduapp.R
import com.example.sitanduapp.api.Request

class JtAdapter(
    private var listRequest: List<Request>,
    private val onTinjauClick: (Request) -> Unit
) : RecyclerView.Adapter<JtAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tv_judul_jt)
        val tvSubJudul: TextView = itemView.findViewById(R.id.tv_sub_judul_jt)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_jt)
        val tvWaktu: TextView = itemView.findViewById(R.id.tv_waktu_jt)
        val btnTinjau: Button = itemView.findViewById(R.id.btn_tinjau_jt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request_jt, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listRequest[position]

        holder.tvJudul.text = item.judul
        holder.tvSubJudul.text = item.keterangan
        holder.tvNama.text = item.nama

        val jam = item.waktu ?: "-"
        holder.tvWaktu.text = "${item.tanggal} • $jam"

        holder.btnTinjau.setOnClickListener {
            onTinjauClick(item)
        }
    }

    override fun getItemCount() = listRequest.size

    fun updateData(newList: List<Request>) {
        listRequest = newList
        notifyDataSetChanged()
    }
}