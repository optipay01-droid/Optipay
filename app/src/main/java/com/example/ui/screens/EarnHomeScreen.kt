package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.example.model.TaskCategory
import com.example.model.AdCampaign
import com.example.model.TaskOffer
import com.example.ui.components.DailyStreakCard
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalIndigo

import com.example.ui.components.AnimatedCoinCounter

import com.example.model.AdminAdConfig

import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import com.example.model.DEFAULT_VIP_PACKAGES
import com.example.model.VipSlotPackage
import com.example.model.getVipPackages

@Composable
fun EarnHomeScreen(
    userCoinBalance: Int,
    currentStreak: Int,
    lastCheckInTimestamp: Long = 0L,
    ads: List<AdCampaign>,
    tasks: List<TaskOffer>,
    adminAdConfig: AdminAdConfig = AdminAdConfig(),
    userVipLevel: Int = 1,
    onOpenAdmin: () -> Unit = {},
    onWatchAdClick: (AdCampaign?) -> Unit,
    onClaimCheckIn: (dayNumber: Int, coinReward: Int) -> Unit,
    onCompleteTask: (TaskOffer) -> Unit,
    onNavigateToSpin: () -> Unit,
    onUpgradeVipSlotPackage: (VipSlotPackage) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("earn_home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Sleek Balance Card (Material You Container)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekRoseContainer),
                shape = RoundedCornerShape(28.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("current_balance_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CURRENT BALANCE",
                            color = com.example.ui.theme.SleekPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AnimatedCoinCounter(
                            targetCoins = userCoinBalance,
                            color = com.example.ui.theme.SleekDarkTerracotta,
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Coins",
                            color = com.example.ui.theme.SleekPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = com.example.ui.theme.SleekCardBgAlt,
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Video Earn Active • Reward: ${adminAdConfig.videoRewardCoins} Coins",
                                color = com.example.ui.theme.SleekTextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Hero Banner Item
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner_1784780628640),
                        contentDescription = "Hero Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = com.example.ui.theme.SleekRoseContainer,
                            contentColor = com.example.ui.theme.SleekDarkTerracotta
                        ) {
                            Text(
                                text = "UNLIMITED REWARDS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Watch Videos & Earn Gold Coins",
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { onWatchAdClick(null) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = com.example.ui.theme.SleekPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("hero_watch_ad_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "WATCH NOW (+${adminAdConfig.videoRewardCoins} COINS)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3 VIP Slot Packages / Level System
        item {
            VipSlotsPackageSection(
                adminAdConfig = adminAdConfig,
                userVipLevel = userVipLevel,
                onUpgradeSlotPackage = onUpgradeVipSlotPackage
            )
        }

        // Daily Streak Item
        item {
            DailyStreakCard(
                currentStreak = currentStreak,
                lastCheckInTimestamp = lastCheckInTimestamp,
                onClaimCheckIn = onClaimCheckIn
            )
        }

        // Lucky Spin Entry Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekRoseContainer),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSpin() }
                    .testTag("spin_wheel_banner")
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(com.example.ui.theme.SleekPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Lucky Prize Wheel",
                                color = com.example.ui.theme.SleekDarkTerracotta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Spin 1x Every 4 Hours • Win up to 150 Jackpot Coins!",
                                color = com.example.ui.theme.SleekPrimary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = com.example.ui.theme.SleekPrimary,
                        contentColor = Color.White
                    ) {
                        Text(
                            text = "SPIN NOW",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Available Rewarded Ads Carousel
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = com.example.ui.theme.SleekPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Featured Video Offers",
                            color = com.example.ui.theme.SleekTextMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                    Text(
                        text = "Instant Payouts",
                        color = com.example.ui.theme.SleekPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(ads) { ad ->
                        AdCampaignCard(
                            ad = ad,
                            onClick = { onWatchAdClick(ad) }
                        )
                    }
                }
            }
        }

        // Offerwall & Serial Tasks Section
        item {
            var selectedCategoryFilter by remember { mutableStateOf<TaskCategory?>(null) }
            var showCompletedTab by remember { mutableStateOf(false) }

            val activeTasks = tasks.filter { !it.isCompleted }
            val completedTasks = tasks.filter { it.isCompleted }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = com.example.ui.theme.SleekPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Serial Task Hub",
                            color = com.example.ui.theme.SleekTextMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }

                    Text(
                        text = "${completedTasks.size}/${tasks.size} Done",
                        color = com.example.ui.theme.SleekTextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Active Tasks vs Completed Tasks Toggle Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (!showCompletedTab) com.example.ui.theme.SleekPrimary else com.example.ui.theme.SleekRoseContainer,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showCompletedTab = false }
                    ) {
                        Text(
                            text = "🎯 চলতি কাজ (${activeTasks.size})",
                            color = if (!showCompletedTab) Color.White else com.example.ui.theme.SleekDarkTerracotta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (showCompletedTab) EmeraldSuccess else com.example.ui.theme.SleekRoseContainer,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showCompletedTab = true }
                    ) {
                        Text(
                            text = "✅ সম্পন্ন তালিকা (${completedTasks.size})",
                            color = if (showCompletedTab) Color.White else com.example.ui.theme.SleekDarkTerracotta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category Filter Chips
                val displayedTabTasks = if (!showCompletedTab) activeTasks else completedTasks

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedCategoryFilter == null) com.example.ui.theme.SleekPrimary else com.example.ui.theme.SleekRoseContainer,
                            modifier = Modifier.clickable { selectedCategoryFilter = null }
                        ) {
                            Text(
                                text = "All (${displayedTabTasks.size})",
                                color = if (selectedCategoryFilter == null) Color.White else com.example.ui.theme.SleekDarkTerracotta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    items(TaskCategory.values()) { cat ->
                        val count = displayedTabTasks.count { it.category == cat }
                        val isSelected = selectedCategoryFilter == cat
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) Color(cat.badgeColorHex) else com.example.ui.theme.SleekRoseContainer,
                            modifier = Modifier.clickable { selectedCategoryFilter = cat }
                        ) {
                            Text(
                                text = "${cat.displayName} ($count)",
                                color = if (isSelected) Color.White else com.example.ui.theme.SleekDarkTerracotta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val filteredTasks = displayedTabTasks
                    .filter { selectedCategoryFilter == null || it.category == selectedCategoryFilter }
                    .sortedBy { it.serialNumber }

                if (filteredTasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (!showCompletedTab) "বর্তমানে কোন নতুন কাজ বাকি নেই! 🎉" else "এখনো কোন কাজ সম্পন্ন করা হয়নি।",
                            color = com.example.ui.theme.SleekTextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    filteredTasks.forEach { task ->
                        TaskOfferCard(
                            task = task,
                            onComplete = { onCompleteTask(task) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AdCampaignCard(
    ad: AdCampaign,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekCardBg),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() }
            .testTag("ad_card_${ad.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(ad.videoBgColor),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Watch",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "${ad.durationSeconds}s",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = ad.title,
                color = com.example.ui.theme.SleekTextMain,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1
            )

            Text(
                text = ad.sponsorName,
                color = com.example.ui.theme.SleekTextMuted,
                fontSize = 12.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = com.example.ui.theme.SleekRoseContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder)
                ) {
                    Text(
                        text = "+${ad.rewardCoins} Coins",
                        color = com.example.ui.theme.SleekDarkTerracotta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = EmeraldSuccess,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun TaskOfferCard(
    task: TaskOffer,
    onComplete: () -> Unit
) {
    var showExecutionDialog by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekCardBg),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Serial Number & Unique Task Code badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = com.example.ui.theme.SleekRoseContainer,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "#${task.serialNumber}",
                            color = com.example.ui.theme.SleekDarkTerracotta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = task.taskCode,
                            color = com.example.ui.theme.SleekDarkTerracotta.copy(alpha = 0.8f),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.sp
                        )
                    }
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(task.category.badgeColorHex),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = task.category.displayName,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "⏱️ ${task.requiredSeconds}s",
                            color = com.example.ui.theme.SleekTextMuted,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.title,
                        color = com.example.ui.theme.SleekTextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = task.instruction,
                        color = com.example.ui.theme.SleekTextMuted,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (task.isCompleted) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = EmeraldSuccess.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess)
                ) {
                    Text(
                        text = "Completed ✅",
                        color = EmeraldSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    )
                }
            } else {
                Button(
                    onClick = { showExecutionDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.SleekPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "+${task.rewardCoins} 🚀",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }

    if (showExecutionDialog) {
        TaskExecutionDialog(
            task = task,
            onCompleteTask = {
                showExecutionDialog = false
                onComplete()
            },
            onDismiss = { showExecutionDialog = false }
        )
    }
}

@Composable
fun TaskExecutionDialog(
    task: TaskOffer,
    onCompleteTask: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var secondsLeft by remember { mutableStateOf(task.requiredSeconds) }
    var timerRunning by remember { mutableStateOf(false) }
    var isLinkOpened by remember { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(timerRunning) {
        if (timerRunning) {
            while (secondsLeft > 0) {
                kotlinx.coroutines.delay(1000L)
                secondsLeft--
            }
            timerRunning = false
        }
    }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = com.example.ui.theme.SleekCanvasBg,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(task.category.badgeColorHex)
                    ) {
                        Text(
                            text = "[${task.taskCode}] Task #${task.serialNumber} • ${task.category.displayName}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "＋${task.rewardCoins} Coins",
                        color = EmeraldSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = task.title,
                    color = com.example.ui.theme.SleekTextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekRoseContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📌 নির্দেশাবলী / Instructions:",
                            color = com.example.ui.theme.SleekDarkTerracotta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.instruction,
                            color = com.example.ui.theme.SleekTextMain,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(task.targetUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // URL intent fallback
                        }
                        isLinkOpened = true
                        timerRunning = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.SleekPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text(
                        text = if (!isLinkOpened) "Open Link & Start Task 🌐" else "Re-open Link 🌐",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (timerRunning || secondsLeft > 0) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = com.example.ui.theme.SleekRoseContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (timerRunning) "⏱️ অপেক্ষা করুন: ${secondsLeft} সেকেন্ড বাকি..." else "টাস্ক সময়সীমা: ${task.requiredSeconds} সেকেন্ড",
                            color = com.example.ui.theme.SleekDarkTerracotta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(10.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.SleekRoseContainer),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Text("Cancel", color = com.example.ui.theme.SleekDarkTerracotta)
                    }

                    Button(
                        onClick = onCompleteTask,
                        enabled = isLinkOpened && secondsLeft == 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldSuccess,
                            disabledContainerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Text(
                            text = if (secondsLeft == 0 && isLinkOpened) "Claim Reward 🎁" else "Wait ${secondsLeft}s",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VipSlotsPackageSection(
    adminAdConfig: AdminAdConfig = AdminAdConfig(),
    userVipLevel: Int,
    onUpgradeSlotPackage: (VipSlotPackage) -> Unit
) {
    var selectedDetailPkg by remember { mutableStateOf<VipSlotPackage?>(null) }

    val dynamicPackages = remember(adminAdConfig) {
        getVipPackages(adminAdConfig)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekRoseContainer),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vip_slots_section")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(com.example.ui.theme.SleekPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "3 VIP Earning Slots (৩টি স্লট প্যাকেজ)",
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.SleekDarkTerracotta,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "পছন্দের স্লট অ্যাক্টিভ করে দৈনিক টাস্ক ও আয় বাড়ান",
                            color = com.example.ui.theme.SleekTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = com.example.ui.theme.SleekPrimary,
                    contentColor = Color.White
                ) {
                    Text(
                        text = "Level $userVipLevel Active",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                dynamicPackages.forEach { pkg ->
                    VipSlotItemCard(
                        pkg = pkg,
                        userVipLevel = userVipLevel,
                        onClickSlot = {
                            selectedDetailPkg = pkg
                        }
                    )
                }
            }
        }
    }

    // Detail Dialog Popup for Slot Details & Buy Now Button
    selectedDetailPkg?.let { pkg ->
        androidx.compose.ui.window.Dialog(onDismissRequest = { selectedDetailPkg = null }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(pkg.highlightColorHex),
                        contentColor = Color.White
                    ) {
                        Text(
                            text = pkg.badge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = pkg.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = com.example.ui.theme.SleekDarkTerracotta,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = pkg.description,
                        fontSize = 13.sp,
                        color = com.example.ui.theme.SleekTextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekRoseContainer),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "📌 স্লটের বিবরণ ও সুবিধা:",
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.SleekDarkTerracotta,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "• প্রতিদিন টাস্ক সীমা: ${pkg.dailyVideoLimit} টি ভিডিও",
                                fontSize = 12.sp,
                                color = com.example.ui.theme.SleekTextMain
                            )
                            Text(
                                text = "• স্লট মূল্য: ${if (pkg.priceBdt == 0) "সম্পূর্ণ ফ্রি (Free)" else "৳${pkg.priceBdt} BDT"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.SleekPrimary
                            )
                            if (pkg.priceBdt > 0) {
                                Text(
                                    text = "• বিকাশ, নগদ বা রকেটের মাধ্যমে ম্যানুয়াল পেমেন্ট জমা দিয়ে সহজেই অ্যাক্টিভ করতে পারবেন।",
                                    fontSize = 11.sp,
                                    color = com.example.ui.theme.SleekTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { selectedDetailPkg = null },
                            colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.SleekRoseContainer),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("বন্ধ করুন", color = com.example.ui.theme.SleekDarkTerracotta, fontWeight = FontWeight.Bold)
                        }

                        if (userVipLevel < pkg.level && pkg.priceBdt > 0) {
                            Button(
                                onClick = {
                                    val currentPkg = pkg
                                    selectedDetailPkg = null
                                    onUpgradeSlotPackage(currentPkg)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.SleekPrimary),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Buy Now (৳${pkg.priceBdt})", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VipSlotItemCard(
    pkg: VipSlotPackage,
    userVipLevel: Int,
    onClickSlot: () -> Unit
) {
    val isCurrentActive = userVipLevel == pkg.level
    val isUnlocked = userVipLevel >= pkg.level

    val cardBg = when {
        isCurrentActive -> Color(0xFFFEF3C7) // Gold highlights
        isUnlocked -> Color(0xFFECFDF5) // Green tint
        else -> com.example.ui.theme.SleekCanvasBg
    }

    val borderColor = when {
        isCurrentActive -> com.example.ui.theme.GoldDark
        isUnlocked -> EmeraldSuccess
        else -> com.example.ui.theme.SleekBorder
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickSlot() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(pkg.highlightColorHex),
                        contentColor = Color.White
                    ) {
                        Text(
                            text = pkg.badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pkg.title,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.SleekDarkTerracotta,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = pkg.description,
                    color = com.example.ui.theme.SleekTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            when {
                isCurrentActive -> {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = com.example.ui.theme.GoldDark,
                        contentColor = Color.White
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ACTIVE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                isUnlocked -> {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldSuccess,
                        contentColor = Color.White
                    ) {
                        Text(
                            text = "UNLOCKED",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
                else -> {
                    Button(
                        onClick = onClickSlot,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = com.example.ui.theme.SleekPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("unlock_slot_btn_${pkg.level}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (pkg.priceBdt > 0) "Buy ৳${pkg.priceBdt}" else "Free",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
