package com.bekalmu.pos.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bekalmu.pos.R
import com.bekalmu.pos.databinding.FragmentHomeBinding
import com.bekalmu.pos.ui.inventori.InventoriActivity
import com.bekalmu.pos.ui.kas.KasActivity
import com.bekalmu.pos.ui.laporan.LaporanActivity
import com.bekalmu.pos.ui.pengaturan.PengaturanActivity
import com.bekalmu.pos.ui.transaksi.TransaksiActivity
import com.bekalmu.pos.utils.SessionManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        binding.tvCabang.text = "Cabang: ${sessionManager.getKodeCabang()}"
        setupMenuItems()
        setupOwnerMenu()
    }

    private fun getBottomNav() =
        (activity as? MainActivity)?.binding?.bottomNav

    private fun setupMenuItems() {
        binding.menuKasir.setOnClickListener {
            getBottomNav()?.selectedItemId = R.id.kasirFragment
        }
        binding.menuLaporan.setOnClickListener {
            getBottomNav()?.selectedItemId = R.id.laporanFragment
        }
        binding.menuTransaksi.setOnClickListener {
            startActivity(Intent(requireContext(), TransaksiActivity::class.java))
        }
        binding.menuInventori.setOnClickListener {
            startActivity(Intent(requireContext(), InventoriActivity::class.java))
        }
        binding.menuKas.setOnClickListener {
            startActivity(Intent(requireContext(), KasActivity::class.java))
        }
        binding.menuPengaturan.setOnClickListener {
            startActivity(Intent(requireContext(), PengaturanActivity::class.java))
        }
    }

    private fun setupOwnerMenu() {
        if (sessionManager.isOwner()) {
            // Tampilkan menu Rekap Penjualan khusus owner
            binding.menuRekapPenjualan.visibility = View.VISIBLE
            binding.dividerRekap.visibility = View.VISIBLE
            binding.menuRekapPenjualan.setOnClickListener {
                startActivity(Intent(requireContext(), LaporanActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}