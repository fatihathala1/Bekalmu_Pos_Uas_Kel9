package com.bekalmu.pos.ui.kasir

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bekalmu.pos.data.db.BekalMuDatabase
import com.bekalmu.pos.data.repository.AppRepository
import com.bekalmu.pos.databinding.DialogKonfirmasiBayarBinding
import com.bekalmu.pos.databinding.FragmentKasirBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.SessionManager
import com.bekalmu.pos.utils.ViewModelFactory
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.bottomsheet.BottomSheetDialog

class KasirFragment : Fragment() {

    private var _binding: FragmentKasirBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var orderDetailAdapter: CartAdapter
    private lateinit var orderDetailAdapterKategori: CartAdapter
    private lateinit var produkAdapter: ProdukKasirAdapter

    private val viewModel: KasirViewModel by viewModels {
        val db = BekalMuDatabase.getDatabase(requireContext())
        val repo = AppRepository(db.userDao(), db.produkDao(), db.transaksiDao(), db.kasDao())
        ViewModelFactory(repo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentKasirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        setupAdapters()
        setupCategoryCards()
        setupObservers()
        setupListeners()
    }

    private fun setupAdapters() {
        produkAdapter = ProdukKasirAdapter(
            onAdd = { produk -> viewModel.addToCart(produk) },
            onRemove = { produk -> viewModel.removeFromCart(produk) }
        )
        binding.rvProduk.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = produkAdapter
        }
        // Adapter terpisah untuk detail pesanan yang ditampilkan di atas list menu
        orderDetailAdapter = CartAdapter(
            onAdd = { viewModel.addToCart(it) },
            onRemove = { viewModel.removeFromCart(it) }
        )
        binding.rvOrderDetail.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrderDetail.adapter = orderDetailAdapter

        // Adapter terpisah untuk detail pesanan di halaman kategori awal
        orderDetailAdapterKategori = CartAdapter(
            onAdd = { viewModel.addToCart(it) },
            onRemove = { viewModel.removeFromCart(it) }
        )
        binding.rvOrderDetailKategori.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrderDetailKategori.adapter = orderDetailAdapterKategori
    }

    private fun setupCategoryCards() {
        // Sesuai Figma: 4 kategori besar
        binding.cardMinuman.setOnClickListener { showProductsForCategory("Minuman") }
        binding.cardSnack.setOnClickListener { showProductsForCategory("Snack & Cemilan") }
        binding.cardNasiLauk.setOnClickListener { showProductsForCategory("Nasi & Lauk") }
        binding.cardLauk.setOnClickListener { showProductsForCategory("Lauk") }

        binding.btnBackToCategory.setOnClickListener { showCategoryView() }
    }

    private val categoryIcons = mapOf(
        "Minuman" to com.bekalmu.pos.R.drawable.ic_menuminuman,
        "Snack & Cemilan" to com.bekalmu.pos.R.drawable.ic_menusnack,
        "Nasi & Lauk" to com.bekalmu.pos.R.drawable.ic_menumakanan,
        "Lauk" to com.bekalmu.pos.R.drawable.ic_menumakanan
    )

    private fun showProductsForCategory(kategori: String) {
        binding.viewCategoryState.visibility = View.GONE
        binding.viewProductState.visibility = View.VISIBLE
        binding.tvSelectedCategory.text = kategori
        // Set icon kategori
        val iconRes = categoryIcons[kategori] ?: com.bekalmu.pos.R.drawable.ic_food_placeholder
        binding.ivCategoryIcon.setImageResource(iconRes)
        viewModel.setKategori(kategori)
        // Setup search produk
        binding.etSearchProduk.text?.clear()
        binding.etSearchProduk.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchQuery(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun showCategoryView() {
        binding.viewProductState.visibility = View.GONE
        binding.viewCategoryState.visibility = View.VISIBLE
        viewModel.setKategori("Semua")
        // Tidak perlu reset manual di sini — observer cartItems/cartTotal/cartCount
        // sudah otomatis menjaga tampilan tetap sinkron dengan isi cart yang sebenarnya,
        // baik saat kembali dengan cart masih terisi maupun setelah transaksi (cart kosong).
    }

    private fun setupObservers() {
        viewModel.filteredProduk.observe(viewLifecycleOwner) { list ->
            produkAdapter.submitList(list)
        }

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            // Sinkronkan tampilan minus/jumlah/plus di setiap card produk
            val quantities = items.associate { it.produk.produkId to it.jumlah }
            produkAdapter.updateCartQuantities(quantities)
            // Tampilkan detail pesanan di atas list menu, gantikan teks "Belum ada pesanan"
            orderDetailAdapter.submitList(items.toList())
            binding.orderDetailContainer.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
            // Tampilkan juga di halaman kategori awal, gantikan teks "Belum ada pesanan"
            orderDetailAdapterKategori.submitList(items.toList())
            binding.orderDetailContainerKategori.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
            binding.emptyOrderState.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.cartTotal.observe(viewLifecycleOwner) { total ->
            binding.tvTotal.text = CurrencyUtils.format(total)
        }

        viewModel.cartCount.observe(viewLifecycleOwner) { count ->
            binding.btnBayar.isEnabled = count > 0
            binding.tvItemCount.text = "$count items"
        }

        viewModel.transaksiResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is KasirViewModel.TransaksiResult.Success -> {
                    val intent = Intent(requireContext(), StrukActivity::class.java).apply {
                        putExtra("nomorOrder", result.transaksi.nomorOrder)
                        putExtra("totalBayar", result.transaksi.totalHarga)
                        putExtra("metode", result.transaksi.metodePembayaran)
                        putExtra("namaKasir", result.transaksi.namaKasir)
                    }
                    startActivity(intent)
                    viewModel.clearTransaksiResult()
                    showCategoryView()
                }
                is KasirViewModel.TransaksiResult.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
                null -> { /* tidak melakukan apa-apa saat result di-clear */ }
            }
        }
    }

    private fun setupListeners() {
        binding.btnBayar.setOnClickListener { showKonfirmasiDialog() }
        binding.btnClearCart.setOnClickListener { viewModel.clearCart() }
    }

    private fun showKonfirmasiDialog() {
        val items = viewModel.cartItems.value ?: return
        val total = viewModel.cartTotal.value ?: 0L

        val dialogBinding = DialogKonfirmasiBayarBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogBinding.root)

        // Nomor order preview
        dialogBinding.tvNomorOrder.text = "#${com.bekalmu.pos.utils.DateUtils.generateOrderNumber()}"
        dialogBinding.tvTotalBayar.text = CurrencyUtils.format(total)

        // Tambahkan item rows ke llOrderItems
        dialogBinding.llOrderItems.removeAllViews()
        items.forEach { item ->
            val row = layoutInflater.inflate(
                android.R.layout.simple_list_item_2,
                dialogBinding.llOrderItems, false
            )
            val tv1 = row.findViewById<android.widget.TextView>(android.R.id.text1)
            val tv2 = row.findViewById<android.widget.TextView>(android.R.id.text2)
            tv1.text = "${item.produk.nama}  ×${item.jumlah}"
            tv1.setTextColor(requireContext().getColor(com.bekalmu.pos.R.color.text_primary))
            tv1.textSize = 14f
            tv2.text = CurrencyUtils.format(item.subtotal)
            tv2.setTextColor(requireContext().getColor(com.bekalmu.pos.R.color.green_primary))
            tv2.textSize = 13f
            dialogBinding.llOrderItems.addView(row)
        }

        // Logika RadioButton - pilih salah satu
        var selectedMetode = "tunai"
        dialogBinding.rbTunai.isChecked = true

        dialogBinding.optionTunai.setOnClickListener {
            selectedMetode = "tunai"
            dialogBinding.rbTunai.isChecked = true
            dialogBinding.rbQris.isChecked = false
            dialogBinding.optionTunai.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_payment_option_selected)
            dialogBinding.optionQris.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_payment_option)
        }
        dialogBinding.optionQris.setOnClickListener {
            selectedMetode = "qris"
            dialogBinding.rbQris.isChecked = true
            dialogBinding.rbTunai.isChecked = false
            dialogBinding.optionQris.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_payment_option_selected)
            dialogBinding.optionTunai.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_payment_option)
        }
        dialogBinding.rbTunai.setOnClickListener {
            selectedMetode = "tunai"
            dialogBinding.rbQris.isChecked = false
            dialogBinding.optionTunai.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_payment_option_selected)
            dialogBinding.optionQris.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_payment_option)
        }
        dialogBinding.rbQris.setOnClickListener {
            selectedMetode = "qris"
            dialogBinding.rbTunai.isChecked = false
            dialogBinding.optionQris.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_payment_option_selected)
            dialogBinding.optionTunai.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_payment_option)
        }

        dialogBinding.btnKonfirmasi.setOnClickListener {
            viewModel.processTransaksi(
                userId = sessionManager.getUserId(),
                namaKasir = sessionManager.getUserName(),
                metodePembayaran = selectedMetode,
                uangDiterima = total
            )
            dialog.dismiss()
        }

        dialogBinding.btnDismiss.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}