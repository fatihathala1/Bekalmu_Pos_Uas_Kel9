package com.bekalmu.pos.`data`.dao

import androidx.collection.LongSparseArray
import androidx.lifecycle.LiveData
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchLongSparseArray
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import com.bekalmu.pos.`data`.model.DetailTransaksi
import com.bekalmu.pos.`data`.model.Transaksi
import com.bekalmu.pos.`data`.model.TransaksiWithDetails
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TransaksiDao_Impl(
  __db: RoomDatabase,
) : TransaksiDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTransaksi: EntityInsertAdapter<Transaksi>

  private val __insertAdapterOfDetailTransaksi: EntityInsertAdapter<DetailTransaksi>

  private val __updateAdapterOfTransaksi: EntityDeleteOrUpdateAdapter<Transaksi>
  init {
    this.__db = __db
    this.__insertAdapterOfTransaksi = object : EntityInsertAdapter<Transaksi>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `transaksi` (`transaksiId`,`nomorOrder`,`userId`,`namaKasir`,`totalHarga`,`metodePembayaran`,`uangDiterima`,`kembalian`,`status`,`tanggal`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Transaksi) {
        statement.bindLong(1, entity.transaksiId.toLong())
        statement.bindText(2, entity.nomorOrder)
        statement.bindLong(3, entity.userId.toLong())
        statement.bindText(4, entity.namaKasir)
        statement.bindLong(5, entity.totalHarga)
        statement.bindText(6, entity.metodePembayaran)
        statement.bindLong(7, entity.uangDiterima)
        statement.bindLong(8, entity.kembalian)
        statement.bindText(9, entity.status)
        statement.bindLong(10, entity.tanggal)
      }
    }
    this.__insertAdapterOfDetailTransaksi = object : EntityInsertAdapter<DetailTransaksi>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `detail_transaksi` (`detailId`,`transaksiId`,`produkId`,`namaProduk`,`hargaSatuan`,`jumlah`,`subtotal`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DetailTransaksi) {
        statement.bindLong(1, entity.detailId.toLong())
        statement.bindLong(2, entity.transaksiId.toLong())
        statement.bindLong(3, entity.produkId.toLong())
        statement.bindText(4, entity.namaProduk)
        statement.bindLong(5, entity.hargaSatuan)
        statement.bindLong(6, entity.jumlah.toLong())
        statement.bindLong(7, entity.subtotal)
      }
    }
    this.__updateAdapterOfTransaksi = object : EntityDeleteOrUpdateAdapter<Transaksi>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `transaksi` SET `transaksiId` = ?,`nomorOrder` = ?,`userId` = ?,`namaKasir` = ?,`totalHarga` = ?,`metodePembayaran` = ?,`uangDiterima` = ?,`kembalian` = ?,`status` = ?,`tanggal` = ? WHERE `transaksiId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Transaksi) {
        statement.bindLong(1, entity.transaksiId.toLong())
        statement.bindText(2, entity.nomorOrder)
        statement.bindLong(3, entity.userId.toLong())
        statement.bindText(4, entity.namaKasir)
        statement.bindLong(5, entity.totalHarga)
        statement.bindText(6, entity.metodePembayaran)
        statement.bindLong(7, entity.uangDiterima)
        statement.bindLong(8, entity.kembalian)
        statement.bindText(9, entity.status)
        statement.bindLong(10, entity.tanggal)
        statement.bindLong(11, entity.transaksiId.toLong())
      }
    }
  }

  public override suspend fun insertTransaksi(transaksi: Transaksi): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfTransaksi.insertAndReturnId(_connection, transaksi)
    _result
  }

  public override suspend fun insertDetail(detail: DetailTransaksi): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfDetailTransaksi.insert(_connection, detail)
  }

  public override suspend fun insertAllDetails(details: List<DetailTransaksi>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDetailTransaksi.insert(_connection, details)
  }

  public override suspend fun updateTransaksi(transaksi: Transaksi): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfTransaksi.handle(_connection, transaksi)
  }

  public override fun getAllTransaksiWithDetails(): LiveData<List<TransaksiWithDetails>> {
    val _sql: String = "SELECT * FROM transaksi ORDER BY tanggal DESC"
    return __db.invalidationTracker.createLiveData(arrayOf("detail_transaksi", "transaksi"), true) {
        _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfTransaksiId: Int = getColumnIndexOrThrow(_stmt, "transaksiId")
        val _columnIndexOfNomorOrder: Int = getColumnIndexOrThrow(_stmt, "nomorOrder")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfNamaKasir: Int = getColumnIndexOrThrow(_stmt, "namaKasir")
        val _columnIndexOfTotalHarga: Int = getColumnIndexOrThrow(_stmt, "totalHarga")
        val _columnIndexOfMetodePembayaran: Int = getColumnIndexOrThrow(_stmt, "metodePembayaran")
        val _columnIndexOfUangDiterima: Int = getColumnIndexOrThrow(_stmt, "uangDiterima")
        val _columnIndexOfKembalian: Int = getColumnIndexOrThrow(_stmt, "kembalian")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _collectionDetails: LongSparseArray<MutableList<DetailTransaksi>> =
            LongSparseArray<MutableList<DetailTransaksi>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfTransaksiId)
          if (!_collectionDetails.containsKey(_tmpKey)) {
            _collectionDetails.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipdetailTransaksiAscomBekalmuPosDataModelDetailTransaksi(_connection,
            _collectionDetails)
        val _result: MutableList<TransaksiWithDetails> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransaksiWithDetails
          val _tmpTransaksi: Transaksi
          val _tmpTransaksiId: Int
          _tmpTransaksiId = _stmt.getLong(_columnIndexOfTransaksiId).toInt()
          val _tmpNomorOrder: String
          _tmpNomorOrder = _stmt.getText(_columnIndexOfNomorOrder)
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpNamaKasir: String
          _tmpNamaKasir = _stmt.getText(_columnIndexOfNamaKasir)
          val _tmpTotalHarga: Long
          _tmpTotalHarga = _stmt.getLong(_columnIndexOfTotalHarga)
          val _tmpMetodePembayaran: String
          _tmpMetodePembayaran = _stmt.getText(_columnIndexOfMetodePembayaran)
          val _tmpUangDiterima: Long
          _tmpUangDiterima = _stmt.getLong(_columnIndexOfUangDiterima)
          val _tmpKembalian: Long
          _tmpKembalian = _stmt.getLong(_columnIndexOfKembalian)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpTanggal: Long
          _tmpTanggal = _stmt.getLong(_columnIndexOfTanggal)
          _tmpTransaksi =
              Transaksi(_tmpTransaksiId,_tmpNomorOrder,_tmpUserId,_tmpNamaKasir,_tmpTotalHarga,_tmpMetodePembayaran,_tmpUangDiterima,_tmpKembalian,_tmpStatus,_tmpTanggal)
          val _tmpDetailsCollection: MutableList<DetailTransaksi>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfTransaksiId)
          _tmpDetailsCollection = checkNotNull(_collectionDetails.get(_tmpKey_1))
          _item = TransaksiWithDetails(_tmpTransaksi,_tmpDetailsCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTransaksiByDate(startOfDay: Long, endOfDay: Long):
      LiveData<List<TransaksiWithDetails>> {
    val _sql: String =
        "SELECT * FROM transaksi WHERE tanggal >= ? AND tanggal <= ? ORDER BY tanggal DESC"
    return __db.invalidationTracker.createLiveData(arrayOf("detail_transaksi", "transaksi"), true) {
        _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startOfDay)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endOfDay)
        val _columnIndexOfTransaksiId: Int = getColumnIndexOrThrow(_stmt, "transaksiId")
        val _columnIndexOfNomorOrder: Int = getColumnIndexOrThrow(_stmt, "nomorOrder")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfNamaKasir: Int = getColumnIndexOrThrow(_stmt, "namaKasir")
        val _columnIndexOfTotalHarga: Int = getColumnIndexOrThrow(_stmt, "totalHarga")
        val _columnIndexOfMetodePembayaran: Int = getColumnIndexOrThrow(_stmt, "metodePembayaran")
        val _columnIndexOfUangDiterima: Int = getColumnIndexOrThrow(_stmt, "uangDiterima")
        val _columnIndexOfKembalian: Int = getColumnIndexOrThrow(_stmt, "kembalian")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _collectionDetails: LongSparseArray<MutableList<DetailTransaksi>> =
            LongSparseArray<MutableList<DetailTransaksi>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfTransaksiId)
          if (!_collectionDetails.containsKey(_tmpKey)) {
            _collectionDetails.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipdetailTransaksiAscomBekalmuPosDataModelDetailTransaksi(_connection,
            _collectionDetails)
        val _result: MutableList<TransaksiWithDetails> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransaksiWithDetails
          val _tmpTransaksi: Transaksi
          val _tmpTransaksiId: Int
          _tmpTransaksiId = _stmt.getLong(_columnIndexOfTransaksiId).toInt()
          val _tmpNomorOrder: String
          _tmpNomorOrder = _stmt.getText(_columnIndexOfNomorOrder)
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpNamaKasir: String
          _tmpNamaKasir = _stmt.getText(_columnIndexOfNamaKasir)
          val _tmpTotalHarga: Long
          _tmpTotalHarga = _stmt.getLong(_columnIndexOfTotalHarga)
          val _tmpMetodePembayaran: String
          _tmpMetodePembayaran = _stmt.getText(_columnIndexOfMetodePembayaran)
          val _tmpUangDiterima: Long
          _tmpUangDiterima = _stmt.getLong(_columnIndexOfUangDiterima)
          val _tmpKembalian: Long
          _tmpKembalian = _stmt.getLong(_columnIndexOfKembalian)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpTanggal: Long
          _tmpTanggal = _stmt.getLong(_columnIndexOfTanggal)
          _tmpTransaksi =
              Transaksi(_tmpTransaksiId,_tmpNomorOrder,_tmpUserId,_tmpNamaKasir,_tmpTotalHarga,_tmpMetodePembayaran,_tmpUangDiterima,_tmpKembalian,_tmpStatus,_tmpTanggal)
          val _tmpDetailsCollection: MutableList<DetailTransaksi>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfTransaksiId)
          _tmpDetailsCollection = checkNotNull(_collectionDetails.get(_tmpKey_1))
          _item = TransaksiWithDetails(_tmpTransaksi,_tmpDetailsCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTransaksiSince(startDate: Long): LiveData<List<TransaksiWithDetails>> {
    val _sql: String = "SELECT * FROM transaksi WHERE tanggal >= ? ORDER BY tanggal DESC"
    return __db.invalidationTracker.createLiveData(arrayOf("detail_transaksi", "transaksi"), true) {
        _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDate)
        val _columnIndexOfTransaksiId: Int = getColumnIndexOrThrow(_stmt, "transaksiId")
        val _columnIndexOfNomorOrder: Int = getColumnIndexOrThrow(_stmt, "nomorOrder")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfNamaKasir: Int = getColumnIndexOrThrow(_stmt, "namaKasir")
        val _columnIndexOfTotalHarga: Int = getColumnIndexOrThrow(_stmt, "totalHarga")
        val _columnIndexOfMetodePembayaran: Int = getColumnIndexOrThrow(_stmt, "metodePembayaran")
        val _columnIndexOfUangDiterima: Int = getColumnIndexOrThrow(_stmt, "uangDiterima")
        val _columnIndexOfKembalian: Int = getColumnIndexOrThrow(_stmt, "kembalian")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _collectionDetails: LongSparseArray<MutableList<DetailTransaksi>> =
            LongSparseArray<MutableList<DetailTransaksi>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfTransaksiId)
          if (!_collectionDetails.containsKey(_tmpKey)) {
            _collectionDetails.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipdetailTransaksiAscomBekalmuPosDataModelDetailTransaksi(_connection,
            _collectionDetails)
        val _result: MutableList<TransaksiWithDetails> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransaksiWithDetails
          val _tmpTransaksi: Transaksi
          val _tmpTransaksiId: Int
          _tmpTransaksiId = _stmt.getLong(_columnIndexOfTransaksiId).toInt()
          val _tmpNomorOrder: String
          _tmpNomorOrder = _stmt.getText(_columnIndexOfNomorOrder)
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpNamaKasir: String
          _tmpNamaKasir = _stmt.getText(_columnIndexOfNamaKasir)
          val _tmpTotalHarga: Long
          _tmpTotalHarga = _stmt.getLong(_columnIndexOfTotalHarga)
          val _tmpMetodePembayaran: String
          _tmpMetodePembayaran = _stmt.getText(_columnIndexOfMetodePembayaran)
          val _tmpUangDiterima: Long
          _tmpUangDiterima = _stmt.getLong(_columnIndexOfUangDiterima)
          val _tmpKembalian: Long
          _tmpKembalian = _stmt.getLong(_columnIndexOfKembalian)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpTanggal: Long
          _tmpTanggal = _stmt.getLong(_columnIndexOfTanggal)
          _tmpTransaksi =
              Transaksi(_tmpTransaksiId,_tmpNomorOrder,_tmpUserId,_tmpNamaKasir,_tmpTotalHarga,_tmpMetodePembayaran,_tmpUangDiterima,_tmpKembalian,_tmpStatus,_tmpTanggal)
          val _tmpDetailsCollection: MutableList<DetailTransaksi>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfTransaksiId)
          _tmpDetailsCollection = checkNotNull(_collectionDetails.get(_tmpKey_1))
          _item = TransaksiWithDetails(_tmpTransaksi,_tmpDetailsCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTransaksiCountToday(startOfDay: Long, endOfDay: Long): LiveData<Int> {
    val _sql: String =
        "SELECT COUNT(*) FROM transaksi WHERE tanggal >= ? AND tanggal <= ? AND status = 'sukses'"
    return __db.invalidationTracker.createLiveData(arrayOf("transaksi"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startOfDay)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endOfDay)
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

  public override fun getPendapatanToday(startOfDay: Long, endOfDay: Long): LiveData<Long?> {
    val _sql: String =
        "SELECT SUM(totalHarga) FROM transaksi WHERE tanggal >= ? AND tanggal <= ? AND status = 'sukses'"
    return __db.invalidationTracker.createLiveData(arrayOf("transaksi"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startOfDay)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endOfDay)
        val _result: Long?
        if (_stmt.step()) {
          val _tmp: Long?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(0)
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

  public override suspend fun getPendapatanRange(startDate: Long, endDate: Long): Long? {
    val _sql: String =
        "SELECT SUM(totalHarga) FROM transaksi WHERE tanggal >= ? AND tanggal <= ? AND status = 'sukses'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDate)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endDate)
        val _result: Long?
        if (_stmt.step()) {
          val _tmp: Long?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(0)
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

  public override suspend fun getTransaksiById(id: Int): TransaksiWithDetails? {
    val _sql: String = "SELECT * FROM transaksi WHERE transaksiId = ? LIMIT 1"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfTransaksiId: Int = getColumnIndexOrThrow(_stmt, "transaksiId")
        val _columnIndexOfNomorOrder: Int = getColumnIndexOrThrow(_stmt, "nomorOrder")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfNamaKasir: Int = getColumnIndexOrThrow(_stmt, "namaKasir")
        val _columnIndexOfTotalHarga: Int = getColumnIndexOrThrow(_stmt, "totalHarga")
        val _columnIndexOfMetodePembayaran: Int = getColumnIndexOrThrow(_stmt, "metodePembayaran")
        val _columnIndexOfUangDiterima: Int = getColumnIndexOrThrow(_stmt, "uangDiterima")
        val _columnIndexOfKembalian: Int = getColumnIndexOrThrow(_stmt, "kembalian")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _collectionDetails: LongSparseArray<MutableList<DetailTransaksi>> =
            LongSparseArray<MutableList<DetailTransaksi>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfTransaksiId)
          if (!_collectionDetails.containsKey(_tmpKey)) {
            _collectionDetails.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipdetailTransaksiAscomBekalmuPosDataModelDetailTransaksi(_connection,
            _collectionDetails)
        val _result: TransaksiWithDetails?
        if (_stmt.step()) {
          val _tmpTransaksi: Transaksi
          val _tmpTransaksiId: Int
          _tmpTransaksiId = _stmt.getLong(_columnIndexOfTransaksiId).toInt()
          val _tmpNomorOrder: String
          _tmpNomorOrder = _stmt.getText(_columnIndexOfNomorOrder)
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpNamaKasir: String
          _tmpNamaKasir = _stmt.getText(_columnIndexOfNamaKasir)
          val _tmpTotalHarga: Long
          _tmpTotalHarga = _stmt.getLong(_columnIndexOfTotalHarga)
          val _tmpMetodePembayaran: String
          _tmpMetodePembayaran = _stmt.getText(_columnIndexOfMetodePembayaran)
          val _tmpUangDiterima: Long
          _tmpUangDiterima = _stmt.getLong(_columnIndexOfUangDiterima)
          val _tmpKembalian: Long
          _tmpKembalian = _stmt.getLong(_columnIndexOfKembalian)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpTanggal: Long
          _tmpTanggal = _stmt.getLong(_columnIndexOfTanggal)
          _tmpTransaksi =
              Transaksi(_tmpTransaksiId,_tmpNomorOrder,_tmpUserId,_tmpNamaKasir,_tmpTotalHarga,_tmpMetodePembayaran,_tmpUangDiterima,_tmpKembalian,_tmpStatus,_tmpTanggal)
          val _tmpDetailsCollection: MutableList<DetailTransaksi>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfTransaksiId)
          _tmpDetailsCollection = checkNotNull(_collectionDetails.get(_tmpKey_1))
          _result = TransaksiWithDetails(_tmpTransaksi,_tmpDetailsCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDetailsByTransaksiId(transaksiId: Int): List<DetailTransaksi> {
    val _sql: String = "SELECT * FROM detail_transaksi WHERE transaksiId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, transaksiId.toLong())
        val _columnIndexOfDetailId: Int = getColumnIndexOrThrow(_stmt, "detailId")
        val _columnIndexOfTransaksiId: Int = getColumnIndexOrThrow(_stmt, "transaksiId")
        val _columnIndexOfProdukId: Int = getColumnIndexOrThrow(_stmt, "produkId")
        val _columnIndexOfNamaProduk: Int = getColumnIndexOrThrow(_stmt, "namaProduk")
        val _columnIndexOfHargaSatuan: Int = getColumnIndexOrThrow(_stmt, "hargaSatuan")
        val _columnIndexOfJumlah: Int = getColumnIndexOrThrow(_stmt, "jumlah")
        val _columnIndexOfSubtotal: Int = getColumnIndexOrThrow(_stmt, "subtotal")
        val _result: MutableList<DetailTransaksi> = mutableListOf()
        while (_stmt.step()) {
          val _item: DetailTransaksi
          val _tmpDetailId: Int
          _tmpDetailId = _stmt.getLong(_columnIndexOfDetailId).toInt()
          val _tmpTransaksiId: Int
          _tmpTransaksiId = _stmt.getLong(_columnIndexOfTransaksiId).toInt()
          val _tmpProdukId: Int
          _tmpProdukId = _stmt.getLong(_columnIndexOfProdukId).toInt()
          val _tmpNamaProduk: String
          _tmpNamaProduk = _stmt.getText(_columnIndexOfNamaProduk)
          val _tmpHargaSatuan: Long
          _tmpHargaSatuan = _stmt.getLong(_columnIndexOfHargaSatuan)
          val _tmpJumlah: Int
          _tmpJumlah = _stmt.getLong(_columnIndexOfJumlah).toInt()
          val _tmpSubtotal: Long
          _tmpSubtotal = _stmt.getLong(_columnIndexOfSubtotal)
          _item =
              DetailTransaksi(_tmpDetailId,_tmpTransaksiId,_tmpProdukId,_tmpNamaProduk,_tmpHargaSatuan,_tmpJumlah,_tmpSubtotal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTotalHppRange(startDate: Long, endDate: Long): Long {
    val _sql: String = """
        |
        |        SELECT COALESCE(SUM(dt.jumlah * p.hargaBeli), 0)
        |        FROM detail_transaksi dt
        |        INNER JOIN transaksi t ON dt.transaksiId = t.transaksiId
        |        INNER JOIN produk p ON dt.produkId = p.produkId
        |        WHERE t.tanggal >= ? AND t.tanggal <= ? AND t.status = 'sukses'
        |        
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDate)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endDate)
        val _result: Long
        if (_stmt.step()) {
          val _tmp: Long
          _tmp = _stmt.getLong(0)
          _result = _tmp
        } else {
          _result = 0L
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTransaksiSinceSync(startDate: Long, endDate: Long):
      List<TransaksiWithDetails> {
    val _sql: String =
        "SELECT * FROM transaksi WHERE tanggal >= ? AND tanggal <= ? ORDER BY tanggal DESC"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDate)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endDate)
        val _columnIndexOfTransaksiId: Int = getColumnIndexOrThrow(_stmt, "transaksiId")
        val _columnIndexOfNomorOrder: Int = getColumnIndexOrThrow(_stmt, "nomorOrder")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfNamaKasir: Int = getColumnIndexOrThrow(_stmt, "namaKasir")
        val _columnIndexOfTotalHarga: Int = getColumnIndexOrThrow(_stmt, "totalHarga")
        val _columnIndexOfMetodePembayaran: Int = getColumnIndexOrThrow(_stmt, "metodePembayaran")
        val _columnIndexOfUangDiterima: Int = getColumnIndexOrThrow(_stmt, "uangDiterima")
        val _columnIndexOfKembalian: Int = getColumnIndexOrThrow(_stmt, "kembalian")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _collectionDetails: LongSparseArray<MutableList<DetailTransaksi>> =
            LongSparseArray<MutableList<DetailTransaksi>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfTransaksiId)
          if (!_collectionDetails.containsKey(_tmpKey)) {
            _collectionDetails.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipdetailTransaksiAscomBekalmuPosDataModelDetailTransaksi(_connection,
            _collectionDetails)
        val _result: MutableList<TransaksiWithDetails> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransaksiWithDetails
          val _tmpTransaksi: Transaksi
          val _tmpTransaksiId: Int
          _tmpTransaksiId = _stmt.getLong(_columnIndexOfTransaksiId).toInt()
          val _tmpNomorOrder: String
          _tmpNomorOrder = _stmt.getText(_columnIndexOfNomorOrder)
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpNamaKasir: String
          _tmpNamaKasir = _stmt.getText(_columnIndexOfNamaKasir)
          val _tmpTotalHarga: Long
          _tmpTotalHarga = _stmt.getLong(_columnIndexOfTotalHarga)
          val _tmpMetodePembayaran: String
          _tmpMetodePembayaran = _stmt.getText(_columnIndexOfMetodePembayaran)
          val _tmpUangDiterima: Long
          _tmpUangDiterima = _stmt.getLong(_columnIndexOfUangDiterima)
          val _tmpKembalian: Long
          _tmpKembalian = _stmt.getLong(_columnIndexOfKembalian)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpTanggal: Long
          _tmpTanggal = _stmt.getLong(_columnIndexOfTanggal)
          _tmpTransaksi =
              Transaksi(_tmpTransaksiId,_tmpNomorOrder,_tmpUserId,_tmpNamaKasir,_tmpTotalHarga,_tmpMetodePembayaran,_tmpUangDiterima,_tmpKembalian,_tmpStatus,_tmpTanggal)
          val _tmpDetailsCollection: MutableList<DetailTransaksi>
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfTransaksiId)
          _tmpDetailsCollection = checkNotNull(_collectionDetails.get(_tmpKey_1))
          _item = TransaksiWithDetails(_tmpTransaksi,_tmpDetailsCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  private
      fun __fetchRelationshipdetailTransaksiAscomBekalmuPosDataModelDetailTransaksi(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<DetailTransaksi>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshipdetailTransaksiAscomBekalmuPosDataModelDetailTransaksi(_connection,
            _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `detailId`,`transaksiId`,`produkId`,`namaProduk`,`hargaSatuan`,`jumlah`,`subtotal` FROM `detail_transaksi` WHERE `transaksiId` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "transaksiId")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfDetailId: Int = 0
      val _columnIndexOfTransaksiId: Int = 1
      val _columnIndexOfProdukId: Int = 2
      val _columnIndexOfNamaProduk: Int = 3
      val _columnIndexOfHargaSatuan: Int = 4
      val _columnIndexOfJumlah: Int = 5
      val _columnIndexOfSubtotal: Int = 6
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<DetailTransaksi>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: DetailTransaksi
          val _tmpDetailId: Int
          _tmpDetailId = _stmt.getLong(_columnIndexOfDetailId).toInt()
          val _tmpTransaksiId: Int
          _tmpTransaksiId = _stmt.getLong(_columnIndexOfTransaksiId).toInt()
          val _tmpProdukId: Int
          _tmpProdukId = _stmt.getLong(_columnIndexOfProdukId).toInt()
          val _tmpNamaProduk: String
          _tmpNamaProduk = _stmt.getText(_columnIndexOfNamaProduk)
          val _tmpHargaSatuan: Long
          _tmpHargaSatuan = _stmt.getLong(_columnIndexOfHargaSatuan)
          val _tmpJumlah: Int
          _tmpJumlah = _stmt.getLong(_columnIndexOfJumlah).toInt()
          val _tmpSubtotal: Long
          _tmpSubtotal = _stmt.getLong(_columnIndexOfSubtotal)
          _item_1 =
              DetailTransaksi(_tmpDetailId,_tmpTransaksiId,_tmpProdukId,_tmpNamaProduk,_tmpHargaSatuan,_tmpJumlah,_tmpSubtotal)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
