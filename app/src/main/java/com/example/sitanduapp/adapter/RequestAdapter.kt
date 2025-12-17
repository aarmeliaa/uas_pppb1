package com.example.sitanduapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sitanduapp.R
import com.example.sitanduapp.api.Request

class RequestAdapter(
    private var listRequest: List<Request>,
    private val onTinjauClick: (Request) -> Unit
) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tv_judul)
        val tvSubJudul: TextView = itemView.findViewById(R.id.tv_sub_judul)
        val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama)
        val tvNim: TextView = itemView.findViewById(R.id.tv_nim)
        val btnTinjau: Button = itemView.findViewById(R.id.btn_tinjau)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request_ttd, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listRequest[position]

        holder.tvJudul.text = item.judul
        holder.tvSubJudul.text = item.keterangan
        holder.tvTanggal.text = "Deadline: ${item.tanggal}"
        holder.tvNama.text = item.nama
        holder.tvNim.text = item.nim

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