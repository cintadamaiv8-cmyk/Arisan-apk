package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.AuditLogEntity
import com.example.data.database.PengaturanEntity
import com.example.data.database.PesertaEntity
import com.example.data.database.RiwayatEntity
import com.example.data.database.SetoranEntity
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MaskArisanViewModel(private val repository: AppRepository) : ViewModel() {

    val allPeserta = repository.allPeserta.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val pesertaAktif = repository.pesertaAktif.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val pesertaEligible = repository.pesertaEligible.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val countPesertaAktif = repository.countPesertaAktif.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val countPesertaSudahKeluar = repository.countPesertaSudahKeluar.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allSetoran = repository.allSetoran.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val belumSetor = repository.belumSetor.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRiwayat = repository.allRiwayat.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val countJumlahPutaran = repository.countJumlahPutaran.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allAuditLog = repository.allAuditLog.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val pengaturan = repository.pengaturan.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PengaturanEntity(1, 1, 0.0))

    fun sumKasMingguIni(start: Long, end: Long) = repository.sumKasMingguIni(start, end)
    fun countSudahSetorMingguIni(start: Long, end: Long) = repository.countSudahSetorMingguIni(start, end)

    init {
        viewModelScope.launch {
            if (repository.pengaturan.first() == null) {
                repository.insertPengaturan(PengaturanEntity(1, 1, 0.0))
            }
        }
    }

    // Peserta
    fun addPeserta(nama: String) = viewModelScope.launch {
        repository.insertPeserta(PesertaEntity(nama = nama))
        logAudit("Tambah Peserta", "Menambahkan peserta baru bernama $nama")
    }

    fun updatePeserta(peserta: PesertaEntity) = viewModelScope.launch {
        repository.updatePeserta(peserta)
        logAudit("Edit Peserta", "Mengubah data peserta: ${peserta.nama}")
    }

    fun toggleStatusAktif(peserta: PesertaEntity) = viewModelScope.launch {
        repository.updatePeserta(peserta.copy(statusAktif = !peserta.statusAktif))
    }

    fun deletePeserta(peserta: PesertaEntity) = viewModelScope.launch {
        repository.deletePesertaById(peserta.id)
        logAudit("Hapus Peserta", "Menghapus peserta: ${peserta.nama}")
    }

    // Setoran
    fun addSetoran(pesertaId: Int, nominal: Double, status: Boolean, alasan: String = "") = viewModelScope.launch {
        repository.insertSetoran(SetoranEntity(
            pesertaId = pesertaId,
            nominal = nominal,
            tanggal = System.currentTimeMillis(),
            statusSetor = status,
            alasan = alasan
        ))
        val pesan = if (status) "Setoran Rp$nominal masuk" else "Belum setor, alasan: $alasan"
        logAudit("Tambah Setoran", "ID Peserta $pesertaId: $pesan")
    }

    fun updateSetoran(setoran: SetoranEntity) = viewModelScope.launch {
        repository.updateSetoran(setoran)
        logAudit("Ubah Setoran", "ID Setoran ${setoran.id} diubah.")
    }

    // Arisan (Mulai & Putaran Baru)
    fun setPemenangArisan(pemenang: PesertaEntity, nominal: Double) = viewModelScope.launch {
        val putaranId = pengaturan.value?.nomorPutaran ?: 1
        val pesertaCount = countPesertaAktif.value

        // Update winner
        val updatedWinner = pemenang.copy(statusKeluar = true, tanggalKeluar = System.currentTimeMillis())
        repository.updatePeserta(updatedWinner)

        // Add history
        repository.insertRiwayat(RiwayatEntity(
            namaPenerima = pemenang.nama,
            tanggal = System.currentTimeMillis(),
            nominalArisan = nominal,
            jumlahPesertaAktif = pesertaCount,
            nomorPutaran = putaranId
        ))
        
        logAudit("Pemenang Arisan", "Peserta ${pemenang.nama} memenangkan arisan (Putaran $putaranId)")
    }

    fun mulaiPutaranBaru() = viewModelScope.launch {
        val nextPutaran = (pengaturan.value?.nomorPutaran ?: 1) + 1
        repository.updateNomorPutaran(nextPutaran)
        repository.resetStatusKeluarPeserta()

        logAudit("Putaran Baru", "Memulai Putaran Arisan ke-$nextPutaran")
    }

    fun logAudit(aksi: String, detail: String) = viewModelScope.launch {
        repository.insertAuditLog(AuditLogEntity(aksi = aksi, detail = detail))
    }
}

class MaskArisanViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MaskArisanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MaskArisanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
