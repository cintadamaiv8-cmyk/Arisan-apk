package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.utils.DateUtils
import com.example.viewmodel.MaskArisanViewModel

@Composable
fun BelumSetorScreen(viewModel: MaskArisanViewModel) {
    val belumSetorList by viewModel.belumSetor.collectAsState()
    val allPeserta by viewModel.allPeserta.collectAsState()
    val pesertaMap = allPeserta.associateBy { it.id }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (belumSetorList.isEmpty()) {
            item {
                Text("Semua peserta sudah setor atau tidak ada catatan belum setor.")
            }
        }
        items(belumSetorList) { setoran ->
            val pName = pesertaMap[setoran.pesertaId]?.nama ?: "Unknown"
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(pName, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                    Text("Tanggal: ${DateUtils.formatTanggal(setoran.tanggal)}", color = MaterialTheme.colorScheme.onErrorContainer)
                    Text("Alasan: ${setoran.alasan}", color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }
    }
}
