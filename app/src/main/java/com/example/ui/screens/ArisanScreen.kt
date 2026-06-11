package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.PesertaEntity
import com.example.utils.DateUtils
import com.example.viewmodel.MaskArisanViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArisanScreen(viewModel: MaskArisanViewModel) {
    val pesertaEligible by viewModel.pesertaEligible.collectAsState()
    val scope = rememberCoroutineScope()
    
    var isShuffling by remember { mutableStateOf(false) }
    var displayedName by remember { mutableStateOf("Siapa selanjutnya?") }
    var winner by remember { mutableStateOf<PesertaEntity?>(null) }
    var showWinnerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("PENGUNDIAN ARISAN", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.size(250.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = displayedName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (pesertaEligible.isEmpty()) {
            Text("Semua peserta aktif sudah mendapatkan arisan.", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.mulaiPutaranBaru() }) {
                Text("MULAI PUTARAN BARU")
            }
        } else {
            Button(
                enabled = !isShuffling,
                onClick = {
                    if (pesertaEligible.isNotEmpty()) {
                        isShuffling = true
                        winner = null
                        scope.launch {
                            val duration = Random.nextLong(3000, 5000)
                            val endTime = System.currentTimeMillis() + duration
                            while (System.currentTimeMillis() < endTime) {
                                displayedName = pesertaEligible.random().nama
                                delay(100)
                            }
                            winner = pesertaEligible.random()
                            displayedName = winner!!.nama
                            isShuffling = false
                            showWinnerDialog = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(if (isShuffling) "MENGACAK..." else "MULAI ARISAN", fontSize = 18.sp)
            }
        }
    }

    if (showWinnerDialog && winner != null) {
        var nominalInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = {}, // Force interact
            title = { Text("🎉 PENERIMA ARISAN") },
            text = {
                Column {
                    Text(winner!!.nama, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Tanggal: ${DateUtils.formatTanggal(System.currentTimeMillis())}")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = nominalInput,
                        onValueChange = { nominalInput = it },
                        label = { Text("Nominal Didapat (Rp)") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val nominal = nominalInput.toDoubleOrNull() ?: 0.0
                    viewModel.setPemenangArisan(winner!!, nominal)
                    showWinnerDialog = false
                }) {
                    Text("Simpan Hasil")
                }
            }
        )
    }
}
