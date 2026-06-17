package com.bekalmu.pos.ui.pengaturan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityPengaturanBinding
import com.bekalmu.pos.databinding.DialogTambahStafBinding
import com.bekalmu.pos.ui.login.LoginActivity
import com.bekalmu.pos.utils.SessionManager
import com.bekalmu.pos.utils.ViewModelFactory

class PengaturanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPengaturanBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var stafAdapter: StafAdapter

    private val viewModel: PengaturanViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengaturanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        viewModel.loadUser(sessionManager.getUserId())

        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Revisi 11: sembunyikan Manajemen Staf untuk kasir, hanya tampil untuk owner
        if (!sessionManager.isOwner()) {
            binding.menuManajemenStaf.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        stafAdapter = StafAdapter(
            currentUserId = sessionManager.getUserId(),
            onDelete = { user ->
                AlertDialog.Builder(this)
                    .setTitle("Hapus Staf")
                    .setMessage("Hapus staf ${user.nama}?")
                    .setPositiveButton("Hapus") { _, _ -> viewModel.deleteUser(user) }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        )
        binding.rvStaf.layoutManager = LinearLayoutManager(this)
        binding.rvStaf.adapter = stafAdapter
        // Sembunyikan daftar staf (label + list) untuk kasir
        if (!sessionManager.isOwner()) {
            binding.tvDaftarStaf.visibility = View.GONE
            binding.rvStaf.visibility = View.GONE
        }
    }

    private fun setupObservers() {
        viewModel.currentUser.observe(this) { user ->
            user?.let {
                binding.tvNamaUser.text = it.nama
                binding.tvRoleUser.text = it.role.replaceFirstChar { c -> c.uppercase() }
            }
        }
        viewModel.allUsers.observe(this) { users ->
            if (sessionManager.isOwner()) stafAdapter.submitList(users)
        }
        viewModel.operationResult.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearResult()
            }
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.menuManajemenStaf.setOnClickListener {
            showTambahStafDialog()
        }

        binding.menuLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Keluar")
                .setMessage("Yakin ingin keluar dari akun ini?")
                .setPositiveButton("Keluar") { _, _ ->
                    sessionManager.clearSession()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun showTambahStafDialog() {
        val dialogBinding = DialogTambahStafBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnSimpan.setOnClickListener {
            val nama = dialogBinding.etNama.text.toString().trim()
            val username = dialogBinding.etUsername.text.toString().trim()
            val password = dialogBinding.etPassword.text.toString().trim()
            // Staf yang ditambahkan owner selalu berperan sebagai Kasir.
            // Pembuatan akun Owner baru tidak diizinkan lewat fitur ini.
            val role = "kasir"

            if (nama.isEmpty()) { dialogBinding.tilNama.error = "Nama wajib diisi"; return@setOnClickListener }
            if (username.isEmpty()) { dialogBinding.tilUsername.error = "Username wajib diisi"; return@setOnClickListener }
            if (password.length < 6) { dialogBinding.tilPassword.error = "Password min. 6 karakter"; return@setOnClickListener }

            viewModel.addStaf(nama, username, password, role)
            dialog.dismiss()
        }
        dialog.show()
    }
}