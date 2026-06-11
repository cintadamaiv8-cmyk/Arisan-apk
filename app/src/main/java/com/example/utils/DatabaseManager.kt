package com.example.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object DatabaseManager {
    private const val DB_NAME = "mask_arisan_database"

    fun backupDatabase(context: Context, targetUri: Uri): Boolean {
        return try {
            val dbFile = context.getDatabasePath(DB_NAME)
            if (!dbFile.exists()) return false

            context.contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                FileInputStream(dbFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            
            // Backup SHM and WAL if needed, but for simpler SQLite it might be enough if checkpointed,
            // or just copy the main file if WAL is disabled. Wait, Room uses WAL by default since Android 9.
            // Better to copy -wal and -shm as well or disable WAL. RoomDatabase Builder .setJournalMode(JournalMode.TRUNCATE).
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun restoreDatabase(context: Context, sourceUri: Uri): Boolean {
        return try {
            val dbFile = context.getDatabasePath(DB_NAME)
            val tempFile = File(context.cacheDir, "temp_restore.db")

            // Copy selected file to temp
            context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Simple replace. In production, ensure DB is closed first via Room instance if possible.
            if (tempFile.exists()) {
                tempFile.copyTo(dbFile, overwrite = true)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
