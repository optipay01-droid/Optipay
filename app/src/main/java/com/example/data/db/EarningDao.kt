package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.TransactionStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface EarningDao {
    @Query("SELECT * FROM earning_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<EarningEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: EarningEntity)

    @Query("UPDATE earning_transactions SET status = :status WHERE id = :id")
    suspend fun updateTransactionStatus(id: Long, status: TransactionStatus)

    @Query("UPDATE earning_transactions SET status = :status, transactionTrxId = :trxId WHERE id = :id")
    suspend fun updateTransactionStatusAndTrxId(id: Long, status: TransactionStatus, trxId: String)

    @Query("SELECT * FROM earning_transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): EarningEntity?

    @Query("SELECT * FROM user_profile_stats WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile_stats WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("DELETE FROM earning_transactions")
    suspend fun clearAllTransactions()

    @Query("SELECT * FROM user_accounts ORDER BY joinedTimestamp DESC")
    fun getAllUserAccounts(): Flow<List<UserAccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAccount(user: UserAccountEntity): Long

    @Update
    suspend fun updateUserAccount(user: UserAccountEntity)

    @Query("SELECT * FROM user_accounts WHERE LOWER(userContact) = LOWER(:contact) LIMIT 1")
    suspend fun getUserAccountByContact(contact: String): UserAccountEntity?

    @Query("UPDATE user_accounts SET coinBalance = :newBalance WHERE id = :id")
    suspend fun updateUserBalance(id: Long, newBalance: Int)

    @Query("UPDATE user_accounts SET isBlocked = :isBlocked WHERE id = :id")
    suspend fun updateUserBlocked(id: Long, isBlocked: Boolean)

    @Query("DELETE FROM user_accounts WHERE id = :id")
    suspend fun deleteUserAccount(id: Long)
}
