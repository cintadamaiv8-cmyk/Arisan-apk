package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PesertaEntity::class,
        SetoranEntity::class,
        RiwayatEntity::class,
        AuditLogEntity::class,
        PengaturanEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pesertaDao(): PesertaDao
    abstract fun setoranDao(): SetoranDao
    abstract fun riwayatDao(): RiwayatDao
    abstract fun auditLogDao(): AuditLogDao
    abstract fun pengaturanDao(): PengaturanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mask_arisan_database"
                )
                    .setJournalMode(androidx.room.RoomDatabase.JournalMode.TRUNCATE)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
