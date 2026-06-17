package com.bekalmu.pos.`data`.dao

import androidx.lifecycle.LiveData
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.bekalmu.pos.`data`.model.Produk
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ProdukDao_Impl(
  __db: RoomDatabase,
) : ProdukDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfProduk: EntityInsertAdapter<Produk>

  private val __deleteAdapterOfProduk: EntityDeleteOrUpdateAdapter<Produk>

  private val __updateAdapterOfProduk: EntityDeleteOrUpdateAdapter<Produk>
  init {
    this.__db = __db
    this.__insertAdapterOfProduk = object : EntityInsertAdapter<Produk>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `produk` (`produkId`,`nama`,`kategori`,`hargaJual`,`hargaBeli`,`stok`,`deskripsi`,`imagePath`,`isActive`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Produk) {
        statement.bindLong(1, entity.produkId.toLong())
        statement.bindText(2, entity.nama)
        statement.bindText(3, entity.kategori)
        statement.bindLong(4, entity.hargaJual)
        statement.bindLong(5, entity.hargaBeli)
        statement.bindLong(6, entity.stok.toLong())
        statement.bindText(7, entity.deskripsi)
        statement.bindText(8, entity.imagePath)
        val _tmp: Int = if (entity.isActive) 1 else 0
        statement.bindLong(9, _tmp.toLong())
        statement.bindLong(10, entity.createdAt)
      }
    }
    this.__deleteAdapterOfProduk = object : EntityDeleteOrUpdateAdapter<Produk>() {
      protected override fun createQuery(): String = "DELETE FROM `produk` WHERE `produkId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Produk) {
        statement.bindLong(1, entity.produkId.toLong())
      }
    }
    this.__updateAdapterOfProduk = object : EntityDeleteOrUpdateAdapter<Produk>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `produk` SET `produkId` = ?,`nama` = ?,`kategori` = ?,`hargaJual` = ?,`hargaBeli` = ?,`stok` = ?,`deskripsi` = ?,`imagePath` = ?,`isActive` = ?,`createdAt` = ? WHERE `produkId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Produk) {
        statement.bindLong(1, entity.produkId.toLong())
        statement.bindText(2, entity.nama)
        statement.bindText(3, entity.kategori)
        statement.bindLong(4, entity.hargaJual)
        statement.bindLong(5, entity.hargaBeli)
        statement.bindLong(6, entity.stok.toLong())
        statement.bindText(7, entity.deskripsi)
        statement.bindText(8, entity.imagePath)
        val _tmp: Int = if (entity.isActive) 1 else 0
        statement.bindLong(9, _tmp.toLong())
        statement.bindLong(10, entity.createdAt)
        statement.bindLong(11, entity.produkId.toLong())
      }
    }
  }

  public override suspend fun insert(produk: Produk): Long = performSuspending(__db, false, true) {
      _connection ->
    val _result: Long = __insertAdapterOfProduk.insertAndReturnId(_connection, produk)
    _result
  }

  public override suspend fun delete(produk: Produk): Unit = performSuspending(__db, false, true) {
      _connection ->
    __deleteAdapterOfProduk.handle(_connection, produk)
  }

  public override suspend fun update(produk: Produk): Unit = performSuspending(__db, false, true) {
      _connection ->
    __updateAdapterOfProduk.handle(_connection, produk)
  }

  public override fun getAllProduk(): LiveData<List<Produk>> {
    val _sql: String = "SELECT * FROM produk WHERE isActive = 1 ORDER BY nama ASC"
    return __db.invalidationTracker.createLiveData(arrayOf("produk"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfProdukId: Int = getColumnIndexOrThrow(_stmt, "produkId")
        val _columnIndexOfNama: Int = getColumnIndexOrThrow(_stmt, "nama")
        val _columnIndexOfKategori: Int = getColumnIndexOrThrow(_stmt, "kategori")
        val _columnIndexOfHargaJual: Int = getColumnIndexOrThrow(_stmt, "hargaJual")
        val _columnIndexOfHargaBeli: Int = getColumnIndexOrThrow(_stmt, "hargaBeli")
        val _columnIndexOfStok: Int = getColumnIndexOrThrow(_stmt, "stok")
        val _columnIndexOfDeskripsi: Int = getColumnIndexOrThrow(_stmt, "deskripsi")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Produk> = mutableListOf()
        while (_stmt.step()) {
          val _item: Produk
          val _tmpProdukId: Int
          _tmpProdukId = _stmt.getLong(_columnIndexOfProdukId).toInt()
          val _tmpNama: String
          _tmpNama = _stmt.getText(_columnIndexOfNama)
          val _tmpKategori: String
          _tmpKategori = _stmt.getText(_columnIndexOfKategori)
          val _tmpHargaJual: Long
          _tmpHargaJual = _stmt.getLong(_columnIndexOfHargaJual)
          val _tmpHargaBeli: Long
          _tmpHargaBeli = _stmt.getLong(_columnIndexOfHargaBeli)
          val _tmpStok: Int
          _tmpStok = _stmt.getLong(_columnIndexOfStok).toInt()
          val _tmpDeskripsi: String
          _tmpDeskripsi = _stmt.getText(_columnIndexOfDeskripsi)
          val _tmpImagePath: String
          _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          val _tmpIsActive: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              Produk(_tmpProdukId,_tmpNama,_tmpKategori,_tmpHargaJual,_tmpHargaBeli,_tmpStok,_tmpDeskripsi,_tmpImagePath,_tmpIsActive,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getProdukByKategori(kategori: String): LiveData<List<Produk>> {
    val _sql: String = "SELECT * FROM produk WHERE isActive = 1 AND kategori = ? ORDER BY nama ASC"
    return __db.invalidationTracker.createLiveData(arrayOf("produk"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, kategori)
        val _columnIndexOfProdukId: Int = getColumnIndexOrThrow(_stmt, "produkId")
        val _columnIndexOfNama: Int = getColumnIndexOrThrow(_stmt, "nama")
        val _columnIndexOfKategori: Int = getColumnIndexOrThrow(_stmt, "kategori")
        val _columnIndexOfHargaJual: Int = getColumnIndexOrThrow(_stmt, "hargaJual")
        val _columnIndexOfHargaBeli: Int = getColumnIndexOrThrow(_stmt, "hargaBeli")
        val _columnIndexOfStok: Int = getColumnIndexOrThrow(_stmt, "stok")
        val _columnIndexOfDeskripsi: Int = getColumnIndexOrThrow(_stmt, "deskripsi")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Produk> = mutableListOf()
        while (_stmt.step()) {
          val _item: Produk
          val _tmpProdukId: Int
          _tmpProdukId = _stmt.getLong(_columnIndexOfProdukId).toInt()
          val _tmpNama: String
          _tmpNama = _stmt.getText(_columnIndexOfNama)
          val _tmpKategori: String
          _tmpKategori = _stmt.getText(_columnIndexOfKategori)
          val _tmpHargaJual: Long
          _tmpHargaJual = _stmt.getLong(_columnIndexOfHargaJual)
          val _tmpHargaBeli: Long
          _tmpHargaBeli = _stmt.getLong(_columnIndexOfHargaBeli)
          val _tmpStok: Int
          _tmpStok = _stmt.getLong(_columnIndexOfStok).toInt()
          val _tmpDeskripsi: String
          _tmpDeskripsi = _stmt.getText(_columnIndexOfDeskripsi)
          val _tmpImagePath: String
          _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          val _tmpIsActive: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              Produk(_tmpProdukId,_tmpNama,_tmpKategori,_tmpHargaJual,_tmpHargaBeli,_tmpStok,_tmpDeskripsi,_tmpImagePath,_tmpIsActive,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun searchProduk(query: String): LiveData<List<Produk>> {
    val _sql: String =
        "SELECT * FROM produk WHERE isActive = 1 AND nama LIKE '%' || ? || '%' ORDER BY nama ASC"
    return __db.invalidationTracker.createLiveData(arrayOf("produk"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        val _columnIndexOfProdukId: Int = getColumnIndexOrThrow(_stmt, "produkId")
        val _columnIndexOfNama: Int = getColumnIndexOrThrow(_stmt, "nama")
        val _columnIndexOfKategori: Int = getColumnIndexOrThrow(_stmt, "kategori")
        val _columnIndexOfHargaJual: Int = getColumnIndexOrThrow(_stmt, "hargaJual")
        val _columnIndexOfHargaBeli: Int = getColumnIndexOrThrow(_stmt, "hargaBeli")
        val _columnIndexOfStok: Int = getColumnIndexOrThrow(_stmt, "stok")
        val _columnIndexOfDeskripsi: Int = getColumnIndexOrThrow(_stmt, "deskripsi")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Produk> = mutableListOf()
        while (_stmt.step()) {
          val _item: Produk
          val _tmpProdukId: Int
          _tmpProdukId = _stmt.getLong(_columnIndexOfProdukId).toInt()
          val _tmpNama: String
          _tmpNama = _stmt.getText(_columnIndexOfNama)
          val _tmpKategori: String
          _tmpKategori = _stmt.getText(_columnIndexOfKategori)
          val _tmpHargaJual: Long
          _tmpHargaJual = _stmt.getLong(_columnIndexOfHargaJual)
          val _tmpHargaBeli: Long
          _tmpHargaBeli = _stmt.getLong(_columnIndexOfHargaBeli)
          val _tmpStok: Int
          _tmpStok = _stmt.getLong(_columnIndexOfStok).toInt()
          val _tmpDeskripsi: String
          _tmpDeskripsi = _stmt.getText(_columnIndexOfDeskripsi)
          val _tmpImagePath: String
          _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          val _tmpIsActive: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              Produk(_tmpProdukId,_tmpNama,_tmpKategori,_tmpHargaJual,_tmpHargaBeli,_tmpStok,_tmpDeskripsi,_tmpImagePath,_tmpIsActive,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getProdukById(id: Int): Produk? {
    val _sql: String = "SELECT * FROM produk WHERE produkId = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfProdukId: Int = getColumnIndexOrThrow(_stmt, "produkId")
        val _columnIndexOfNama: Int = getColumnIndexOrThrow(_stmt, "nama")
        val _columnIndexOfKategori: Int = getColumnIndexOrThrow(_stmt, "kategori")
        val _columnIndexOfHargaJual: Int = getColumnIndexOrThrow(_stmt, "hargaJual")
        val _columnIndexOfHargaBeli: Int = getColumnIndexOrThrow(_stmt, "hargaBeli")
        val _columnIndexOfStok: Int = getColumnIndexOrThrow(_stmt, "stok")
        val _columnIndexOfDeskripsi: Int = getColumnIndexOrThrow(_stmt, "deskripsi")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: Produk?
        if (_stmt.step()) {
          val _tmpProdukId: Int
          _tmpProdukId = _stmt.getLong(_columnIndexOfProdukId).toInt()
          val _tmpNama: String
          _tmpNama = _stmt.getText(_columnIndexOfNama)
          val _tmpKategori: String
          _tmpKategori = _stmt.getText(_columnIndexOfKategori)
          val _tmpHargaJual: Long
          _tmpHargaJual = _stmt.getLong(_columnIndexOfHargaJual)
          val _tmpHargaBeli: Long
          _tmpHargaBeli = _stmt.getLong(_columnIndexOfHargaBeli)
          val _tmpStok: Int
          _tmpStok = _stmt.getLong(_columnIndexOfStok).toInt()
          val _tmpDeskripsi: String
          _tmpDeskripsi = _stmt.getText(_columnIndexOfDeskripsi)
          val _tmpImagePath: String
          _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          val _tmpIsActive: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result =
              Produk(_tmpProdukId,_tmpNama,_tmpKategori,_tmpHargaJual,_tmpHargaBeli,_tmpStok,_tmpDeskripsi,_tmpImagePath,_tmpIsActive,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalStok(): LiveData<Int> {
    val _sql: String = "SELECT SUM(stok) FROM produk WHERE isActive = 1"
    return __db.invalidationTracker.createLiveData(arrayOf("produk"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int?
        if (_stmt.step()) {
          val _tmp: Int?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(0).toInt()
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalMenu(): LiveData<Int> {
    val _sql: String = "SELECT COUNT(*) FROM produk WHERE isActive = 1"
    return __db.invalidationTracker.createLiveData(arrayOf("produk"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int?
        if (_stmt.step()) {
          val _tmp: Int?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(0).toInt()
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllKategori(): List<String> {
    val _sql: String = "SELECT DISTINCT kategori FROM produk WHERE isActive = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun kurangiStok(produkId: Int, jumlah: Int) {
    val _sql: String = "UPDATE produk SET stok = stok - ? WHERE produkId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, jumlah.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, produkId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun tambahStok(produkId: Int, jumlah: Int) {
    val _sql: String = "UPDATE produk SET stok = stok + ? WHERE produkId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, jumlah.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, produkId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
