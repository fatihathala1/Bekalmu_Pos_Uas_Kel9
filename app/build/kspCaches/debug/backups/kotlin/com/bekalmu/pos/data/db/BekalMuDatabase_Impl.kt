package com.bekalmu.pos.`data`.db

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.bekalmu.pos.`data`.dao.KasDao
import com.bekalmu.pos.`data`.dao.KasDao_Impl
import com.bekalmu.pos.`data`.dao.ProdukDao
import com.bekalmu.pos.`data`.dao.ProdukDao_Impl
import com.bekalmu.pos.`data`.dao.TransaksiDao
import com.bekalmu.pos.`data`.dao.TransaksiDao_Impl
import com.bekalmu.pos.`data`.dao.UserDao
import com.bekalmu.pos.`data`.dao.UserDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class BekalMuDatabase_Impl : BekalMuDatabase() {
  private val _userDao: Lazy<UserDao> = lazy {
    UserDao_Impl(this)
  }

  private val _produkDao: Lazy<ProdukDao> = lazy {
    ProdukDao_Impl(this)
  }

  private val _transaksiDao: Lazy<TransaksiDao> = lazy {
    TransaksiDao_Impl(this)
  }

  private val _kasDao: Lazy<KasDao> = lazy {
    KasDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3,
        "51adb4731f9d9d3f2babef929bdacf17", "84d99321a4b4652b37b58fac7f976aa7") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `users` (`userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nama` TEXT NOT NULL, `username` TEXT NOT NULL, `password` TEXT NOT NULL, `role` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `produk` (`produkId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nama` TEXT NOT NULL, `kategori` TEXT NOT NULL, `hargaJual` INTEGER NOT NULL, `hargaBeli` INTEGER NOT NULL, `stok` INTEGER NOT NULL, `deskripsi` TEXT NOT NULL, `imagePath` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `transaksi` (`transaksiId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nomorOrder` TEXT NOT NULL, `userId` INTEGER NOT NULL, `namaKasir` TEXT NOT NULL, `totalHarga` INTEGER NOT NULL, `metodePembayaran` TEXT NOT NULL, `uangDiterima` INTEGER NOT NULL, `kembalian` INTEGER NOT NULL, `status` TEXT NOT NULL, `tanggal` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `detail_transaksi` (`detailId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `transaksiId` INTEGER NOT NULL, `produkId` INTEGER NOT NULL, `namaProduk` TEXT NOT NULL, `hargaSatuan` INTEGER NOT NULL, `jumlah` INTEGER NOT NULL, `subtotal` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `kas` (`kasId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `jenis` TEXT NOT NULL, `kategori` TEXT NOT NULL, `jumlah` INTEGER NOT NULL, `catatan` TEXT NOT NULL, `tanggal` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '51adb4731f9d9d3f2babef929bdacf17')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `users`")
        connection.execSQL("DROP TABLE IF EXISTS `produk`")
        connection.execSQL("DROP TABLE IF EXISTS `transaksi`")
        connection.execSQL("DROP TABLE IF EXISTS `detail_transaksi`")
        connection.execSQL("DROP TABLE IF EXISTS `kas`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsUsers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsers.put("userId", TableInfo.Column("userId", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("nama", TableInfo.Column("nama", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("username", TableInfo.Column("username", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("password", TableInfo.Column("password", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("role", TableInfo.Column("role", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("isActive", TableInfo.Column("isActive", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUsers: TableInfo = TableInfo("users", _columnsUsers, _foreignKeysUsers,
            _indicesUsers)
        val _existingUsers: TableInfo = read(connection, "users")
        if (!_infoUsers.equals(_existingUsers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |users(com.bekalmu.pos.data.model.User).
              | Expected:
              |""".trimMargin() + _infoUsers + """
              |
              | Found:
              |""".trimMargin() + _existingUsers)
        }
        val _columnsProduk: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsProduk.put("produkId", TableInfo.Column("produkId", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("nama", TableInfo.Column("nama", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("kategori", TableInfo.Column("kategori", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("hargaJual", TableInfo.Column("hargaJual", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("hargaBeli", TableInfo.Column("hargaBeli", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("stok", TableInfo.Column("stok", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("deskripsi", TableInfo.Column("deskripsi", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("imagePath", TableInfo.Column("imagePath", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("isActive", TableInfo.Column("isActive", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProduk.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysProduk: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesProduk: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoProduk: TableInfo = TableInfo("produk", _columnsProduk, _foreignKeysProduk,
            _indicesProduk)
        val _existingProduk: TableInfo = read(connection, "produk")
        if (!_infoProduk.equals(_existingProduk)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |produk(com.bekalmu.pos.data.model.Produk).
              | Expected:
              |""".trimMargin() + _infoProduk + """
              |
              | Found:
              |""".trimMargin() + _existingProduk)
        }
        val _columnsTransaksi: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTransaksi.put("transaksiId", TableInfo.Column("transaksiId", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("nomorOrder", TableInfo.Column("nomorOrder", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("namaKasir", TableInfo.Column("namaKasir", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("totalHarga", TableInfo.Column("totalHarga", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("metodePembayaran", TableInfo.Column("metodePembayaran", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("uangDiterima", TableInfo.Column("uangDiterima", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("kembalian", TableInfo.Column("kembalian", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransaksi.put("tanggal", TableInfo.Column("tanggal", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTransaksi: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTransaksi: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTransaksi: TableInfo = TableInfo("transaksi", _columnsTransaksi,
            _foreignKeysTransaksi, _indicesTransaksi)
        val _existingTransaksi: TableInfo = read(connection, "transaksi")
        if (!_infoTransaksi.equals(_existingTransaksi)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |transaksi(com.bekalmu.pos.data.model.Transaksi).
              | Expected:
              |""".trimMargin() + _infoTransaksi + """
              |
              | Found:
              |""".trimMargin() + _existingTransaksi)
        }
        val _columnsDetailTransaksi: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDetailTransaksi.put("detailId", TableInfo.Column("detailId", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetailTransaksi.put("transaksiId", TableInfo.Column("transaksiId", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetailTransaksi.put("produkId", TableInfo.Column("produkId", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetailTransaksi.put("namaProduk", TableInfo.Column("namaProduk", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetailTransaksi.put("hargaSatuan", TableInfo.Column("hargaSatuan", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDetailTransaksi.put("jumlah", TableInfo.Column("jumlah", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDetailTransaksi.put("subtotal", TableInfo.Column("subtotal", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDetailTransaksi: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDetailTransaksi: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDetailTransaksi: TableInfo = TableInfo("detail_transaksi", _columnsDetailTransaksi,
            _foreignKeysDetailTransaksi, _indicesDetailTransaksi)
        val _existingDetailTransaksi: TableInfo = read(connection, "detail_transaksi")
        if (!_infoDetailTransaksi.equals(_existingDetailTransaksi)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |detail_transaksi(com.bekalmu.pos.data.model.DetailTransaksi).
              | Expected:
              |""".trimMargin() + _infoDetailTransaksi + """
              |
              | Found:
              |""".trimMargin() + _existingDetailTransaksi)
        }
        val _columnsKas: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsKas.put("kasId", TableInfo.Column("kasId", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsKas.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsKas.put("jenis", TableInfo.Column("jenis", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsKas.put("kategori", TableInfo.Column("kategori", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsKas.put("jumlah", TableInfo.Column("jumlah", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsKas.put("catatan", TableInfo.Column("catatan", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsKas.put("tanggal", TableInfo.Column("tanggal", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysKas: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesKas: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoKas: TableInfo = TableInfo("kas", _columnsKas, _foreignKeysKas, _indicesKas)
        val _existingKas: TableInfo = read(connection, "kas")
        if (!_infoKas.equals(_existingKas)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |kas(com.bekalmu.pos.data.model.Kas).
              | Expected:
              |""".trimMargin() + _infoKas + """
              |
              | Found:
              |""".trimMargin() + _existingKas)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "produk", "transaksi",
        "detail_transaksi", "kas")
  }

  public override fun clearAllTables() {
    super.performClear(false, "users", "produk", "transaksi", "detail_transaksi", "kas")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(UserDao::class, UserDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ProdukDao::class, ProdukDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TransaksiDao::class, TransaksiDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(KasDao::class, KasDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun userDao(): UserDao = _userDao.value

  public override fun produkDao(): ProdukDao = _produkDao.value

  public override fun transaksiDao(): TransaksiDao = _transaksiDao.value

  public override fun kasDao(): KasDao = _kasDao.value
}
