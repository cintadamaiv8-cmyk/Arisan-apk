package com.example.data.repository

import com.example.data.database.AuditLogDao
import com.example.data.database.AuditLogEntity
import com.example.data.database.PengaturanDao
import com.example.data.database.PengaturanEntity
import com.example.data.database.PesertaDao
import com.example.data.database.PesertaEntity
import com.example.data.database.RiwayatDao
import com.example.data.database.RiwayatEntity
import com.example.data.database.SetoranDao
import com.example.data.database.SetoranEntity
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val pesertaDao: PesertaDao,
    private val setoranDao: SetoranDao,
    private val riwayatDao: RiwayatDao,
    private val auditLogDao: AuditLogDao,
    private val pengaturanDao: PengaturanDao
) {

    // Peserta
    val allPeserta: Flow<List<PesertaEntity>> = pesertaDao.getAllPeserta()
    val pesertaAktif: Flow<List<PesertaEntity>> = pesertaDao.getPesertaAktif()
    val pesertaEligible: Flow<List<PesertaEntity>> = pesertaDao.getPesertaEligible()
    val countPesertaAktif: Flow<Int> = pesertaDao.countPesertaAktif()
    val countPesertaSudahMenang: Flow<Int> = pesertaDao.countPesertaSudahMenang()

    suspend fun insertPeserta(peserta: PesertaEntity): Long = pesertaDao.insertPeserta(peserta)
    suspend fun updatePeserta(peserta: PesertaEntity) = pesertaDao.updatePeserta(peserta)
    suspend fun deletePesertaById(id: Int) = pesertaDao.deletePesertaById(id)
    suspend fun resetStatusMenangPeserta() = pesertaDao.resetStatusMenang()
    fun searchPeserta(query: String): Flow<List<PesertaEntity>> = pesertaDao.searchPeserta(query)

    // Setoran
    val allSetoran: Flow<List<SetoranEntity>> = setoranDao.getAllSetoran()
    val belumSetor: Flow<List<SetoranEntity>> = setoranDao.getBelumSetor()

    suspend fun insertSetoran(setoran: SetoranEntity) = setoranDao.insertSetoran(setoran)
    suspend fun updateSetoran(setoran: SetoranEntity) = setoranDao.updateSetoran(setoran)
    suspend fun deleteSetoranById(id: Int) = setoranDao.deleteSetoranById(id)

    fun countSudahSetorMingguIni(startOfWeek: Long, endOfWeek: Long): Flow<Int> {
        return setoranDao.countSudahSetorMingguIni(startOfWeek, endOfWeek)
    }

    fun sumKasMingguIni(startOfWeek: Long, endOfWeek: Long): Flow<Double?> {
        return setoranDao.sumKasMingguIni(startOfWeek, endOfWeek)
    }
    
    suspend fun getSumKasMingguIniSync(startOfWeek: Long, endOfWeek: Long): Double {
        return setoranDao.getSumKasMingguIniSync(startOfWeek, endOfWeek) ?: 0.0
    }

    // Riwayat
    val allRiwayat: Flow<List<RiwayatEntity>> = riwayatDao.getAllRiwayat()
    val countJumlahPutaran: Flow<Int> = riwayatDao.countJumlahPutaran()

    suspend fun insertRiwayat(riwayat: RiwayatEntity) = riwayatDao.insertRiwayat(riwayat)
    fun searchRiwayat(query: String): Flow<List<RiwayatEntity>> = riwayatDao.searchRiwayat(query)

    // Audit Log
    val allAuditLog: Flow<List<AuditLogEntity>> = auditLogDao.getAllAuditLog()
    suspend fun insertAuditLog(auditLog: AuditLogEntity) = auditLogDao.insertAuditLog(auditLog)

    // Pengaturan
    val pengaturan: Flow<PengaturanEntity?> = pengaturanDao.getPengaturan()
    suspend fun insertPengaturan(pengaturan: PengaturanEntity) = pengaturanDao.insertPengaturan(pengaturan)
    suspend fun updateNomorPutaran(putaran: Int) = pengaturanDao.updateNomorPutaran(putaran)
}
