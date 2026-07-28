package com.example.model

import androidx.compose.ui.graphics.Color

enum class TransactionType {
    AD_WATCH,
    DAILY_STREAK,
    SPIN_WHEEL,
    SCRATCH_CARD,
    OFFER_TASK,
    PAYOUT_REDEEMED,
    BONUS_REFERRAL,
    SLOT_UPGRADE
}

enum class TransactionStatus {
    COMPLETED,
    PENDING,
    REJECTED
}

data class AdCampaign(
    val id: String,
    val title: String,
    val sponsorName: String,
    val rewardCoins: Int,
    val durationSeconds: Int,
    val category: String,
    val videoBgColor: Color,
    val description: String,
    val ctaUrl: String = "https://example.com"
)

data class AdminAdConfig(
    val videoRewardCoins: Int = 35,
    val videoDurationSeconds: Int = 8,
    val enforceFullWatch: Boolean = true,
    val videoUrl: String = "https://www.w3schools.com/html/mov_bbb.mp4",
    // VIP Slot Settings
    val slot1TaskLimit: Int = 5,
    val slot2PriceBdt: Int = 500,
    val slot2TaskLimit: Int = 25,
    val slot3PriceBdt: Int = 1500,
    val slot3TaskLimit: Int = 60,
    val slot4PriceBdt: Int = 3000,
    val slot4TaskLimit: Int = 120,
    // Payment Deposit Phone Numbers
    val bkashNumber: String = "01700000000",
    val nagadNumber: String = "01800000000",
    val rocketNumber: String = "01900000000",
    // Minimum Payout Settings
    val minPayoutBdt: Int = 50,
    val coinsPerBdt: Int = 100
)

data class GiftCardOffer(
    val id: String,
    val title: String,
    val category: String, // PayPal, Amazon, Google Play, Crypto, Airtime
    val coinCost: Int,
    val usdValue: Double,
    val isPopular: Boolean = false,
    val drawableResName: String? = null
)

data class SpinSegment(
    val id: Int,
    val coins: Int,
    val label: String,
    val color: Color,
    val isJackpot: Boolean = false
)

data class LiveWinner(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userName: String,
    val coinsWon: Int,
    val isJackpot: Boolean = false,
    val timeAgo: String = "Just now"
)

enum class TaskCategory(val displayName: String, val badgeColorHex: Long) {
    FACEBOOK_FOLLOW("Facebook Follow/Like", 0xFF1877F2),
    FACEBOOK_POST("Facebook Post Like & Share", 0xFF0284C7),
    YOUTUBE_SUBSCRIBE("YouTube Subscribe", 0xFFFF0000),
    YOUTUBE_WATCH("YouTube Video & Comment", 0xFFDC2626),
    WEBSITE_VISIT("Website Visit", 0xFF10B981),
    VIDEO_WATCH("Video Task", 0xFF8B5CF6)
}

data class TaskOffer(
    val id: String = "task_1",
    val serialNumber: Int = 1,
    val taskCode: String = "TASK-101",
    val title: String = "",
    val provider: String = "OptiPay Task",
    val rewardCoins: Int = 100,
    val estimatedMinutes: Int = 1,
    val requiredSeconds: Int = 30,
    val difficulty: String = "Easy",
    val iconName: String = "ic_task",
    val category: TaskCategory = TaskCategory.FACEBOOK_FOLLOW,
    val targetUrl: String = "https://facebook.com",
    val instruction: String = "লিংকে গিয়ে লাইক, শেয়ার বা সাবস্ক্রাইব করে প্রুফ কমপ্লিট করুন",
    val isCompleted: Boolean = false
)

fun getDefaultTaskList(): List<TaskOffer> = listOf(
    TaskOffer(
        id = "task_1",
        serialNumber = 1,
        taskCode = "TASK-101",
        title = "Facebook Page Like & Follow Task",
        provider = "Facebook Social",
        rewardCoins = 120,
        requiredSeconds = 15,
        category = TaskCategory.FACEBOOK_FOLLOW,
        targetUrl = "https://facebook.com",
        instruction = "ফেসবুক পেজটিতে ভিজিট করে লাইক ও ফলো বাটনে ক্লিক করুন।"
    ),
    TaskOffer(
        id = "task_2",
        serialNumber = 2,
        taskCode = "TASK-102",
        title = "Facebook Post Like, Comment & Share",
        provider = "Facebook Social",
        rewardCoins = 150,
        requiredSeconds = 20,
        category = TaskCategory.FACEBOOK_POST,
        targetUrl = "https://facebook.com",
        instruction = "পোস্টে একটি সুন্দর কমেন্ট, লাইক ও শেয়ার দিন।"
    ),
    TaskOffer(
        id = "task_3",
        serialNumber = 3,
        taskCode = "TASK-103",
        title = "YouTube Channel Subscribe & Notification",
        provider = "YouTube Network",
        rewardCoins = 200,
        requiredSeconds = 30,
        category = TaskCategory.YOUTUBE_SUBSCRIBE,
        targetUrl = "https://youtube.com",
        instruction = "ইউটিউব চ্যানেলে গিয়ে সাবস্ক্রাইব করুন ও বেল আইকনটি অন করুন।"
    ),
    TaskOffer(
        id = "task_4",
        serialNumber = 4,
        taskCode = "TASK-104",
        title = "YouTube Full Video View & Comment",
        provider = "YouTube Network",
        rewardCoins = 250,
        requiredSeconds = 45,
        category = TaskCategory.YOUTUBE_WATCH,
        targetUrl = "https://youtube.com",
        instruction = "ভিডিওটি ৪৫ সেকেন্ড মনোযোগ দিয়ে দেখুন ও পজিটিভ কমেন্ট করুন।"
    ),
    TaskOffer(
        id = "task_5",
        serialNumber = 5,
        taskCode = "TASK-105",
        title = "Visit Official Sponsor Website (60s)",
        provider = "Sponsor Web",
        rewardCoins = 180,
        requiredSeconds = 60,
        category = TaskCategory.WEBSITE_VISIT,
        targetUrl = "https://google.com",
        instruction = "ওয়েবসাইটে প্রবেশ করে ৬০ সেকেন্ড অবস্থান করুন।"
    ),
    TaskOffer(
        id = "task_6",
        serialNumber = 6,
        taskCode = "TASK-106",
        title = "Watch High-Reward Video Task",
        provider = "OptiPay Media",
        rewardCoins = 100,
        requiredSeconds = 25,
        category = TaskCategory.VIDEO_WATCH,
        targetUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
        instruction = "ভিডিওটি সম্পূর্ণ দেখে রিওয়ার্ড অর্জন করুন।"
    )
)

data class DailyStreakDay(
    val dayNumber: Int,
    val rewardCoins: Int,
    val isClaimed: Boolean,
    val isToday: Boolean
)

data class VipSlotPackage(
    val level: Int,
    val title: String,
    val badge: String,
    val costCoins: Int = 0,
    val priceBdt: Int = 0,
    val multiplier: Double,
    val dailyVideoLimit: Int,
    val description: String,
    val highlightColorHex: Long = 0xFF3B82F6
)

fun getVipPackages(config: AdminAdConfig = AdminAdConfig()): List<VipSlotPackage> = listOf(
    VipSlotPackage(
        level = 1,
        title = "Level 1 • Free Default Slot",
        badge = "FREE / 5 TASKS",
        costCoins = 0,
        priceBdt = 0,
        multiplier = 1.0,
        dailyVideoLimit = config.slot1TaskLimit,
        description = "১.০x ইনকাম স্পিড • ${config.slot1TaskLimit}টি ডেইলি ভিডিও টাস্ক • ফ্রি ডিফল্ট স্লট",
        highlightColorHex = 0xFF10B981
    ),
    VipSlotPackage(
        level = 2,
        title = "Level 2 • Basic Slot",
        badge = "BASIC ৳500",
        costCoins = 0,
        priceBdt = config.slot2PriceBdt,
        multiplier = 1.5,
        dailyVideoLimit = config.slot2TaskLimit,
        description = "১.৫x ইনকাম বোনাস • ${config.slot2TaskLimit}টি ডেইলি ভিডিও টাস্ক • ৳${config.slot2PriceBdt}",
        highlightColorHex = 0xFF3B82F6
    ),
    VipSlotPackage(
        level = 3,
        title = "Level 3 • Silver VIP Slot",
        badge = "SILVER ৳1500",
        costCoins = 0,
        priceBdt = config.slot3PriceBdt,
        multiplier = 2.0,
        dailyVideoLimit = config.slot3TaskLimit,
        description = "২.০x ডাবল ইনকাম • ${config.slot3TaskLimit}টি ডেইলি ভিডিও টাস্ক • ৳${config.slot3PriceBdt}",
        highlightColorHex = 0xFF8B5CF6
    ),
    VipSlotPackage(
        level = 4,
        title = "Level 4 • Gold Master Slot",
        badge = "GOLD PRO ৳3000",
        costCoins = 0,
        priceBdt = config.slot4PriceBdt,
        multiplier = 3.0,
        dailyVideoLimit = config.slot4TaskLimit,
        description = "৩.০x মেগা স্পিড • ${config.slot4TaskLimit}টি ডেইলি ভিডিও টাস্ক • ৳${config.slot4PriceBdt}",
        highlightColorHex = 0xFFD97706
    )
)

val DEFAULT_VIP_PACKAGES = getVipPackages(AdminAdConfig())
