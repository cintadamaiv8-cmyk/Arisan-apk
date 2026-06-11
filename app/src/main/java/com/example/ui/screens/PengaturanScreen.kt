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
import com.example.utils.DatabaseManager
import com.example.utils.ThemePreferences
import com.example.viewmodel.MaskArisanViewModel
import kotlinx.coroutines.launch

@Composable
fun PengaturanScreen(viewModel: MaskArisanViewModel, themePreferences: ThemePreferences) {
    val themeMode by themePreferences.themeMode.collectAsState(initial = ThemePreferences.SYSTEM)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
        if (uri != null) {
            val success = DatabaseManager.backupDatabase(context, uri)
            Toast.makeText(context, if (success) "Backup Berhasil" else "Backup Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val success = DatabaseManager.restoreDatabase(context, uri)
            Toast.makeText(context, if (success) "Restore Berhasil. Silakan mulai ulang aplikasi." else "Restore Gagal", Toast.LENGTH_LONG).show()
        }
    }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tema Aplikasi", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Terang")
            RadioButton(
                selected = themeMode == ThemePreferences.LIGHT,
                onClick = { scope.launch { themePreferences.saveThemeMode(ThemePreferences.LIGHT) } }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Gelap")
            RadioButton(
                selected = themeMode == ThemePreferences.DARK,
                onClick = { scope.launch { themePreferences.saveThemeMode(ThemePreferences.DARK) } }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Ikuti Sistem")
            RadioButton(
                selected = themeMode == ThemePreferences.SYSTEM,
                onClick = { scope.launch { themePreferences.saveThemeMode(ThemePreferences.SYSTEM) } }
            )
        }
        
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        
        Text("Database", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { exportLauncher.launch("mask_arisan_backup.db") }, modifier = Modifier.fillMaxWidth()) {
            Text("Backup Database")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { importLauncher.launch(arrayOf("application/octet-stream", "*/*")) }, modifier = Modifier.fillMaxWidth()) {
            Text("Restore Database")
        }
    }
}
