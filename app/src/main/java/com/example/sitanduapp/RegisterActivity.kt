package com.example.sitanduapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var containerRoleFields: LinearLayout
    private lateinit var txtNamaLabel: TextView
    private lateinit var edtNama: EditText
    private lateinit var txtNimLabel: TextView
    private lateinit var edtNim: EditText
    private lateinit var txtNipLabel: TextView
    private lateinit var edtNip: EditText
    private lateinit var btnLanjutkan: Button
    private lateinit var txtRgtrLogin: TextView

    private var selectedRole: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtEmail = findViewById(R.id.edt_register_email)
        edtPassword = findViewById(R.id.edt_register_password)
        spinnerRole = findViewById(R.id.spinner_role)
        containerRoleFields = findViewById(R.id.container_role_fields)
        txtNamaLabel = findViewById(R.id.txt_nama_label)
        edtNama = findViewById(R.id.edt_nama)
        txtNimLabel = findViewById(R.id.txt_nim_label)
        edtNim = findViewById(R.id.edt_nim)
        txtNipLabel = findViewById(R.id.txt_nip_label)
        edtNip = findViewById(R.id.edt_nip)
        btnLanjutkan = findViewById(R.id.btn_lanjutkan)
        txtRgtrLogin = findViewById(R.id.txt_rgtr_login)

        setupRoleSpinner()

        btnLanjutkan.setOnClickListener {
            validateAndRegister()
        }

        txtRgtrLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupRoleSpinner() {
        val roles = arrayOf("Pilih Role", "Mahasiswa", "Dosen")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRole = roles[position]
                showRoleFields(selectedRole)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                hideAllRoleFields()
            }
        }
    }

    private fun showRoleFields(role: String) {
        when (role) {
            "Mahasiswa" -> {
                containerRoleFields.visibility = View.VISIBLE
                txtNamaLabel.visibility = View.VISIBLE
                edtNama.visibility = View.VISIBLE
                txtNimLabel.visibility = View.VISIBLE
                edtNim.visibility = View.VISIBLE
                txtNipLabel.visibility = View.GONE
                edtNip.visibility = View.GONE
            }
            "Dosen" -> {
                containerRoleFields.visibility = View.VISIBLE
                txtNamaLabel.visibility = View.VISIBLE
                edtNama.visibility = View.VISIBLE
                txtNimLabel.visibility = View.GONE
                edtNim.visibility = View.GONE
                txtNipLabel.visibility = View.VISIBLE
                edtNip.visibility = View.VISIBLE
            }
            else -> {
                hideAllRoleFields()
            }
        }
    }

    private fun hideAllRoleFields() {
        containerRoleFields.visibility = View.GONE
        txtNamaLabel.visibility = View.GONE
        edtNama.visibility = View.GONE
        txtNimLabel.visibility = View.GONE
        edtNim.visibility = View.GONE
        txtNipLabel.visibility = View.GONE
        edtNip.visibility = View.GONE
    }

    private fun validateAndRegister() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val nama = edtNama.text.toString().trim()
        val nim = edtNim.text.toString().trim()
        val nip = edtNip.text.toString().trim()

        if (email.isEmpty()) {
            edtEmail.error = "Email tidak boleh kosong"
            edtEmail.requestFocus()
            return
        }

        if (!isValidEmail(email)) {
            edtEmail.error = "Format email tidak valid"
            edtEmail.requestFocus()
            Toast.makeText(this, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        if (isEmailAlreadyRegistered(email)) {
            edtEmail.error = "Email sudah terdaftar"
            edtEmail.requestFocus()
            Toast.makeText(this, "Email sudah terdaftar! Silakan login", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            edtPassword.error = "Password tidak boleh kosong"
            edtPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            edtPassword.error = "Password minimal 6 karakter"
            edtPassword.requestFocus()
            return
        }

        if (selectedRole == "Pilih Role") {
            Toast.makeText(this, "Silakan pilih role terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        if (nama.isEmpty()) {
            edtNama.error = "Nama tidak boleh kosong"
            edtNama.requestFocus()
            return
        }

        when (selectedRole) {
            "Mahasiswa" -> {
                if (nim.isEmpty()) {
                    edtNim.error = "NIM tidak boleh kosong"
                    edtNim.requestFocus()
                    return
                }
                registerUser(email, password, nama, nim, "", "Mahasiswa")
            }
            "Dosen" -> {
                if (nip.isEmpty()) {
                    edtNip.error = "NIP tidak boleh kosong"
                    edtNip.requestFocus()
                    return
                }
                registerUser(email, password, nama, "", nip, "Dosen")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isEmailAlreadyRegistered(email: String): Boolean {
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        return sharedPref.getString("${email}_name", null) != null
    }

    private fun registerUser(email: String, password: String, nama: String, nim: String, nip: String, role: String) {
        // Simpan data user ke SharedPreferences
        saveUserData(email, password, nama, role, nim, nip)

        Toast.makeText(this, "Registrasi berhasil! Silakan login", Toast.LENGTH_LONG).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("REGISTERED_EMAIL", email)
        startActivity(intent)
        finish()
    }

    private fun saveUserData(email: String, password: String, nama: String, role: String, nim: String, nip: String) {
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            // Simpan data user
            putString("${email}_name", nama)
            putString("${email}_password", hashPassword(password)) // Simpan hash password
            putString("${email}_role", role)

            when (role) {
                "Mahasiswa" -> putString("${email}_nim", nim)
                "Dosen" -> putString("${email}_nip", nip)
            }

            apply()
        }

        // Debug: Tampilkan data yang disimpan
        println("DEBUG: User registered - Email: $email, Name: $nama, Role: $role")
    }

    private fun hashPassword(password: String): String {
        return try {
            val bytes = password.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            digest.fold("") { str, it -> str + "%02x".format(it) }
        } catch (e: Exception) {
            password // Fallback jika hashing gagal
        }
    }
}