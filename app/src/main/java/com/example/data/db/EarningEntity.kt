package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.TransactionStatus
import com.example.model.TransactionType

@Entity(tableName = "earning_transactions")
data class EarningEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val coins: Int,
    val type: TransactionType,
    val status: TransactionStatus = TransactionStatus.COMPLETED,
    val timestamp: Long = System.currentTimeMillis(),
    val targetVipLevel: Int = 1,
    val paymentMethod: String = "",
    val senderPhoneNumber: String = "",
    val transactionTrxId: String = "",
    val amountBdt: Int = 0
)

@Entity(tableName = "user_profile_stats")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val coinBalance: Int = 0,
    val totalEarnedCoins: Int = 0,
    val totalPayoutsUsd: Double = 0.0,
    val currentStreakDays: Int = 1,
    val lastCheckInTimestamp: Long = 0,
    val totalAdsWatched: Int = 0,
    val totalSpinsCompleted: Int = 0,
    val lastSpinTimestamp: Long = 0,
    val referralCode: String = "WATCH2026",
    val appliedReferralCode: String = "",
    val isLoggedIn: Boolean = false,
    val userName: String = "Guest User",
    val userContact: String = "guest@example.com",
    val userPasswordHash: String = "123456",
    val isBlocked: Boolean = false,
    val vipLevel: Int = 1
)

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userName: String,
    val userContact: String,
    val userPasswordHash: String,
    val coinBalance: Int = 0,
    val totalEarnedCoins: Int = 0,
    val isBlocked: Boolean = false,
    val referralCode: String = "REF${(1000..9999).random()}",
    val appliedReferralCode: String = "",
    val joinedTimestamp: Long = System.currentTimeMillis(),
    val vipLevel: Int = 1
)
