package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.EarningEntity
import com.example.data.db.UserProfileEntity
import com.example.data.repository.EarningRepository
import com.example.model.AdCampaign
import com.example.model.DailyStreakDay
import com.example.model.GiftCardOffer
import com.example.model.LiveWinner
import com.example.model.SpinSegment
import com.example.model.TaskOffer
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.notification.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

import com.example.model.AdminAdConfig
import com.example.model.VipSlotPackage

import com.example.data.db.UserAccountEntity

data class MainUiState(
    val profile: UserProfileEntity = UserProfileEntity(),
    val transactions: List<EarningEntity> = emptyList(),
    val allRawTransactions: List<EarningEntity> = emptyList(),
    val userAccounts: List<UserAccountEntity> = emptyList(),
    val activeAd: AdCampaign? = null,
    val adProgressSeconds: Int = 0,
    val isAdFinished: Boolean = false,
    val isAdMuted: Boolean = false,
    val isSpinning: Boolean = false,
    val spinAngle: Float = 0f,
    val spinWinCoins: Int? = null,
    val selectedGiftCard: GiftCardOffer? = null,
    val selectedDirectPayoutMethod: String? = null,
    val selectedSlotForPayment: VipSlotPackage? = null,
    val toastMessage: String? = null,
    val activeTab: Int = 0,
    val canNavigateBackTab: Boolean = false,
    val adminAdConfig: AdminAdConfig = AdminAdConfig(),
    val showAdminDialog: Boolean = false,
    val showAdminLoginDialog: Boolean = false,
    val isAdminLoggedIn: Boolean = false,
    val showAuthDialog: Boolean = false,
    val authDialogIsSignUp: Boolean = true,
    val taskOffers: List<TaskOffer> = com.example.model.getDefaultTaskList()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EarningRepository
    private var dailyJackpotCount = 0
    private var lastJackpotDateString = ""

    private val _liveWinners = MutableStateFlow<List<LiveWinner>>(generateInitialLiveWinners())
    val liveWinners: StateFlow<List<LiveWinner>> = _liveWinners.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = EarningRepository(db.earningDao())
        viewModelScope.launch {
            repository.ensureProfileInitialized()
        }
        startLiveWinnersSimulation()
    }

    private fun generateInitialLiveWinners(): List<LiveWinner> {
        val names = listOf(
            "Sumon_BD", "Tanvir_77", "Nusrat_Dhaka", "Sabbir_Pro", "Rakib_99",
            "Anik_Ctg", "Riya_01", "Shakib_Vip", "Mehedi_Ctg", "Jahid_88",
            "Fahim_Sylhet", "Mahim_Max", "Arian_99", "Tania_33"
        )
        val coinsList = listOf(15, 25, 50, 20, 30, 10, 100)
        val times = listOf("1m ago", "2m ago", "4m ago", "6m ago", "9m ago", "12m ago")
        return names.take(6).mapIndexed { idx, name ->
            val coins = coinsList[idx % coinsList.size]
            LiveWinner(
                userName = name,
                coinsWon = coins,
                isJackpot = coins == 150,
                timeAgo = times[idx % times.size]
            )
        }
    }

    private fun startLiveWinnersSimulation() {
        viewModelScope.launch {
            while (true) {
                delay(3500L + Random.nextLong(3000L))
                val names = listOf(
                    "Sumon_BD", "Tanvir_77", "Nusrat_Dhaka", "Sabbir_Pro", "Rakib_99",
                    "Anik_Ctg", "Riya_01", "Shakib_Vip", "Mehedi_Ctg", "Jahid_88",
                    "Fahim_Sylhet", "Mahim_Max", "Arian_99", "Tania_33", "Kawsar_77",
                    "Mim_Dhaka", "Shuvo_BD", "Ayesha_01", "Sohag_Opti", "Monir_99"
                )
                val coinsList = listOf(15, 25, 50, 20, 30, 10, 100)
                val isJackpotSim = Random.nextInt(100) < 2
                val wonCoins = if (isJackpotSim && dailyJackpotCount < 3) 150 else coinsList.random()

                val newWinner = LiveWinner(
                    userName = names.random(),
                    coinsWon = wonCoins,
                    isJackpot = wonCoins == 150,
                    timeAgo = "Just now"
                )

                val updated = listOf(newWinner) + _liveWinners.value.map {
                    if (it.timeAgo == "Just now") it.copy(timeAgo = "1m ago") else it
                }
                _liveWinners.value = updated.take(12)
            }
        }
    }

    private val _activeAd = MutableStateFlow<AdCampaign?>(null)
    private val _adProgressSeconds = MutableStateFlow(0)
    private val _isAdFinished = MutableStateFlow(false)
    private val _isAdMuted = MutableStateFlow(false)
    private val _isSpinning = MutableStateFlow(false)
    private val _spinAngle = MutableStateFlow(0f)
    private val _spinWinCoins = MutableStateFlow<Int?>(null)
    private val _selectedGiftCard = MutableStateFlow<GiftCardOffer?>(null)
    private val _selectedDirectPayoutMethod = MutableStateFlow<String?>(null)
    private val _selectedSlotForPayment = MutableStateFlow<VipSlotPackage?>(null)
    private val _toastMessage = MutableStateFlow<String?>(null)
    private val _activeTab = MutableStateFlow(0)
    private val _adminAdConfig = MutableStateFlow(AdminAdConfig())
    private val _showAdminDialog = MutableStateFlow(false)
    private val _showAdminLoginDialog = MutableStateFlow(false)
    private val _isAdminLoggedIn = MutableStateFlow(false)
    private val _showAuthDialog = MutableStateFlow(false)
    private val _authDialogIsSignUp = MutableStateFlow(true)
    private val _taskOffers = MutableStateFlow<List<TaskOffer>>(com.example.model.getDefaultTaskList())

    private val _canNavigateBackTab = MutableStateFlow(false)
    private val tabHistory = mutableListOf(0)

    private var adTimerJob: Job? = null

    val uiState: StateFlow<MainUiState> = combine(
        repository.userProfile,
        repository.allTransactions,
        repository.allUserAccounts,
        _activeAd,
        _adProgressSeconds,
        _isAdFinished,
        _isAdMuted,
        _isSpinning,
        _spinAngle,
        _spinWinCoins,
        _selectedGiftCard,
        _selectedDirectPayoutMethod,
        _selectedSlotForPayment,
        _toastMessage,
        _activeTab,
        _canNavigateBackTab,
        _adminAdConfig,
        _showAdminDialog,
        _showAdminLoginDialog,
        _isAdminLoggedIn,
        _showAuthDialog,
        _authDialogIsSignUp,
        _taskOffers
    ) { args ->
        val rawProfile = args[0] as UserProfileEntity
        val rawTransactions = args[1] as List<EarningEntity>
        val accounts = args[2] as List<UserAccountEntity>
        val activeAd = args[3] as AdCampaign?
        val adProgress = args[4] as Int
        val isAdFinished = args[5] as Boolean
        val isAdMuted = args[6] as Boolean
        val isSpinning = args[7] as Boolean
        val spinAngle = args[8] as Float
        val winCoins = args[9] as Int?
        val selectedCard = args[10] as GiftCardOffer?
        val selectedDirectPayout = args[11] as String?
        val selectedSlot = args[12] as VipSlotPackage?
        val toast = args[13] as String?
        val tab = args[14] as Int
        val canNavBack = args[15] as Boolean
        val adminConfig = args[16] as AdminAdConfig
        val showAdmin = args[17] as Boolean
        val showAdminLogin = args[18] as Boolean
        val isAdminLoggedIn = args[19] as Boolean
        val showAuth = args[20] as Boolean
        val authSignUp = args[21] as Boolean
        val tasksList = args[22] as List<TaskOffer>

        val effectiveProfile = if (rawProfile.isLoggedIn) {
            rawProfile
        } else {
            UserProfileEntity(
                userName = "Guest User",
                userContact = "",
                coinBalance = 0,
                totalEarnedCoins = 0,
                currentStreakDays = 1,
                totalAdsWatched = 0,
                totalSpinsCompleted = 0,
                isLoggedIn = false
            )
        }

        val effectiveTransactions = if (rawProfile.isLoggedIn) {
            rawTransactions
        } else {
            emptyList()
        }

        MainUiState(
            profile = effectiveProfile,
            transactions = effectiveTransactions,
            allRawTransactions = rawTransactions,
            userAccounts = accounts,
            activeAd = activeAd,
            adProgressSeconds = adProgress,
            isAdFinished = isAdFinished,
            isAdMuted = isAdMuted,
            isSpinning = isSpinning,
            spinAngle = spinAngle,
            spinWinCoins = winCoins,
            selectedGiftCard = selectedCard,
            selectedDirectPayoutMethod = selectedDirectPayout,
            selectedSlotForPayment = selectedSlot,
            toastMessage = toast,
            activeTab = tab,
            canNavigateBackTab = canNavBack,
            adminAdConfig = adminConfig,
            showAdminDialog = showAdmin,
            showAdminLoginDialog = showAdminLogin,
            isAdminLoggedIn = isAdminLoggedIn,
            showAuthDialog = showAuth,
            authDialogIsSignUp = authSignUp,
            taskOffers = tasksList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    val sampleAds = listOf(
        AdCampaign(
            id = "ad_1",
            title = "CyberQuest 3D RPG",
            sponsorName = "CyberGames Studio",
            rewardCoins = 20,
            durationSeconds = 6,
            category = "Gaming",
            videoBgColor = Color(0xFF312E81),
            description = "Experience next-gen RPG battles! Play now and get free starter gear."
        ),
        AdCampaign(
            id = "ad_2",
            title = "CryptoVault Pro",
            sponsorName = "Fintech Global",
            rewardCoins = 25,
            durationSeconds = 8,
            category = "Finance",
            videoBgColor = Color(0xFF065F46),
            description = "Secure zero-fee crypto wallet. Earn 5% APY on daily deposits."
        ),
        AdCampaign(
            id = "ad_3",
            title = "BeatFlow Music",
            sponsorName = "SoundStream Inc",
            rewardCoins = 15,
            durationSeconds = 5,
            category = "Entertainment",
            videoBgColor = Color(0xFF831843),
            description = "Stream millions of songs ad-free for 30 days free trial!"
        ),
        AdCampaign(
            id = "ad_4",
            title = "TravelDeals Flash Sale",
            sponsorName = "Wanderlust Media",
            rewardCoins = 30,
            durationSeconds = 10,
            category = "Travel",
            videoBgColor = Color(0xFF1E3A8A),
            description = "Book flights & luxury hotels with up to 50% cash rewards."
        )
    )

    val giftCards = listOf(
        GiftCardOffer("gc_1", "$5 PayPal Cash", "PayPal", 5000, 5.00, isPopular = true),
        GiftCardOffer("gc_2", "$10 PayPal Cash", "PayPal", 9500, 10.00),
        GiftCardOffer("gc_3", "$5 Amazon Gift Card", "Amazon", 5000, 5.00, isPopular = true),
        GiftCardOffer("gc_4", "$15 Amazon Gift Card", "Amazon", 14000, 15.00),
        GiftCardOffer("gc_5", "$5 Google Play Code", "Google Play", 5000, 5.00),
        GiftCardOffer("gc_6", "$10 USDT Crypto", "Crypto", 10000, 10.00)
    )

    val spinSegments = listOf(
        SpinSegment(1, 15, "15 Coins", Color(0xFF3B82F6)),
        SpinSegment(2, 25, "25 Coins", Color(0xFF10B981)),
        SpinSegment(3, 50, "50 Coins", Color(0xFF8B5CF6)),
        SpinSegment(4, 10, "10 Coins", Color(0xFF64748B)),
        SpinSegment(5, 150, "JACKPOT 150!", Color(0xFFF59E0B), isJackpot = true),
        SpinSegment(6, 20, "20 Coins", Color(0xFFEC4899)),
        SpinSegment(7, 30, "30 Coins", Color(0xFF06B6D4)),
        SpinSegment(8, 5, "5 Coins", Color(0xFF94A3B8))
    )

    val taskOffers: List<TaskOffer> get() = _taskOffers.value

    fun setActiveTab(tab: Int) {
        if (_activeTab.value != tab) {
            _activeTab.value = tab
            if (tabHistory.isEmpty() || tabHistory.last() != tab) {
                tabHistory.add(tab)
            }
            _canNavigateBackTab.value = tabHistory.size > 1 || tab != 0
        }
    }

    fun navigateBackTab(): Boolean {
        if (tabHistory.size > 1) {
            tabHistory.removeAt(tabHistory.lastIndex)
            val previousTab = tabHistory.last()
            _activeTab.value = previousTab
            _canNavigateBackTab.value = tabHistory.size > 1 || previousTab != 0
            return true
        } else if (_activeTab.value != 0) {
            _activeTab.value = 0
            tabHistory.clear()
            tabHistory.add(0)
            _canNavigateBackTab.value = false
            return true
        }
        _canNavigateBackTab.value = false
        return false
    }

    fun openAdminDialog() {
        if (_isAdminLoggedIn.value) {
            _showAdminDialog.value = true
        } else {
            _showAdminLoginDialog.value = true
        }
    }

    fun closeAdminDialog() {
        _showAdminDialog.value = false
    }

    fun openAdminLoginDialog() {
        _showAdminLoginDialog.value = true
    }

    fun closeAdminLoginDialog() {
        _showAdminLoginDialog.value = false
    }

    fun loginAdmin(gmail: String, pass: String) {
        val trimmedGmail = gmail.trim().lowercase()
        val trimmedPass = pass.trim()

        if (trimmedGmail.isEmpty() || trimmedPass.isEmpty()) {
            showToast("Please enter Admin Gmail and Password")
            return
        }

        if (trimmedGmail.contains("admin") && (trimmedPass == "admin" || trimmedPass == "admin123" || trimmedPass == "123456")) {
            _isAdminLoggedIn.value = true
            _showAdminLoginDialog.value = false
            _showAdminDialog.value = true
            showToast("Admin Login Successful! Welcome Admin 👑")
        } else {
            showToast("Invalid Admin Gmail or Password! ❌")
        }
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        _showAdminDialog.value = false
        showToast("Logged out from Admin Panel.")
    }

    fun updateUserCoinBalance(userId: Long, newBalance: Int) {
        viewModelScope.launch {
            repository.setUserCoinBalance(userId, newBalance)
            showToast("User #$userId coin balance updated to $newBalance Coins!")
        }
    }

    fun toggleUserBlockedStatus(userId: Long, isBlocked: Boolean) {
        viewModelScope.launch {
            repository.setUserBlockedStatus(userId, isBlocked)
            val actionText = if (isBlocked) "Blocked 🚫" else "Unblocked ✅"
            showToast("User #$userId has been $actionText")
        }
    }

    fun deleteUserAccount(userId: Long) {
        viewModelScope.launch {
            repository.deleteUserAccount(userId)
            showToast("User account #$userId deleted successfully! 🗑️")
        }
    }

    private fun isSameDay(ts1: Long, ts2: Long): Boolean {
        val cal1 = java.util.Calendar.getInstance().apply { timeInMillis = ts1 }
        val cal2 = java.util.Calendar.getInstance().apply { timeInMillis = ts2 }
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }

    fun updateAdminConfig(
        rewardCoins: Int,
        durationSeconds: Int,
        enforceFullWatch: Boolean,
        videoUrl: String = "",
        slot1Limit: Int = 5,
        slot2Price: Int = 500,
        slot2Limit: Int = 25,
        slot3Price: Int = 1500,
        slot3Limit: Int = 60,
        slot4Price: Int = 3000,
        slot4Limit: Int = 120,
        bkashNum: String = "01700000000",
        nagadNum: String = "01800000000",
        rocketNum: String = "01900000000",
        minPayoutBdt: Int = 50,
        coinsPerBdt: Int = 100
    ) {
        val currentUrl = if (videoUrl.isNotBlank()) videoUrl else _adminAdConfig.value.videoUrl
        _adminAdConfig.value = AdminAdConfig(
            videoRewardCoins = rewardCoins,
            videoDurationSeconds = durationSeconds,
            enforceFullWatch = enforceFullWatch,
            videoUrl = currentUrl,
            slot1TaskLimit = slot1Limit,
            slot2PriceBdt = slot2Price,
            slot2TaskLimit = slot2Limit,
            slot3PriceBdt = slot3Price,
            slot3TaskLimit = slot3Limit,
            slot4PriceBdt = slot4Price,
            slot4TaskLimit = slot4Limit,
            bkashNumber = bkashNum,
            nagadNumber = nagadNum,
            rocketNumber = rocketNum,
            minPayoutBdt = minPayoutBdt,
            coinsPerBdt = coinsPerBdt
        )
        showToast("এডমিন কনফিগ ও পেআউট সেটিংস আপডেট করা হয়েছে! ⚙️")
    }

    fun startAdWatch(campaign: AdCampaign? = null) {
        val userVip = uiState.value.profile.vipLevel
        val config = _adminAdConfig.value
        val dailyTaskLimit = when (userVip) {
            2 -> config.slot2TaskLimit
            3 -> config.slot3TaskLimit
            4 -> config.slot4TaskLimit
            else -> config.slot1TaskLimit
        }

        val todayAdCount = uiState.value.transactions.count {
            it.type == TransactionType.AD_WATCH && isSameDay(it.timestamp, System.currentTimeMillis())
        }

        if (todayAdCount >= dailyTaskLimit) {
            showToast("আজকের স্লট লিমিট ($todayAdCount/$dailyTaskLimit) শেষ! পরবর্তী VIP স্লট একটিভ করুন। 🔒")
            return
        }

        val baseAd = campaign ?: sampleAds.random()
        val vipMultiplier = when (userVip) {
            2 -> 1.5
            3 -> 2.0
            4 -> 3.0
            else -> 1.0
        }
        val calculatedReward = (config.videoRewardCoins * vipMultiplier).toInt()
        
        // Apply admin configured reward points, VIP slot multiplier, duration, and video URL
        val adToPlay = baseAd.copy(
            rewardCoins = calculatedReward,
            durationSeconds = config.videoDurationSeconds,
            ctaUrl = config.videoUrl
        )

        _activeAd.value = adToPlay
        _adProgressSeconds.value = adToPlay.durationSeconds
        _isAdFinished.value = false

        adTimerJob?.cancel()
        adTimerJob = viewModelScope.launch {
            while (_adProgressSeconds.value > 0) {
                delay(1000L)
                _adProgressSeconds.value = _adProgressSeconds.value - 1
            }
            _isAdFinished.value = true
        }
    }

    fun toggleAdMute() {
        _isAdMuted.value = !_isAdMuted.value
    }

    fun claimAdReward() {
        val ad = _activeAd.value ?: return
        if (!uiState.value.profile.isLoggedIn) {
            showToast("কয়েন আর্ন করতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            resetAdState()
            return
        }
        if (!_isAdFinished.value && _adminAdConfig.value.enforceFullWatch) {
            showToast("Video full time dekhte hobe, ta na hoile point add hobe na! ❌")
            return
        }

        viewModelScope.launch {
            repository.addCoins(
                coins = ad.rewardCoins,
                title = "Watched Video: ${ad.title}",
                description = "Rewarded video ad view completed (${ad.sponsorName})",
                type = TransactionType.AD_WATCH
            )
            showToast("🎉 Full video watched! +${ad.rewardCoins} Coins added!")
            resetAdState()
        }
    }

    fun closeAdDialog(isExplicitCancel: Boolean = true) {
        if (isExplicitCancel && !_isAdFinished.value && _adminAdConfig.value.enforceFullWatch) {
            showToast("Video full time dekhte hobe, ta na hoile point add hobe na! ❌")
        }
        resetAdState()
    }

    private fun resetAdState() {
        adTimerJob?.cancel()
        _activeAd.value = null
        _adProgressSeconds.value = 0
        _isAdFinished.value = false
    }

    fun spinLuckyWheel() {
        if (_isSpinning.value) return
        if (!uiState.value.profile.isLoggedIn) {
            showToast("স্পিন করে কয়েন আর্ন করতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }

        val now = System.currentTimeMillis()
        val lastSpin = uiState.value.profile.lastSpinTimestamp
        val cooldownMs = 4 * 60 * 60 * 1000L // 4 hours

        if (lastSpin > 0 && (now - lastSpin) < cooldownMs) {
            val remainingMs = cooldownMs - (now - lastSpin)
            val hours = remainingMs / (1000 * 60 * 60)
            val minutes = (remainingMs % (1000 * 60 * 60)) / (1000 * 60)
            val seconds = (remainingMs % (1000 * 60)) / 1000
            showToast("প্রতি ৪ ঘণ্টা পর পর ১টি স্পিন পাওয়া যায়। পরবর্তী স্পিন: ${hours}h ${minutes}m ${seconds}s ⏳")
            return
        }

        viewModelScope.launch {
            _isSpinning.value = true
            _spinWinCoins.value = null

            val todayDateStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            if (todayDateStr != lastJackpotDateString) {
                lastJackpotDateString = todayDateStr
                dailyJackpotCount = 0
            }

            var winningSegmentIndex = Random.nextInt(spinSegments.size)
            var winningSegment = spinSegments[winningSegmentIndex]

            // Enforce 150 Jackpot coins max 3 per day limit
            if (winningSegment.isJackpot || winningSegment.coins == 150) {
                if (dailyJackpotCount >= 3) {
                    val nonJackpotIndices = spinSegments.indices.filter { !spinSegments[it].isJackpot && spinSegments[it].coins != 150 }
                    winningSegmentIndex = nonJackpotIndices.random()
                    winningSegment = spinSegments[winningSegmentIndex]
                } else {
                    dailyJackpotCount++
                }
            }

            val degreesPerSegment = 360f / spinSegments.size
            val targetSegmentAngle = (spinSegments.size - winningSegmentIndex) * degreesPerSegment - (degreesPerSegment / 2f)
            val fullRotations = 5 * 360f
            val finalAngle = _spinAngle.value + fullRotations + (targetSegmentAngle - (_spinAngle.value % 360f))

            _spinAngle.value = finalAngle

            delay(3200L) // Wait for spin animation

            _isSpinning.value = false
            _spinWinCoins.value = winningSegment.coins

            repository.addCoins(
                coins = winningSegment.coins,
                title = "Lucky Wheel Spin",
                description = "Landed on ${winningSegment.label}",
                type = TransactionType.SPIN_WHEEL
            )

            val winner = LiveWinner(
                userName = uiState.value.profile.userName.ifBlank { "You" },
                coinsWon = winningSegment.coins,
                isJackpot = winningSegment.isJackpot || winningSegment.coins == 150,
                timeAgo = "Just now"
            )
            _liveWinners.value = (listOf(winner) + _liveWinners.value).take(12)

            showToast("You won ${winningSegment.coins} Coins! 🎉")
        }
    }

    fun dismissSpinWinDialog() {
        _spinWinCoins.value = null
    }

    fun claimDailyCheckIn(dayNumber: Int, coinReward: Int) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("ডেইলি বোনাস পেতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        viewModelScope.launch {
            repository.claimDailyCheckIn(dayNumber, coinReward)
                .onSuccess { coins ->
                    val updatedStreak = uiState.value.profile.currentStreakDays
                    showToast("🎉 Day $updatedStreak Check-In Bonus Claimed (+${coins} Coins)!")
                }
                .onFailure { error ->
                    showToast(error.message ?: "আজকের ডেইলি বোনাস আগেই ক্লেইম করা হয়েছে!")
                }
        }
    }

    fun completeTaskOffer(task: TaskOffer) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("কয়েন অর্জন করতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        viewModelScope.launch {
            repository.addCoins(
                coins = task.rewardCoins,
                title = "Task #${task.serialNumber}: ${task.title}",
                description = "Completed ${task.category.displayName} Task",
                type = TransactionType.OFFER_TASK
            )
            markTaskCompleted(task.id)
            showToast("🎉 টাস্ক #${task.serialNumber} সম্পন্ন হয়েছে! +${task.rewardCoins} কয়েন যুক্ত করা হয়েছে।")
        }
    }

    fun addNewTask(task: TaskOffer) {
        val current = _taskOffers.value.toMutableList()
        val newId = "task_${System.currentTimeMillis()}"
        val serial = if (task.serialNumber <= 0) current.size + 1 else task.serialNumber
        val code = if (task.taskCode.isBlank() || task.taskCode == "TASK-101") "TASK-${100 + serial}" else task.taskCode
        val newTask = task.copy(id = newId, serialNumber = serial, taskCode = code)
        current.add(newTask)
        current.sortBy { it.serialNumber }
        _taskOffers.value = current
        
        // Trigger system notification for new task
        NotificationHelper.triggerNewTaskNotification(
            getApplication(),
            newTask.title,
            newTask.taskCode
        )

        showToast("নতুন টাস্ক [${newTask.taskCode}] তৈরি ও নোটিফিকেশন পাঠানো হয়েছে! 🎯")
    }

    fun updateTask(updatedTask: TaskOffer) {
        val current = _taskOffers.value.toMutableList()
        val index = current.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            current[index] = updatedTask
            current.sortBy { it.serialNumber }
            _taskOffers.value = current
            showToast("টাস্ক #${updatedTask.serialNumber} আপডেট হয়েছে! ✅")
        }
    }

    fun deleteTask(taskId: String) {
        val current = _taskOffers.value.filter { it.id != taskId }
        _taskOffers.value = current
        showToast("টাস্ক সফলভাবে মুছে ফেলা হয়েছে! 🗑️")
    }

    fun markTaskCompleted(taskId: String) {
        val current = _taskOffers.value.toMutableList()
        val index = current.indexOfFirst { it.id == taskId }
        if (index != -1) {
            current[index] = current[index].copy(isCompleted = true)
            _taskOffers.value = current
        }
    }

    fun openRedeemModal(giftCard: GiftCardOffer) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("রিডিম করতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        _selectedGiftCard.value = giftCard
    }

    fun openDirectPayoutModal(methodName: String) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("পেআউট করতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        _selectedDirectPayoutMethod.value = methodName
    }

    fun closeRedeemModal() {
        _selectedGiftCard.value = null
        _selectedDirectPayoutMethod.value = null
    }

    fun processRedeem(destinationAccount: String) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("রিডিম করতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        val giftCard = _selectedGiftCard.value ?: return
        if (destinationAccount.isBlank()) {
            showToast("Please enter a valid payout account or email")
            return
        }

        viewModelScope.launch {
            val result = repository.redeemGiftCard(
                title = giftCard.title,
                coinCost = giftCard.coinCost,
                usdValue = giftCard.usdValue,
                destinationAccount = destinationAccount
            )
            result.onSuccess {
                showToast("Redemption request submitted! Awaiting Admin Approval. ⏳")
                closeRedeemModal()
            }.onFailure { ex ->
                showToast(ex.message ?: "Redemption failed")
            }
        }
    }

    fun openSlotPaymentModal(pkg: VipSlotPackage) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("স্লট কিনতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        _selectedSlotForPayment.value = pkg
    }

    fun closeSlotPaymentModal() {
        _selectedSlotForPayment.value = null
    }

    fun submitSlotPayment(
        pkg: VipSlotPackage,
        paymentMethod: String,
        senderNumber: String,
        trxId: String
    ) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("স্লট কিনতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        submitSlotPayment(
            targetLevel = pkg.level,
            amountBdt = pkg.priceBdt,
            paymentMethod = paymentMethod,
            senderNumber = senderNumber,
            trxId = trxId
        )
        closeSlotPaymentModal()
    }

    fun submitSlotPayment(
        targetLevel: Int,
        amountBdt: Int,
        paymentMethod: String,
        senderNumber: String,
        trxId: String
    ) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("স্লট কিনতে অনুগ্রহ করে একাউন্ট সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        if (senderNumber.isBlank() || trxId.isBlank()) {
            showToast("অনুগ্রহ করে সেন্ডার নম্বর ও TrxID সঠিকভাবে লিখুন! ❌")
            return
        }

        viewModelScope.launch {
            val result = repository.submitSlotPurchaseRequest(
                targetLevel = targetLevel,
                amountBdt = amountBdt,
                paymentMethod = paymentMethod,
                senderNumber = senderNumber,
                trxId = trxId
            )
            result.onSuccess {
                showToast("🎉 পেমেন্ট রিকোয়েস্ট জমা হয়েছে! এডমিন ভেরিফাই করে দ্রুত স্লট একটিভ করে দেবে।")
                closeSlotPaymentModal()
            }.onFailure { ex ->
                showToast(ex.message ?: "রিকোয়েস্ট জমা দিতে ব্যর্থ হয়েছে")
            }
        }
    }

    fun submitDirectPayoutRequest(methodName: String, phoneNumber: String, amountBdt: Int, coinCost: Int) {
        if (!uiState.value.profile.isLoggedIn) {
            showToast("পেআউট করতে অনুগ্রহ করে সাইন আপ বা লগইন করুন! ⚠️")
            openAuthDialog(isSignUp = true)
            return
        }
        if (phoneNumber.isBlank()) {
            showToast("অনুগ্রহ করে সঠিক মোবাইল নম্বরটি লিখুন! 📱")
            return
        }

        viewModelScope.launch {
            val result = repository.requestPayout(
                methodName = methodName,
                phoneNumber = phoneNumber,
                amountBdt = amountBdt,
                coinCost = coinCost
            )
            result.onSuccess {
                showToast("🎉 ৳$amountBdt $methodName পেআউট রিকোয়েস্ট জমা হয়েছে! এডমিন দ্রুত টাকা পাঠাবে।")
                closeRedeemModal()
            }.onFailure { ex ->
                showToast(ex.message ?: "পেআউট জমা দিতে ব্যর্থ হয়েছে")
            }
        }
    }

    fun updatePayoutStatus(transactionId: Long, newStatus: TransactionStatus, trxId: String = "") {
        viewModelScope.launch {
            val transaction = repository.getTransactionById(transactionId)
            val success = repository.updatePayoutStatus(transactionId, newStatus, trxId)
            if (success) {
                if (transaction != null && transaction.type == com.example.model.TransactionType.SLOT_UPGRADE) {
                    val label = when (newStatus) {
                        TransactionStatus.COMPLETED -> "Approved & Activated! 👑"
                        TransactionStatus.REJECTED -> "Rejected ❌"
                        TransactionStatus.PENDING -> "Reset to Pending ⏳"
                    }
                    showToast("Slot Upgrade #$transactionId $label")

                    when (newStatus) {
                        TransactionStatus.COMPLETED -> NotificationHelper.triggerSlotStatusNotification(
                            context = getApplication(),
                            slotTitle = transaction.title,
                            isApproved = true
                        )
                        TransactionStatus.REJECTED -> NotificationHelper.triggerSlotStatusNotification(
                            context = getApplication(),
                            slotTitle = transaction.title,
                            isApproved = false
                        )
                        else -> {}
                    }
                } else {
                    val label = when (newStatus) {
                        TransactionStatus.COMPLETED -> "Approved & Sent ✅"
                        TransactionStatus.REJECTED -> "Rejected & Refunded ❌"
                        TransactionStatus.PENDING -> "Reset to Pending ⏳"
                    }
                    showToast("Payout Request #$transactionId $label")

                    if (transaction != null) {
                        when (newStatus) {
                            TransactionStatus.COMPLETED -> NotificationHelper.triggerPayoutStatusNotification(
                                context = getApplication(),
                                payoutTitle = transaction.title,
                                isApproved = true
                            )
                            TransactionStatus.REJECTED -> NotificationHelper.triggerPayoutStatusNotification(
                                context = getApplication(),
                                payoutTitle = transaction.title,
                                isApproved = false
                            )
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    fun openAuthDialog(isSignUp: Boolean = true) {
        _showAuthDialog.value = true
        _authDialogIsSignUp.value = isSignUp
    }

    fun closeAuthDialog() {
        _showAuthDialog.value = false
    }

    fun signUpUser(name: String, contact: String, pass: String, confirmPass: String, refCode: String? = null) {
        viewModelScope.launch {
            val result = repository.signUpUser(name, contact, pass, confirmPass, refCode)
            result.onSuccess { profile ->
                val bonusMsg = if (!refCode.isNullOrBlank()) " (+50 Bonus Coins Added!)" else ""
                showToast("Welcome ${profile.userName}! Account created successfully. 🎉$bonusMsg")
                closeAuthDialog()
            }.onFailure { ex ->
                showToast(ex.message ?: "Sign up failed")
            }
        }
    }

    fun loginUser(contact: String, pass: String) {
        val trimmedContact = contact.trim().lowercase()
        val trimmedPass = pass.trim()

        // Intercept Admin credentials and log directly into Admin Panel
        if ((trimmedContact.contains("admin") || trimmedContact == "admin@gmail.com") && 
            (trimmedPass == "admin123" || trimmedPass == "admin" || trimmedPass == "123456" || trimmedPass == "admin1234")) {
            _isAdminLoggedIn.value = true
            _showAdminDialog.value = true
            closeAuthDialog()
            closeAdminLoginDialog()
            showToast("Admin Login Successful! Welcome Admin 👑")
            return
        }

        viewModelScope.launch {
            val result = repository.loginUser(contact, pass)
            result.onSuccess { profile ->
                if (profile.isBlocked) {
                    showToast("Your account has been BLOCKED by Admin! 🚫")
                    repository.logoutUser()
                } else {
                    showToast("Welcome back ${profile.userName}! Logged in successfully. ✅")
                    closeAuthDialog()
                }
            }.onFailure { ex ->
                showToast(ex.message ?: "Login failed")
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
            showToast("Logged out successfully. (সাইন আউট সম্পন্ন)")
        }
    }

    fun sendTestDailyCheckInReminder() {
        NotificationHelper.triggerDailyCheckInReminder(getApplication())
        showToast("FCM Daily Check-in Reminder Notification Triggered! 🔔")
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
