package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class TtdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ttd, container, false)
        setupDropdown(view)
        setupButtonClickListeners(view)

        return view
    }

    private fun setupDropdown(view: View) {
        val dropdownHeader = view.findViewById<View>(R.id.dropdown_header)
        val dropdownOptions = view.findViewById<View>(R.id.dropdown_options)
        val dropdownArrow = view.findViewById<android.widget.ImageView>(R.id.dropdown_arrow)
        val dropdownText = view.findViewById<android.widget.TextView>(R.id.dropdown_text)
        val optionTerbaru = view.findViewById<android.widget.TextView>(R.id.option_terbaru)
        val optionTerlama = view.findViewById<android.widget.TextView>(R.id.option_terlama)

        dropdownHeader.setOnClickListener {
            val isVisible = dropdownOptions.visibility == View.VISIBLE
            dropdownOptions.visibility = if (isVisible) View.GONE else View.VISIBLE
            dropdownArrow.rotation = if (isVisible) 0f else 180f
        }

        optionTerbaru.setOnClickListener {
            dropdownText.text = "Terbaru"
            dropdownOptions.visibility = View.GONE
            dropdownArrow.rotation = 0f
            // TODO: Sort data by newest
        }

        optionTerlama.setOnClickListener {
            dropdownText.text = "Terlama"
            dropdownOptions.visibility = View.GONE
            dropdownArrow.rotation = 0f
            // TODO: Sort data by oldest
        }
    }

    private fun setupButtonClickListeners(view: View) {
        view.findViewById<android.widget.Button>(R.id.button_tinjau_ttd_1)?.setOnClickListener {
            navigateToDetailTtdActivity()
        }
        view.findViewById<android.widget.Button>(R.id.button_tinjau_ttd_2)?.setOnClickListener {
            navigateToDetailTtdActivity()
        }
        view.findViewById<android.widget.Button>(R.id.button_tinjau_ttd_3)?.setOnClickListener {
            navigateToDetailTtdActivity()
        }
        view.findViewById<android.widget.Button>(R.id.button_tinjau_ttd_4)?.setOnClickListener {
            navigateToDetailTtdActivity()
        }
    }

    private fun navigateToDetailTtdActivity() {
        val intent = Intent(requireContext(), DetailTtdActivity::class.java)
        startActivity(intent)
    }
}