package com.example.sitanduapp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val judul: String,
    val tipe: String, // "ttd" atau "jt"
    val tanggal: String,
    val status: String // "Disetujui"
) : Parcelable