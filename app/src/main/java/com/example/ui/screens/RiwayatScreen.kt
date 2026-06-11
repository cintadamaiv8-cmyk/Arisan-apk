package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.utils.CurrencyUtils
import com.example.utils.DateUtils
import com.example.viewmodel.MaskArisanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen(viewModel: MaskArisanViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val riwayatList by viewModel.allRiwayat.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Cari Riwayat") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        val filtered = riwayatList.filter { it.namaPenerima.contains(searchQuery, ignoreCase = true) }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            items(filtered) { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pemenang: ${item.namaPenerima}", style = MaterialTheme.typography.titleMedium)
                        Text("Tanggal: ${DateUtils.formatTanggal(item.tanggal)}")
                        Text("Nominal: ${CurrencyUtils.formatRupiah(item.nominalArisan)}")
                        Text("Putaran ke-${item.nomorPutaran} | ${item.jumlahPesertaAktif} Peserta Aktif")
                    }
                }
            }
        }
    }
}
