package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utils.CurrencyUtils
import com.example.utils.MaskBot
import com.example.viewmodel.MaskArisanViewModel

@Composable
fun DashboardScreen(viewModel: MaskArisanViewModel) {
    val pesertaAktif by viewModel.countPesertaAktif.collectAsState()
    val sudahKeluar by viewModel.countPesertaSudahKeluar.collectAsState()
    val belumKeluar = pesertaAktif - sudahKeluar
    
    // For MaskBot
    val allPeserta by viewModel.allPeserta.collectAsState()
    val belumSetorList by viewModel.belumSetor.collectAsState()
    
    val bounds = com.example.utils.DateUtils.getCurrentWeekStartEnd()
    val totalKas by viewModel.sumKasMingguIni(bounds.first, bounds.second).collectAsState(initial = 0.0)
    val totalSetor by viewModel.countSudahSetorMingguIni(bounds.first, bounds.second).collectAsState(initial = 0)
    
    val maskBotMessage = MaskBot.getMessage(
        pesertaKosong = allPeserta.isEmpty(),
        adaBelumSetor = belumSetorList.isNotEmpty(),
        semuaSetor = belumSetorList.isEmpty() && pesertaAktif > 0,
        semuaSudahMenerima = (belumKeluar <= 0 && pesertaAktif > 0)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Dashboard", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // MaskBot Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("maskbot_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "MaskBot", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.padding(top = 2.dp)) {
                        Text("MASKBOT", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f), letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(maskBotMessage, fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp)
                    }
                }
            }
        }

        if (allPeserta.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Belum ada peserta. Silakan tambahkan peserta terlebih dahulu.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
        } else {
            // Stats Grid
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatsCard(modifier = Modifier.weight(1f), title = "Peserta Aktif", value = pesertaAktif.toString(), valueColor = MaterialTheme.colorScheme.primary)
                    StatsCard(modifier = Modifier.weight(1f), title = "Sudah Keluar", value = sudahKeluar.toString(), valueColor = MaterialTheme.colorScheme.onSurface)
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatsCard(modifier = Modifier.weight(1f), title = "Belum Keluar", value = belumKeluar.toString(), valueColor = MaterialTheme.colorScheme.onSurface)
                    StatsCard(modifier = Modifier.weight(1f), title = "Belum Setor", value = belumSetorList.size.toString(), valueColor = MaterialTheme.colorScheme.error)
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatsCard(modifier = Modifier.weight(1f), title = "Sudah Setor", value = totalSetor.toString(), valueColor = MaterialTheme.colorScheme.onSurface)
                    StatsCard(modifier = Modifier.weight(1f), title = "Kas Minggu Ini", value = com.example.utils.CurrencyUtils.formatRupiah(totalKas ?: 0.0), valueColor = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun StatsCard(modifier: Modifier = Modifier, title: String, value: String, valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface) {
    Card(
        modifier = modifier, 
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = androidx.compose.ui.graphics.Color(0xFF64748B))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = valueColor)
        }
    }
}
