package com.example.sitanduapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var edtResetPassword1: EditText
    private lateinit var edtResetPassword2: EditText
    private lateinit var btnReset: Button
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        edtResetPassword1 = findViewById(R.id.edt_reset_password1)
        edtResetPassword2 = findViewById(R.id.edt_reset_password2)
        btnReset = findViewById(R.id.btn_reset)

        // Tampilkan email yang akan direset
        if (userEmail.isNotEmpty()) {
            Toast.makeText(this, "Reset password untuk: $userEmail", Toast.LENGTH_SHORT).show()
        }

        btnReset.setOnClickListener {
            validateAndResetPassword()
        }
    }

    private fun validateAndResetPassword() {
        val password1 = edtResetPassword1.text.toString().trim()
        val password2 = edtResetPassword2.text.toString().trim()

        when {
            password1.isEmpty() -> {
                edtResetPassword1.error = "Password tidak boleh kosong"
                edtResetPassword1.requestFocus()
                return
            }
            password1.length < 6 -> {
                edtResetPassword1.error = "Password minimal 6 karakter"
                edtResetPassword1.requestFocus()
                return
            }
        }

        when {
            password2.isEmpty() -> {
                edtResetPassword2.error = "Konfirmasi password tidak boleh kosong"
                edtResetPassword2.requestFocus()
                return
            }
            password2 != password1 -> {
                edtResetPassword2.error = "Password tidak cocok"
                edtResetPassword2.requestFocus()
                return
            }
        }

        resetPassword(password1)
    }

    private fun resetPassword(newPassword: String) {
        // Update password di database
        if (updateUserPassword(userEmail, newPassword)) {
            Toast.makeText(
                this,
                "Password berhasil direset! Silakan login dengan password baru",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("RESET_EMAIL", userEmail)
            // Clear all previous activities
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(
                this,
                "Gagal reset password. User tidak ditemukan!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateUserPassword(email: String, newPassword: String): Boolean {
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Cek apakah user ada
        if (sharedPref.getString("${email}_name", null) == null) {
            return false
        }

        // Update password
        with(sharedPref.edit()) {
            putString("${email}_password", hashPassword(newPassword))
            apply()
        }

        return true
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }
}