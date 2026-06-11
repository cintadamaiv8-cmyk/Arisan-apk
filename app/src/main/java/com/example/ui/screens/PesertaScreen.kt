package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.database.PesertaEntity
import com.example.viewmodel.MaskArisanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesertaScreen(viewModel: MaskArisanViewModel) {
    val pesertaList by viewModel.allPeserta.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var currentPeserta by remember { mutableStateOf<PesertaEntity?>(null) }
    var namaInput by remember { mutableStateOf("") }
    
    var showDeleteConfirm by remember { mutableStateOf<PesertaEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { currentPeserta = null; namaInput = ""; showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Peserta")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(pesertaList) { peserta ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(peserta.nama, style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (peserta.statusAktif) "Aktif" else "Nonaktif",
                                color = if (peserta.statusAktif) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }
                        Switch(
                            checked = peserta.statusAktif,
                            onCheckedChange = { viewModel.toggleStatusAktif(peserta) }
                        )
                        IconButton(onClick = { currentPeserta = peserta; namaInput = peserta.nama; showDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { showDeleteConfirm = peserta }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (currentPeserta == null) "Tambah Peserta" else "Edit Peserta") },
            text = {
                OutlinedTextField(
                    value = namaInput,
                    onValueChange = { namaInput = it },
                    label = { Text("Nama") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("nama_input")
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (namaInput.isNotBlank()) {
                        if (currentPeserta == null) {
                            viewModel.addPeserta(namaInput)
                        } else {
                            viewModel.updatePeserta(currentPeserta!!.copy(nama = namaInput))
                        }
                        showDialog = false
                    }
                }, modifier = Modifier.testTag("simpan_peserta")) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Batal") }
            }
        )
    }

    showDeleteConfirm?.let { p ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Hapus Peserta") },
            text = { Text("Apakah Anda yakin ingin menghapus ${p.nama}?") },
            confirmButton = {
                Button(onClick = { viewModel.deletePeserta(p); showDeleteConfirm = null }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) { Text("Batal") }
            }
        )
    }
}
