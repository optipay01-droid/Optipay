package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.model.TransactionStatus
import com.example.model.TransactionType

class RoomConverters {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType =
        runCatching { TransactionType.valueOf(value) }.getOrDefault(TransactionType.AD_WATCH)

    @TypeConverter
    fun fromTransactionStatus(status: TransactionStatus): String = status.name

    @TypeConverter
    fun toTransactionStatus(value: String): TransactionStatus =
        runCatching { TransactionStatus.valueOf(value) }.getOrDefault(TransactionStatus.COMPLETED)
}

@Database(
    entities = [EarningEntity::class, UserProfileEntity::class, UserAccountEntity::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun earningDao(): EarningDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "watch_and_earn_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
