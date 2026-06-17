package com.bekalmu.pos.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityLoginBinding
import com.bekalmu.pos.ui.home.MainActivity
import com.bekalmu.pos.utils.SessionManager
import com.bekalmu.pos.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    private val viewModel: LoginViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Check if already logged in
        if (sessionManager.isLoggedIn()) {
            goToMain()
            return
        }

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnMasuk.setOnClickListener {
            val kodeCabang = binding.etKodeCabang.text.toString()
            val username = binding.etPilihKasir.text.toString()
            val password = binding.etPassword.text.toString()

            if (kodeCabang.isBlank()) {
                binding.etKodeCabang.error = "Kode cabang tidak boleh kosong"
                return@setOnClickListener
            }
            viewModel.login(username, password)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnMasuk.isEnabled = !isLoading
        }

        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginViewModel.LoginResult.Success -> {
                    val user = result.user
                    val kodeCabang = binding.etKodeCabang.text.toString().trim()
                    sessionManager.saveSession(user.userId, user.nama, user.role, kodeCabang)
                    Toast.makeText(this, "Selamat datang, ${user.nama}!", Toast.LENGTH_SHORT).show()
                    goToMain()
                }
                is LoginViewModel.LoginResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
