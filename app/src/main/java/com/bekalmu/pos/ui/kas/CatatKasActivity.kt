package com.bekalmu.pos.ui.kas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.model.Kas
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityCatatKasBinding
import com.bekalmu.pos.utils.SessionManager
import com.bekalmu.pos.utils.ViewModelFactory

class CatatKasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatatKasBinding
    private lateinit var sessionManager: SessionManager
    private var jenis: String = "masuk"

    private val viewModel: KasViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    private val kategoriMasuk = listOf("Penjualan", "Modal Tambahan", "Pinjaman", "Lainnya")
    private val kategoriKeluar = listOf("Bahan Baku", "Operasional", "Gaji", "Transportasi", "Lainnya")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatatKasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        jenis = intent.getStringExtra("jenis") ?: "masuk"

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = if (jenis == "masuk") "Catat Kas Masuk" else "Catat Kas Keluar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupKategoriDropdown()
        setupObservers()
        setupListeners()
    }

    private fun setupKategoriDropdown() {
        val list = if (jenis == "masuk") kategoriMasuk else kategoriKeluar
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)
        binding.actvKategori.setAdapter(adapter)
        // AutoCompleteTextView dengan inputType="none" tidak otomatis menampilkan
        // dropdown saat disentuh, jadi perlu dipicu manual lewat listener berikut
        binding.actvKategori.setOnClickListener {
            binding.actvKategori.showDropDown()
        }
        binding.actvKategori.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.actvKategori.showDropDown()
        }
    }

    private fun setupObservers() {
        viewModel.operationResult.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearResult()
                if (!it.startsWith("Gagal")) finish()
            }
        }
    }

    private fun setupListeners() {
        binding.btnSimpan.setOnClickListener {
            if (validateInput()) saveKas()
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        val jumlahStr = binding.etJumlah.text.toString().trim()
        val jumlah = jumlahStr.toLongOrNull()
        if (jumlah == null || jumlah <= 0) {
            binding.tilJumlah.error = "Jumlah harus lebih dari 0"
            isValid = false
        } else {
            binding.tilJumlah.error = null
        }

        val kategori = binding.actvKategori.text.toString().trim()
        if (kategori.isEmpty()) {
            binding.tilKategori.error = "Kategori wajib dipilih"
            isValid = false
        } else {
            binding.tilKategori.error = null
        }

        return isValid
    }

    private fun saveKas() {
        val jumlah = binding.etJumlah.text.toString().toLong()
        val kategori = binding.actvKategori.text.toString().trim()
        val catatan = binding.etCatatan.text.toString().trim()

        val kas = Kas(
            userId = sessionManager.getUserId(),
            jenis = jenis,
            kategori = kategori,
            jumlah = jumlah,
            catatan = catatan
        )
        viewModel.insertKas(kas)
    }
}