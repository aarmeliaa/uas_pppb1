package com.example.sitanduapp.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Request(
    val id: Int,
    val tipe: String, // "ttd" atau "jt"
    @SerializedName("nama_mahasiswa") val nama: String,
    val nim: String,
    @SerializedName("judul_dokumen") val judul: String,
    val keterangan: String,
    val tanggal: String,
    val waktu: String?, // null kalau ttd
    val status: String
) : Parcelable