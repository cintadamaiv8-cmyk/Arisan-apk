package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peserta")
data class PesertaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val statusAktif: Boolean = true,
    val statusMenang: Boolean = false,
    val tanggalMenang: Long? = null
)

@Entity(tableName = "setoran")
data class SetoranEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pesertaId: Int,
    val nominal: Double,
    val tanggal: Long,
    val statusSetor: Boolean,
    val alasan: String = ""
)

@Entity(tableName = "riwayat")
data class RiwayatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaPenerima: String,
    val tanggal: Long,
    val nominalArisan: Double,
    val jumlahPesertaAktif: Int,
    val nomorPutaran: Int
)

@Entity(tableName = "audit_log")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val aksi: String,
    val detail: String,
    val tanggal: Long = System.currentTimeMillis()
)

@Entity(tableName = "pengaturan")
data class PengaturanEntity(
    @PrimaryKey val id: Int = 1,
    val nomorPutaran: Int = 1,
    val jumlahUangKas: Double = 0.0
)
