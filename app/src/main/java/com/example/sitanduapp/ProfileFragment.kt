package com.example.sitanduapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    // Views
    private lateinit var imgProfile: ImageView
    private lateinit var txtProfileNama: TextView
    private lateinit var txtProfileRoleLabel: TextView
    private lateinit var txtProfileInfo: TextView
    private lateinit var txtProfileEmail: TextView
    private lateinit var layoutLogout: LinearLayout

    // User data
    private var userEmail: String = ""
    private var userName: String = ""
    private var userRole: String = ""
    private var userInfo: String = ""
    private var userNim: String = ""
    private var userNip: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        initViews(view)

        // Load user data
        loadUserData()

        // Display user data
        displayUserData()

        // Setup logout button
        setupLogoutButton()
    }

    private fun initViews(view: View) {
        imgProfile = view.findViewById(R.id.img_profile)
        txtProfileNama = view.findViewById(R.id.txt_profile_nama)
        txtProfileRoleLabel = view.findViewById(R.id.txt_profile_role_label)
        txtProfileInfo = view.findViewById(R.id.txt_profile_info)
        txtProfileEmail = view.findViewById(R.id.txt_profile_email)
        layoutLogout = view.findViewById(R.id.layout_logout)
    }

    private fun loadUserData() {
        // Ambil data dari SharedPreferences session
        val sessionPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        userEmail = sessionPref.getString("userEmail", "") ?: ""

        if (userEmail.isNotEmpty()) {
            // Ambil data lengkap dari UserData
            val userDataPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
            userName = userDataPref.getString("${userEmail}_name", "Nama User") ?: "Nama User"
            userRole = userDataPref.getString("${userEmail}_role", "Mahasiswa") ?: "Mahasiswa"
            userNim = userDataPref.getString("${userEmail}_nim", "") ?: ""
            userNip = userDataPref.getString("${userEmail}_nip", "") ?: ""

            // Set info berdasarkan role
            userInfo = when (userRole) {
                "Dosen" -> if (userNip.isNotEmpty()) "NIP: $userNip" else "Teknologi Rekayasa\nInstrumentasi dan Kontrol"
                "Mahasiswa" -> if (userNim.isNotEmpty()) "NIM: $userNim" else "Program Studi\nTeknologi Informasi"
                else -> "Informasi tidak tersedia"
            }
        }
    }

    private fun displayUserData() {
        // Set nama
        txtProfileNama.text = userName

        // Set role label
        txtProfileRoleLabel.text = when (userRole) {
            "Dosen" -> "Dosen:"
            "Mahasiswa" -> "Mahasiswa:"
            else -> "Role:"
        }

        // Set info (prodi atau department)
        txtProfileInfo.text = userInfo

        // Set email
        txtProfileEmail.text = if (userEmail.isNotEmpty()) userEmail else "Email tidak tersedia"
    }

    private fun setupLogoutButton() {
        layoutLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Ya") { dialog, _ ->
                dialog.dismiss()
                performLogout()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

    private fun performLogout() {
        // Panggil fungsi logout dari DashboardActivity
        (activity as? DashboardActivity)?.logout()
    }
}