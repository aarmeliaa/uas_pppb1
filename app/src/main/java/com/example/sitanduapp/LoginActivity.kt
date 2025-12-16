package com.example.sitanduapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView
    private lateinit var txtForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edt_login_email)
        edtPassword = findViewById(R.id.edt_login_password)
        btnLogin = findViewById(R.id.btn_login)
        txtRegister = findViewById(R.id.txt_register_login)
        txtForgotPassword = findViewById(R.id.txt_forgot_password)

        // Auto-fill email jika ada dari register atau reset password
        intent.getStringExtra("REGISTERED_EMAIL")?.let {
            edtEmail.setText(it)
            Toast.makeText(this, "Silakan login dengan akun baru Anda", Toast.LENGTH_SHORT).show()
        }
        intent.getStringExtra("RESET_EMAIL")?.let {
            edtEmail.setText(it)
            Toast.makeText(this, "Silakan login dengan password baru", Toast.LENGTH_SHORT).show()
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    edtEmail.error = "Email tidak boleh kosong"
                    edtEmail.requestFocus()
                }
                password.isEmpty() -> {
                    edtPassword.error = "Password tidak boleh kosong"
                    edtPassword.requestFocus()
                }
                !isValidEmail(email) -> {
                    edtEmail.error = "Format email tidak valid"
                    edtEmail.requestFocus()
                    Toast.makeText(this, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    authenticateUser(email, password)
                }
            }
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun authenticateUser(email: String, password: String) {
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Cek apakah email terdaftar
        if (!isEmailRegistered(email)) {
            Toast.makeText(this, "Email belum terdaftar!", Toast.LENGTH_SHORT).show()
            edtEmail.error = "Email belum terdaftar"
            edtEmail.requestFocus()
            return
        }

        // Ambil hash password yang tersimpan
        val storedPasswordHash = sharedPref.getString("${email}_password", null)
        val hashedInputPassword = hashPassword(password)

        // Verifikasi password
        if (storedPasswordHash == hashedInputPassword) {
            // Login berhasil
            saveLoginStatus(email)
            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
            navigateToDashboard(email)
        } else {
            Toast.makeText(this, "Password salah!", Toast.LENGTH_SHORT).show()
            edtPassword.error = "Password salah"
            edtPassword.requestFocus()
        }
    }

    private fun isEmailRegistered(email: String): Boolean {
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        return sharedPref.getString("${email}_name", null) != null
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

    private fun saveLoginStatus(email: String) {
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("${email}_name", "User")
        val userRole = sharedPref.getString("${email}_role", "Mahasiswa")
        val userNim = sharedPref.getString("${email}_nim", "")
        val userNip = sharedPref.getString("${email}_nip", "")

        // Simpan ke SharedPreferences untuk session
        val sessionPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sessionPref.edit()) {
            putString("userEmail", email)
            putString("userName", userName)
            putString("userRole", userRole)

            // Set info berdasarkan role
            val userInfo = when (userRole) {
                "Dosen" -> "NIP: $userNip"
                "Mahasiswa" -> "NIM: $userNim"
                else -> ""
            }
            putString("userInfo", userInfo)

            apply()
        }
    }

    private fun navigateToDashboard(email: String = "") {
        val intent = Intent(this, DashboardActivity::class.java)
        if (email.isNotEmpty()) {
            intent.putExtra("USER_EMAIL", email)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}