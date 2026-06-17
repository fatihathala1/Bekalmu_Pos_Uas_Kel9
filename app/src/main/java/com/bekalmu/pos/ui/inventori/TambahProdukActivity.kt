package com.bekalmu.pos.ui.inventori

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.model.Produk
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.ActivityTambahProdukBinding
import com.bekalmu.pos.utils.ViewModelFactory

class TambahProdukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahProdukBinding
    private var existingProduk: Produk? = null
    private var selectedImagePath: String? = null

    private val viewModel: InventoriViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(applicationContext)
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    private val kategoriList = listOf(
        "Nasi & Lauk", "Lauk", "Minuman", "Snack & Cemilan", "Lainnya"
    )

    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                // Copy gambar ke storage internal app, supaya tidak bergantung pada
                // persistable URI permission dari galeri (yang sering tidak didukung
                // di banyak HP dan menyebabkan crash SecurityException)
                val localPath = copyImageToInternalStorage(uri)
                if (localPath != null) {
                    selectedImagePath = localPath
                    binding.ivFotoProduk.setImageURI(Uri.fromFile(java.io.File(localPath)))
                    binding.ivFotoProduk.visibility = View.VISIBLE
                    binding.layoutPlaceholder.visibility = View.GONE
                } else {
                    Toast.makeText(this, "Gagal memuat gambar, coba pilih foto lain", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Copy gambar dari content URI galeri ke folder internal app (filesDir),
     * lalu kembalikan path absolut file lokal tersebut. Pendekatan ini lebih aman
     * dibanding menyimpan content URI langsung, karena:
     * 1. Tidak butuh takePersistableUriPermission() yang sering gagal/crash
     * 2. Gambar tetap bisa diakses meski foto aslinya dihapus dari galeri nanti
     */
    private fun copyImageToInternalStorage(sourceUri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(sourceUri) ?: return null
            val fileName = "produk_${System.currentTimeMillis()}.jpg"
            val imagesDir = java.io.File(filesDir, "produk_images").apply { mkdirs() }
            val destFile = java.io.File(imagesDir, fileName)

            inputStream.use { input ->
                java.io.FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            destFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    // Permission launcher
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) openImagePicker()
        else Toast.makeText(this, "Izin akses galeri diperlukan", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        existingProduk = intent.getParcelableExtra("produk")

        binding.btnBack.setOnClickListener { finish() }
        binding.tvToolbarTitle.text = if (existingProduk != null) "Edit Produk" else "Tambah Produk"

        setupKategoriDropdown()
        setupImagePicker()
        setupObservers()
        setupListeners()
        populateFormIfEditing()
    }

    private fun setupImagePicker() {
        binding.layoutFotoProduk.setOnClickListener {
            checkPermissionAndOpenPicker()
        }
    }

    private fun checkPermissionAndOpenPicker() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                openImagePicker()
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun setupKategoriDropdown() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            kategoriList
        )
        binding.actvKategori.setAdapter(adapter)
        binding.actvKategori.setOnClickListener {
            binding.actvKategori.showDropDown()
        }
        binding.actvKategori.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.actvKategori.showDropDown()
        }
    }

    private fun populateFormIfEditing() {
        existingProduk?.let { produk ->
            binding.etNamaProduk.setText(produk.nama)
            binding.actvKategori.setText(produk.kategori, false)
            binding.etStokAwal.setText(produk.stok.toString())
            binding.etHargaJual.setText(produk.hargaJual.toString())
            binding.etHargaBeli.setText(produk.hargaBeli.toString())
            if (!produk.imagePath.isNullOrEmpty() && !produk.imagePath.startsWith("drawable://")) {
                try {
                    val file = java.io.File(produk.imagePath)
                    if (file.exists()) {
                        binding.ivFotoProduk.setImageURI(Uri.fromFile(file))
                    } else {
                        binding.ivFotoProduk.setImageURI(Uri.parse(produk.imagePath))
                    }
                    binding.ivFotoProduk.visibility = View.VISIBLE
                    binding.layoutPlaceholder.visibility = View.GONE
                } catch (e: Exception) { /* gambar tidak bisa dimuat, biarkan placeholder */ }
            }
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
            if (validateInput()) saveProduk()
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        val nama = binding.etNamaProduk.text.toString().trim()
        if (nama.isEmpty()) {
            binding.tilNamaProduk.error = "Nama produk wajib diisi"
            isValid = false
        } else binding.tilNamaProduk.error = null

        val kategori = binding.actvKategori.text.toString().trim()
        if (kategori.isEmpty()) {
            binding.tilKategori.error = "Kategori wajib dipilih"
            isValid = false
        } else binding.tilKategori.error = null

        val hargaJual = binding.etHargaJual.text.toString().toLongOrNull()
        if (hargaJual == null || hargaJual <= 0) {
            binding.tilHargaJual.error = "Harga jual harus lebih dari 0"
            isValid = false
        } else binding.tilHargaJual.error = null

        val stok = binding.etStokAwal.text.toString().trim().toIntOrNull()
        if (stok == null || stok < 0) {
            binding.tilStok.error = "Stok awal tidak valid"
            isValid = false
        } else binding.tilStok.error = null

        return isValid
    }

    private fun saveProduk() {
        val nama = binding.etNamaProduk.text.toString().trim()
        val kategori = binding.actvKategori.text.toString().trim()
        val stok = binding.etStokAwal.text.toString().toIntOrNull() ?: 0
        val hargaJual = binding.etHargaJual.text.toString().toLongOrNull() ?: 0L
        val hargaBeli = binding.etHargaBeli.text.toString().toLongOrNull() ?: 0L
        val imagePath = selectedImagePath ?: existingProduk?.imagePath ?: ""

        val produk = existingProduk?.copy(
            nama = nama, kategori = kategori, stok = stok,
            hargaJual = hargaJual, hargaBeli = hargaBeli, imagePath = imagePath
        ) ?: Produk(
            nama = nama, kategori = kategori, stok = stok,
            hargaJual = hargaJual, hargaBeli = hargaBeli, imagePath = imagePath
        )

        if (existingProduk != null) viewModel.updateProduk(produk)
        else viewModel.insertProduk(produk)
    }
}