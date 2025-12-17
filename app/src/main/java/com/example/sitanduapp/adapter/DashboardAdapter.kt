package com.example.sitanduapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sitanduapp.R
import com.example.sitanduapp.api.Request

class DashboardAdapter(
    private var listRequest: List<Request>,
    private val onClick: (Request) -> Unit
) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tv_judul_dash)
        val tvSubJudul: TextView = itemView.findViewById(R.id.tv_sub_judul_dash)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_dash)
        val tvNim: TextView = itemView.findViewById(R.id.tv_nim_dash)
        val imgIcon: ImageView = itemView.findViewById(R.id.img_icon_tipe)
        val btnTinjau: Button = itemView.findViewById(R.id.btn_tinjau_dash)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listRequest[position]

        holder.tvJudul.text = item.judul
        holder.tvSubJudul.text = item.keterangan
        holder.tvNama.text = item.nama
        holder.tvNim.text = item.nim

        if (item.tipe == "ttd") {
            holder.imgIcon.setImageResource(R.drawable.dokumen)
        } else {
            holder.imgIcon.setImageResource(R.drawable.jam)
        }

        holder.btnTinjau.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount() = listRequest.size

    fun updateData(newList: List<Request>) {
        listRequest = newList
        notifyDataSetChanged()
    }
}