package com.bekalmu.pos.`data`.dao

import androidx.lifecycle.LiveData
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.bekalmu.pos.`data`.model.Kas
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

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class KasDao_Impl(
  __db: RoomDatabase,
) : KasDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfKas: EntityInsertAdapter<Kas>

  private val __deleteAdapterOfKas: EntityDeleteOrUpdateAdapter<Kas>

  private val __updateAdapterOfKas: EntityDeleteOrUpdateAdapter<Kas>
  init {
    this.__db = __db
    this.__insertAdapterOfKas = object : EntityInsertAdapter<Kas>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `kas` (`kasId`,`userId`,`jenis`,`kategori`,`jumlah`,`catatan`,`tanggal`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Kas) {
        statement.bindLong(1, entity.kasId.toLong())
        statement.bindLong(2, entity.userId.toLong())
        statement.bindText(3, entity.jenis)
        statement.bindText(4, entity.kategori)
        statement.bindLong(5, entity.jumlah)
        statement.bindText(6, entity.catatan)
        statement.bindLong(7, entity.tanggal)
      }
    }
    this.__deleteAdapterOfKas = object : EntityDeleteOrUpdateAdapter<Kas>() {
      protected override fun createQuery(): String = "DELETE FROM `kas` WHERE `kasId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Kas) {
        statement.bindLong(1, entity.kasId.toLong())
      }
    }
    this.__updateAdapterOfKas = object : EntityDeleteOrUpdateAdapter<Kas>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `kas` SET `kasId` = ?,`userId` = ?,`jenis` = ?,`kategori` = ?,`jumlah` = ?,`catatan` = ?,`tanggal` = ? WHERE `kasId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Kas) {
        statement.bindLong(1, entity.kasId.toLong())
        statement.bindLong(2, entity.userId.toLong())
        statement.bindText(3, entity.jenis)
        statement.bindText(4, entity.kategori)
        statement.bindLong(5, entity.jumlah)
        statement.bindText(6, entity.catatan)
        statement.bindLong(7, entity.tanggal)
        statement.bindLong(8, entity.kasId.toLong())
      }
    }
  }

  public override suspend fun insert(kas: Kas): Long = performSuspending(__db, false, true) {
      _connection ->
    val _result: Long = __insertAdapterOfKas.insertAndReturnId(_connection, kas)
    _result
  }

  public override suspend fun delete(kas: Kas): Unit = performSuspending(__db, false, true) {
      _connection ->
    __deleteAdapterOfKas.handle(_connection, kas)
  }

  public override suspend fun update(kas: Kas): Unit = performSuspending(__db, false, true) {
      _connection ->
    __updateAdapterOfKas.handle(_connection, kas)
  }

  public override fun getAllKas(): LiveData<List<Kas>> {
    val _sql: String = "SELECT * FROM kas ORDER BY tanggal DESC"
    return __db.invalidationTracker.createLiveData(arrayOf("kas"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfKasId: Int = getColumnIndexOrThrow(_stmt, "kasId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfJenis: Int = getColumnIndexOrThrow(_stmt, "jenis")
        val _columnIndexOfKategori: Int = getColumnIndexOrThrow(_stmt, "kategori")
        val _columnIndexOfJumlah: Int = getColumnIndexOrThrow(_stmt, "jumlah")
        val _columnIndexOfCatatan: Int = getColumnIndexOrThrow(_stmt, "catatan")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _result: MutableList<Kas> = mutableListOf()
        while (_stmt.step()) {
          val _item: Kas
          val _tmpKasId: Int
          _tmpKasId = _stmt.getLong(_columnIndexOfKasId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpJenis: String
          _tmpJenis = _stmt.getText(_columnIndexOfJenis)
          val _tmpKategori: String
          _tmpKategori = _stmt.getText(_columnIndexOfKategori)
          val _tmpJumlah: Long
          _tmpJumlah = _stmt.getLong(_columnIndexOfJumlah)
          val _tmpCatatan: String
          _tmpCatatan = _stmt.getText(_columnIndexOfCatatan)
          val _tmpTanggal: Long
          _tmpTanggal = _stmt.getLong(_columnIndexOfTanggal)
          _item =
              Kas(_tmpKasId,_tmpUserId,_tmpJenis,_tmpKategori,_tmpJumlah,_tmpCatatan,_tmpTanggal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getKasByJenis(jenis: String): LiveData<List<Kas>> {
    val _sql: String = "SELECT * FROM kas WHERE jenis = ? ORDER BY tanggal DESC"
    return __db.invalidationTracker.createLiveData(arrayOf("kas"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, jenis)
        val _columnIndexOfKasId: Int = getColumnIndexOrThrow(_stmt, "kasId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfJenis: Int = getColumnIndexOrThrow(_stmt, "jenis")
        val _columnIndexOfKategori: Int = getColumnIndexOrThrow(_stmt, "kategori")
        val _columnIndexOfJumlah: Int = getColumnIndexOrThrow(_stmt, "jumlah")
        val _columnIndexOfCatatan: Int = getColumnIndexOrThrow(_stmt, "catatan")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _result: MutableList<Kas> = mutableListOf()
        while (_stmt.step()) {
          val _item: Kas
          val _tmpKasId: Int
          _tmpKasId = _stmt.getLong(_columnIndexOfKasId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpJenis: String
          _tmpJenis = _stmt.getText(_columnIndexOfJenis)
          val _tmpKategori: String
          _tmpKategori = _stmt.getText(_columnIndexOfKategori)
          val _tmpJumlah: Long
          _tmpJumlah = _stmt.getLong(_columnIndexOfJumlah)
          val _tmpCatatan: String
          _tmpCatatan = _stmt.getText(_columnIndexOfCatatan)
          val _tmpTanggal: Long
          _tmpTanggal = _stmt.getLong(_columnIndexOfTanggal)
          _item =
              Kas(_tmpKasId,_tmpUserId,_tmpJenis,_tmpKategori,_tmpJumlah,_tmpCatatan,_tmpTanggal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalMasuk(): LiveData<Long?> {
    val _sql: String = "SELECT SUM(jumlah) FROM kas WHERE jenis = 'masuk'"
    return __db.invalidationTracker.createLiveData(arrayOf("kas"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override fun getTotalKeluar(): LiveData<Long?> {
    val _sql: String = "SELECT SUM(jumlah) FROM kas WHERE jenis = 'keluar'"
    return __db.invalidationTracker.createLiveData(arrayOf("kas"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override suspend fun getTotalMasukRange(startDate: Long, endDate: Long): Long? {
    val _sql: String =
        "SELECT SUM(jumlah) FROM kas WHERE jenis = 'masuk' AND tanggal >= ? AND tanggal <= ?"
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

  public override suspend fun getTotalKeluarRange(startDate: Long, endDate: Long): Long? {
    val _sql: String =
        "SELECT SUM(jumlah) FROM kas WHERE jenis = 'keluar' AND tanggal >= ? AND tanggal <= ?"
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

  public override fun getKasByDateRange(startDate: Long, endDate: Long): LiveData<List<Kas>> {
    val _sql: String = "SELECT * FROM kas WHERE tanggal >= ? AND tanggal <= ? ORDER BY tanggal DESC"
    return __db.invalidationTracker.createLiveData(arrayOf("kas"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDate)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endDate)
        val _columnIndexOfKasId: Int = getColumnIndexOrThrow(_stmt, "kasId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfJenis: Int = getColumnIndexOrThrow(_stmt, "jenis")
        val _columnIndexOfKategori: Int = getColumnIndexOrThrow(_stmt, "kategori")
        val _columnIndexOfJumlah: Int = getColumnIndexOrThrow(_stmt, "jumlah")
        val _columnIndexOfCatatan: Int = getColumnIndexOrThrow(_stmt, "catatan")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _result: MutableList<Kas> = mutableListOf()
        while (_stmt.step()) {
          val _item: Kas
          val _tmpKasId: Int
          _tmpKasId = _stmt.getLong(_columnIndexOfKasId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId).toInt()
          val _tmpJenis: String
          _tmpJenis = _stmt.getText(_columnIndexOfJenis)
          val _tmpKategori: String
          _tmpKategori = _stmt.getText(_columnIndexOfKategori)
          val _tmpJumlah: Long
          _tmpJumlah = _stmt.getLong(_columnIndexOfJumlah)
          val _tmpCatatan: String
          _tmpCatatan = _stmt.getText(_columnIndexOfCatatan)
          val _tmpTanggal: Long
          _tmpTanggal = _stmt.getLong(_columnIndexOfTanggal)
          _item =
              Kas(_tmpKasId,_tmpUserId,_tmpJenis,_tmpKategori,_tmpJumlah,_tmpCatatan,_tmpTanggal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
