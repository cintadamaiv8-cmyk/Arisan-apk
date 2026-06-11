package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PesertaDao {
    @Query("SELECT * FROM peserta ORDER BY nama ASC")
    fun getAllPeserta(): Flow<List<PesertaEntity>>

    @Query("SELECT * FROM peserta WHERE statusAktif = 1")
    fun getPesertaAktif(): Flow<List<PesertaEntity>>

    @Query("SELECT * FROM peserta WHERE statusAktif = 1 AND statusKeluar = 0")
    fun getPesertaEligible(): Flow<List<PesertaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeserta(peserta: PesertaEntity): Long

    @Update
    suspend fun updatePeserta(peserta: PesertaEntity)

    @Query("DELETE FROM peserta WHERE id = :id")
    suspend fun deletePesertaById(id: Int)

    @Query("SELECT COUNT(*) FROM peserta WHERE statusAktif = 1")
    fun countPesertaAktif(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM peserta WHERE statusKeluar = 1")
    fun countPesertaSudahKeluar(): Flow<Int>

    @Query("UPDATE peserta SET statusKeluar = 0, tanggalKeluar = null")
    suspend fun resetStatusKeluar()

    @Query("SELECT * FROM peserta WHERE nama LIKE '%' || :query || '%'")
    fun searchPeserta(query: String): Flow<List<PesertaEntity>>
}

@Dao
interface SetoranDao {
    @Query("SELECT * FROM setoran ORDER BY tanggal DESC")
    fun getAllSetoran(): Flow<List<SetoranEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetoran(setoran: SetoranEntity)

    @Update
    suspend fun updateSetoran(setoran: SetoranEntity)

    @Query("DELETE FROM setoran WHERE id = :id")
    suspend fun deleteSetoranById(id: Int)
    
    @Query("SELECT * FROM setoran WHERE statusSetor = 0")
    fun getBelumSetor(): Flow<List<SetoranEntity>>
    
    @Query("SELECT COUNT(*) FROM setoran WHERE statusSetor = 1 AND tanggal >= :startOfWeek AND tanggal <= :endOfWeek")
    fun countSudahSetorMingguIni(startOfWeek: Long, endOfWeek: Long): Flow<Int>
    
    @Query("SELECT SUM(nominal) FROM setoran WHERE statusSetor = 1 AND tanggal >= :startOfWeek AND tanggal <= :endOfWeek")
    fun sumKasMingguIni(startOfWeek: Long, endOfWeek: Long): Flow<Double?>
}

@Dao
interface RiwayatDao {
    @Query("SELECT * FROM riwayat ORDER BY tanggal DESC")
    fun getAllRiwayat(): Flow<List<RiwayatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRiwayat(riwayat: RiwayatEntity)

    @Query("SELECT * FROM riwayat WHERE namaPenerima LIKE '%' || :query || '%'")
    fun searchRiwayat(query: String): Flow<List<RiwayatEntity>>
    
    @Query("SELECT COUNT(DISTINCT nomorPutaran) FROM riwayat")
    fun countJumlahPutaran(): Flow<Int>
}

@Dao
interface AuditLogDao {
    @Query("SELECT * FROM audit_log ORDER BY tanggal DESC")
    fun getAllAuditLog(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(auditLog: AuditLogEntity)
}

@Dao
interface PengaturanDao {
    @Query("SELECT * FROM pengaturan WHERE id = 1")
    fun getPengaturan(): Flow<PengaturanEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPengaturan(pengaturan: PengaturanEntity)

    @Query("UPDATE pengaturan SET nomorPutaran = :putaran WHERE id = 1")
    suspend fun updateNomorPutaran(putaran: Int)
}
