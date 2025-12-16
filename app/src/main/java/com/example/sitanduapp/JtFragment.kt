package com.example.sitanduapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class JtFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_jt, container, false)

        dropdownText = view.findViewById(R.id.dropdown_text)
        dropdownArrow = view.findViewById(R.id.dropdown_arrow)
        dropdownOptions = view.findViewById(R.id.dropdown_options)
        optionTerbaru = view.findViewById(R.id.option_terbaru)
        optionTerlama = view.findViewById(R.id.option_terlama)

        setupDropdownWithDatePicker(view)

        setupButtonClickListeners(view)

        updateDateText(calendar.time)

        return view
    }

    private fun setupDropdownWithDatePicker(view: View) {
        val dropdownHeader = view.findViewById<View>(R.id.dropdown_header)

        dropdownHeader.setOnClickListener {
            showDatePickerDialog()
        }

        dropdownArrow.setOnClickListener {
            showDatePickerDialog()
        }

        optionTerbaru.setOnClickListener {
            dropdownText.text = "Terbaru"
            dropdownOptions.visibility = View.GONE
            dropdownArrow.rotation = 0f
            // TODO: Sort data by newest
            filterJanjiTemuBySort("terbaru")
        }

        optionTerlama.setOnClickListener {
            dropdownText.text = "Terlama"
            dropdownOptions.visibility = View.GONE
            dropdownArrow.rotation = 0f
            // TODO: Sort data by oldest
            filterJanjiTemuBySort("terlama")
        }

        dropdownHeader.setOnLongClickListener {
            val isVisible = dropdownOptions.visibility == View.VISIBLE
            dropdownOptions.visibility = if (isVisible) View.GONE else View.VISIBLE
            dropdownArrow.rotation = if (isVisible) 0f else 180f
            true
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

                // TODO: Filter atau refresh data janji temu berdasarkan tanggal yang dipilih
                loadJanjiTemuForDate(calendar.time)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun updateDateText(date: Date) {
        val formattedDate = dateFormat.format(date)
        dropdownText.text = formattedDate
    }

    private fun loadJanjiTemuForDate(date: Date) {
        // TODO: Implementasi loading janji temu berdasarkan tanggal

        android.widget.Toast.makeText(
            requireContext(),
            "Memuat janji temu untuk ${dateFormat.format(date)}",
            android.widget.Toast.LENGTH_SHORT
        ).show()

    }

    private fun filterJanjiTemuBySort(sortType: String) {
        // TODO: Implementasi sorting janji temu

        android.widget.Toast.makeText(
            requireContext(),
            "Menyortir janji temu: $sortType",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupButtonClickListeners(view: View) {
        view.findViewById<android.widget.Button>(R.id.button_tinjau_jt_1)?.setOnClickListener {
            navigateToDetailJtActivity(1) // Parameter bisa ID janji temu
        }

        view.findViewById<android.widget.Button>(R.id.button_tinjau_jt_2)?.setOnClickListener {
            navigateToDetailJtActivity(2) // Parameter bisa ID janji temu
        }
    }

    private fun navigateToDetailJtActivity(janjiTemuId: Int) {
        val intent = Intent(requireContext(), DetailJtActivity::class.java)

        intent.putExtra("JANJI_TEMU_ID", janjiTemuId)
        intent.putExtra("SELECTED_DATE", calendar.timeInMillis)

        startActivity(intent)
    }
}