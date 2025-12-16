package com.example.sitanduapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var edtForgetEmail: EditText
    private lateinit var btnForget: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        edtForgetEmail = findViewById(R.id.edt_forget_email)
        btnForget = findViewById(R.id.btn_forget)

        btnForget.setOnClickListener {
            val email = edtForgetEmail.text.toString().trim()

            when {
                email.isEmpty() -> {
                    edtForgetEmail.error = "Email tidak boleh kosong"
                    edtForgetEmail.requestFocus()
                }
                !isValidEmail(email) -> {
                    edtForgetEmail.error = "Format email tidak valid"
                    edtForgetEmail.requestFocus()
                }
                !isEmailRegistered(email) -> {
                    edtForgetEmail.error = "Email belum terdaftar"
                    edtForgetEmail.requestFocus()
                    Toast.makeText(this, "Email belum terdaftar!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    sendVerificationCode(email)
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isEmailRegistered(email: String): Boolean {
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        return sharedPref.getString("${email}_name", null) != null
    }

    private fun sendVerificationCode(email: String) {
        // Simpan email untuk reset password
        val intent = Intent(this, ResetPasswordActivity::class.java)
        intent.putExtra("USER_EMAIL", email)

        Toast.makeText(
            this,
            "Kode verifikasi telah dikirim ke $email",
            Toast.LENGTH_LONG
        ).show()

        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}