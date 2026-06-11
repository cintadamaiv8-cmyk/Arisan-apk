package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TentangScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("M", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("MASKARISAN", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Versi 1.0.0")
        Spacer(modifier = Modifier.height(32.dp))
        Text("Developer: Maskaav", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Aplikasi arisan offline untuk RT, RW, komunitas, kelompok warga dan organisasi kecil.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Mode: Offline")
                Text("Database: Room SQLite")
                Text("Didukung oleh: Maskaav")
            }
        }
    }
}
