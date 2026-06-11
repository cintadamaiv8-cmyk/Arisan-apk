package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.database.PesertaEntity
import com.example.data.database.SetoranEntity
import com.example.utils.CurrencyUtils
import com.example.utils.DateUtils
import com.example.viewmodel.MaskArisanViewModel

@Composable
fun SetoranScreen(viewModel: MaskArisanViewModel) {
    val setoranList by viewModel.allSetoran.collectAsState()
    val pesertaAktif by viewModel.pesertaAktif.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Setoran")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            items(setoranList) { setoran ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Peserta ID: ${setoran.pesertaId}", style = MaterialTheme.typography.titleMedium)
                        Text("Tanggal: ${DateUtils.formatTanggal(setoran.tanggal)}")
                        if (setoran.statusSetor) {
                            Text("Setor: ${CurrencyUtils.formatRupiah(setoran.nominal)}", color = MaterialTheme.colorScheme.primary)
                        } else {
                            Text("Belum Setor - Alasan: ${setoran.alasan}", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddSetoranDialog(
            pesertaList = pesertaAktif,
            onDismiss = { showDialog = false },
            onSave = { pesertaId, nominal, status, alasan ->
                viewModel.addSetoran(pesertaId, nominal, status, alasan)
                showDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSetoranDialog(pesertaList: List<PesertaEntity>, onDismiss: () -> Unit, onSave: (Int, Double, Boolean, String) -> Unit) {
    var selectedPeserta by remember { mutableStateOf<PesertaEntity?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var nominalInput by remember { mutableStateOf("") }
    var statusSetor by remember { mutableStateOf(true) }
    var alasanInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Setoran") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Dropdown logic via ExposedDropdownMenuBox
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedPeserta?.nama ?: "Pilih Peserta",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        pesertaList.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.nama) },
                                onClick = { selectedPeserta = p; expanded = false }
                            )
                        }
                    }
                }
                
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked = statusSetor, onCheckedChange = { statusSetor = it })
                    Text("Sudah Setor")
                }

                if (statusSetor) {
                    OutlinedTextField(
                        value = nominalInput,
                        onValueChange = { nominalInput = it },
                        label = { Text("Nominal (Rp)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = alasanInput,
                        onValueChange = { alasanInput = it },
                        label = { Text("Alasan Wajib Diisi") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (selectedPeserta != null) {
                    if (statusSetor) {
                        onSave(selectedPeserta!!.id, nominalInput.toDoubleOrNull() ?: 0.0, true, "")
                    } else if (alasanInput.isNotBlank()) {
                        onSave(selectedPeserta!!.id, 0.0, false, alasanInput)
                    }
                }
            }) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
