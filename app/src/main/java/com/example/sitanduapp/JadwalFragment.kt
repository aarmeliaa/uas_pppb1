package com.example.sitanduapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sitanduapp.adapter.JadwalAdapter
import com.example.sitanduapp.database.AppDatabase
import com.example.sitanduapp.database.HistoryEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class JadwalFragment : Fragment() {

    private lateinit var dropdownText: android.widget.TextView
    private lateinit var dropdownArrow: android.widget.ImageView
    private lateinit var dropdownOptions: View
    private lateinit var optionTerbaru: android.widget.TextView
    private lateinit var optionTerlama: android.widget.TextView
    private lateinit var rvJadwal: RecyclerView
    private lateinit var adapter: JadwalAdapter
    private var allDataHistory: List<HistoryEntity> = emptyList() // Simpan semua data di sini

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jadwal, container, false)

        dropdownText = view.findViewById(R.id.dropdown_text)
        dropdownArrow = view.findViewById(R.id.dropdown_arrow)
        dropdownOptions = view.findViewById(R.id.dropdown_options)
        optionTerbaru = view.findViewById(R.id.option_terbaru)
        optionTerlama = view.findViewById(R.id.option_terlama)
        rvJadwal = view.findViewById(R.id.rv_jadwal)
        rvJadwal.layoutManager = LinearLayoutManager(context)
        adapter = JadwalAdapter(emptyList())
        rvJadwal.adapter = adapter

        setupDropdown(view)

        loadDataFromRoom()

        return view
    }

    private fun loadDataFromRoom() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                val rawData = db.historyDao().getAllHistory()

                allDataHistory = rawData.filter { it.tipe == "jt" }

                withContext(Dispatchers.Main) {
                    adapter.updateData(allDataHistory)

                    if (allDataHistory.isEmpty()) {
                        Toast.makeText(context, "Belum ada jadwal yang disetujui", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error DB: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupDropdown(view: View) {
        val dropdownHeader = view.findViewById<View>(R.id.dropdown_header)

        dropdownHeader.setOnClickListener {
            if (dropdownOptions.visibility == View.VISIBLE) {
                dropdownOptions.visibility = View.GONE
                dropdownArrow.animate().rotation(0f).start()
            } else {
                dropdownOptions.visibility = View.VISIBLE
                dropdownArrow.animate().rotation(180f).start()
            }
        }

        optionTerbaru.setOnClickListener {
            dropdownText.text = "Terbaru"
            dropdownOptions.visibility = View.GONE
            dropdownArrow.rotation = 0f

            val sorted = allDataHistory.sortedByDescending { it.id }
            adapter.updateData(sorted)
        }

        optionTerlama.setOnClickListener {
            dropdownText.text = "Terlama"
            dropdownOptions.visibility = View.GONE
            dropdownArrow.rotation = 0f

            val sorted = allDataHistory.sortedBy { it.id }
            adapter.updateData(sorted)
        }

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
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDateStr = dateFormat.format(calendar.time)

                dropdownText.text = selectedDateStr

                val filteredList = allDataHistory.filter {
                    it.tanggal.contains(selectedDateStr, ignoreCase = true)
                }

                adapter.updateData(filteredList)

                if (filteredList.isEmpty()) {
                    Toast.makeText(context, "Tidak ada jadwal pada tanggal ini", Toast.LENGTH_SHORT).show()
                }
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}