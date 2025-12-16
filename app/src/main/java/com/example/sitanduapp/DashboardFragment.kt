package com.example.sitanduapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sitanduapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var userEmail: String
    private lateinit var userName: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        userEmail = sharedPref.getString("userEmail", "Guest") ?: "Guest"
        userName = sharedPref.getString("userName", "User") ?: "User"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonListeners()
        setupTinjauButtons(view)
    }

    private fun setupButtonListeners() {
        binding.lainnyaTtd.setOnClickListener {
            navigateToFragment(R.id.ttdFragment, "Tanda Tangan")
        }

        binding.lainnyaJt.setOnClickListener {
            navigateToFragment(R.id.jtFragment, "Janji Temu")
        }

        binding.lainnyaJadwal.setOnClickListener {
            navigateToFragment(R.id.jadwalFragment, "Jadwal")
        }
    }

    private fun setupTinjauButtons(view: View) {
        // Temukan semua button "Tinjau" di layout
        setupTtdTinjauButtons(view)
        setupJtTinjauButtons(view)
    }

    private fun setupTtdTinjauButtons(view: View) {
        // Card 1 - Tanda Tangan
        view.findViewById<Button>(R.id.card1_ttd_btn_tinjau)?.setOnClickListener {
            openDetailTtd("Persetujuan KRS", "Semester Ganjil", "Jacob Louis", "24/555555/sv/22222")
        }

        // Card 2 - Tanda Tangan
        view.findViewById<Button>(R.id.card2_ttd_btn_tinjau)?.setOnClickListener {
            openDetailTtd("Persetujuan KRS", "Semester Ganjil", "Jacob Louis", "24/555555/sv/22222")
        }
    }

    private fun setupJtTinjauButtons(view: View) {
        // Card 1 - Janji Temu
        view.findViewById<Button>(R.id.card1_jt_btn_tinjau)?.setOnClickListener {
            openDetailJt(
                "Bimbingan Tugas Akhir",
                "Jacob Louis",
                "24/555555/sv/22222",
                "Senin, 13 Oktober 2025",
                "10.00 - 11.00"
            )
        }

        // Card 2 - Janji Temu
        view.findViewById<Button>(R.id.card2_jt_btn_tinjau)?.setOnClickListener {
            openDetailJt(
                "Bimbingan Tugas Akhir",
                "Jacob Louis",
                "24/555555/sv/22222",
                "Senin, 13 Oktober 2025",
                "10.00 - 11.00"
            )
        }
    }

    private fun openDetailTtd(jenis: String, semester: String, nama: String, nim: String) {
        val intent = Intent(requireContext(), DetailTtdActivity::class.java)
        intent.putExtra("JENIS_TTD", jenis)
        intent.putExtra("SEMESTER", semester)
        intent.putExtra("NAMA_MAHASISWA", nama)
        intent.putExtra("NIM", nim)
        startActivity(intent)

        // Tambahkan animasi
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun openDetailJt(jenis: String, nama: String, nim: String, tanggal: String, waktu: String) {
        val intent = Intent(requireContext(), DetailJtActivity::class.java)
        intent.putExtra("JENIS_JT", jenis)
        intent.putExtra("NAMA_MAHASISWA", nama)
        intent.putExtra("NIM", nim)
        intent.putExtra("TANGGAL", tanggal)
        intent.putExtra("WAKTU", waktu)
        startActivity(intent)

        // Tambahkan animasi
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun navigateToFragment(fragmentId: Int, fragmentName: String) {
        try {
            val currentDestination = findNavController().currentDestination?.id
            if (currentDestination != fragmentId) {
                findNavController().navigate(fragmentId)
                Toast.makeText(
                    requireContext(),
                    "Membuka halaman $fragmentName",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Gagal membuka halaman $fragmentName: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}