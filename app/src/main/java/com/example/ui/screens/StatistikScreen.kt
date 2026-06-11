package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viewmodel.MaskArisanViewModel

@Composable
fun StatistikScreen(viewModel: MaskArisanViewModel) {
    val pesertaAktif by viewModel.countPesertaAktif.collectAsState()
    val sudahKeluar by viewModel.countPesertaSudahKeluar.collectAsState()
    val belumKeluar = pesertaAktif - sudahKeluar
    
    val allSetoran by viewModel.allSetoran.collectAsState()
    val sudahSetor = allSetoran.count { it.statusSetor }
    val belumSetor = allSetoran.count { !it.statusSetor }
    val putaran by viewModel.countJumlahPutaran.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Peserta", style = MaterialTheme.typography.titleMedium)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Aktif: $pesertaAktif")
                    Text("Sudah Keluar: $sudahKeluar")
                    Text("Belum Keluar: $belumKeluar")
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Setoran", style = MaterialTheme.typography.titleMedium)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Total Sudah Setor: $sudahSetor")
                    Text("Total Belum Setor: $belumSetor")
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Lainnya", style = MaterialTheme.typography.titleMedium)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Jumlah Putaran Dilalui: $putaran")
                }
            }
        }
    }
}
