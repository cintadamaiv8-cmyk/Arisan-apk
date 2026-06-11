package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.utils.DateUtils
import com.example.viewmodel.MaskArisanViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EksporRiwayatScreen(viewModel: MaskArisanViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val allRiwayat by viewModel.allRiwayat.collectAsState()
    val belumSetorList by viewModel.belumSetor.collectAsState()
    val allPeserta by viewModel.allPeserta.collectAsState()

    var exportContent by remember { mutableStateOf("") }

    val exportTxtLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri ->
        if (uri != null) {
            scope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(exportContent.toByteArray())
                        }
                    }
                    Toast.makeText(context, "Berhasil ekspor ke TXT", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Gagal ekspor: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val exportCsvLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
        if (uri != null) {
            scope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(exportContent.toByteArray())
                        }
                    }
                    Toast.makeText(context, "Berhasil ekspor ke CSV", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Gagal ekspor: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ekspor Riwayat Arisan", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Ekspor riwayat pemenang untuk 5 putaran terakhir beserta data peserta yang belum melakukan setoran.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Generate TXT Content
                val sb = StringBuilder()
                sb.append("=== Laporan Arisan (5 Siklus Terakhir) ===\n\n")
                
                val last5Riwayat = allRiwayat.sortedByDescending { it.tanggal }.take(5)
                if (last5Riwayat.isEmpty()) {
                    sb.append("Belum ada riwayat pemenang arisan.\n\n")
                } else {
                    sb.append("Riwayat Pemenang Terakhir:\n")
                    last5Riwayat.forEach { r ->
                        sb.append("- Putaran: ${r.nomorPutaran}\n")
                        sb.append("  Pemenang: ${r.namaPenerima}\n")
                        sb.append("  Tanggal: ${DateUtils.formatTanggal(r.tanggal)}\n")
                        sb.append("  Nominal: Rp ${r.nominalArisan.toLong()}\n\n")
                    }
                }
                
                sb.append("=== Data Peserta Belum Setor ===\n\n")
                if (belumSetorList.isEmpty()) {
                    sb.append("Semua peserta sudah membayar setoran (Tidak ada tunggakan).\n")
                } else {
                    belumSetorList.forEach { s ->
                        val nama = allPeserta.find { it.id == s.pesertaId }?.nama ?: "Peserta (ID: ${s.pesertaId})"
                        sb.append("- $nama (Nominal: Rp ${s.nominal.toLong()})\n")
                        if (s.alasan.isNotBlank()) {
                            sb.append("  Keterangan: ${s.alasan}\n")
                        }
                    }
                }
                
                exportContent = sb.toString()
                exportTxtLauncher.launch("Laporan_Arisan_${System.currentTimeMillis()}.txt")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan sebagai Teks (.txt)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Generate CSV Content
                val sb = StringBuilder()
                sb.append("Kategori,Nama,Tanggal,Keterangan,Nominal\n")
                
                val last5Riwayat = allRiwayat.sortedByDescending { it.tanggal }.take(5)
                last5Riwayat.forEach { r ->
                    sb.append("Pemenang Putaran ${r.nomorPutaran},${r.namaPenerima},${DateUtils.formatTanggal(r.tanggal)},-,${r.nominalArisan.toLong()}\n")
                }
                
                belumSetorList.forEach { s ->
                    val nama = allPeserta.find { it.id == s.pesertaId }?.nama ?: "Peserta ${s.pesertaId}"
                    sb.append("Belum Setor,$nama,${DateUtils.formatTanggal(s.tanggal)},${s.alasan.ifBlank { "-" }},${s.nominal.toLong()}\n")
                }
                
                exportContent = sb.toString()
                exportCsvLauncher.launch("Laporan_Arisan_${System.currentTimeMillis()}.csv")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan sebagai CSV (.csv)")
        }
    }
}
