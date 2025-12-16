package com.example.sitanduapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class JadwalFragment : Fragment() {

    private lateinit var dropdownText: android.widget.TextView
    private lateinit var dropdownArrow: android.widget.ImageView
    private lateinit var dropdownOptions: View
    private lateinit var optionTerbaru: android.widget.TextView
    private lateinit var optionTerlama: android.widget.TextView

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jadwal, container, false)

        // Initialize views
        dropdownText = view.findViewById(R.id.dropdown_text)
        dropdownArrow = view.findViewById(R.id.dropdown_arrow)
        dropdownOptions = view.findViewById(R.id.dropdown_options)
        optionTerbaru = view.findViewById(R.id.option_terbaru)
        optionTerlama = view.findViewById(R.id.option_terlama)

        // Setup dropdown functionality
        setupDropdown(view)

        return view
    }

    private fun setupDropdown(view: View) {
        val dropdownHeader = view.findViewById<View>(R.id.dropdown_header)

        // Set initial date to today
        updateDateText(calendar.time)

        // Setup dropdown header click listener
        dropdownHeader.setOnClickListener {
            showDatePickerDialog()
        }

        // Setup dropdown options click listeners
        optionTerbaru.setOnClickListener {
            dropdownText.text = "Terbaru"
            dropdownOptions.visibility = View.GONE
            dropdownArrow.rotation = 0f
            // TODO: Sort data by newest
            // Filter jadwal berdasarkan tanggal terbaru
        }

        optionTerlama.setOnClickListener {
            dropdownText.text = "Terlama"
            dropdownOptions.visibility = View.GONE
            dropdownArrow.rotation = 0f
            // TODO: Sort data by oldest
            // Filter jadwal berdasarkan tanggal terlama
        }

        // Kalender icon click listener (alternatif: bisa juga membuka date picker)
        dropdownArrow.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update calendar dengan tanggal yang dipilih
                calendar.set(selectedYear, selectedMonth, selectedDay)
                updateDateText(calendar.time)

                // TODO: Filter atau refresh data jadwal berdasarkan tanggal yang dipilih
                loadJadwalForDate(calendar.time)
            },
            year,
            month,
            day
        )

        // Optional: Set min date jika diperlukan
        // datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        // Optional: Set max date jika diperlukan
        // val maxDate = Calendar.getInstance()
        // maxDate.add(Calendar.MONTH, 3)
        // datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }

    private fun updateDateText(date: Date) {
        val formattedDate = dateFormat.format(date)
        dropdownText.text = formattedDate
    }

    private fun loadJadwalForDate(date: Date) {
        // TODO: Implementasi loading jadwal berdasarkan tanggal
        // Contoh: Panggil API atau filter dari database lokal

        // Untuk sementara, tampilkan toast
        android.widget.Toast.makeText(
            requireContext(),
            "Memuat jadwal untuk ${dateFormat.format(date)}",
            android.widget.Toast.LENGTH_SHORT
        ).show()

        // Contoh filter data:
        // 1. Jika data jadwal sudah ada di local
        // 2. Filter berdasarkan tanggal yang dipilih
        // 3. Update UI dengan data yang sudah difilter
    }

    // Helper function untuk mendapatkan nama hari dalam bahasa Indonesia
    private fun getIndonesianDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Minggu"
            Calendar.MONDAY -> "Senin"
            Calendar.TUESDAY -> "Selasa"
            Calendar.WEDNESDAY -> "Rabu"
            Calendar.THURSDAY -> "Kamis"
            Calendar.FRIDAY -> "Jumat"
            Calendar.SATURDAY -> "Sabtu"
            else -> ""
        }
    }
}