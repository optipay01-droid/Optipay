package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.LiveWinner
import com.example.model.SpinSegment
import com.example.ui.components.LuckySpinWheel
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekCardBg
import com.example.ui.theme.SleekDarkTerracotta
import com.example.ui.theme.SleekPrimary
import com.example.ui.theme.SleekRoseContainer
import com.example.ui.theme.SleekTextMain
import com.example.ui.theme.SleekTextMuted
import kotlinx.coroutines.delay

@Composable
fun SpinWheelScreen(
    segments: List<SpinSegment>,
    spinAngle: Float,
    isSpinning: Boolean,
    winCoins: Int?,
    totalSpinsCompleted: Int,
    lastSpinTimestamp: Long = 0L,
    liveWinners: List<LiveWinner> = emptyList(),
    onSpinClick: () -> Unit,
    onDismissWinDialog: () -> Unit
) {
    // 4-Hour Cooldown Timer Ticker
    var remainingMs by remember(lastSpinTimestamp) {
        mutableStateOf(maxOf(0L, (lastSpinTimestamp + 4 * 60 * 60 * 1000L) - System.currentTimeMillis()))
    }

    LaunchedEffect(lastSpinTimestamp) {
        while (remainingMs > 0) {
            delay(1000L)
            remainingMs = maxOf(0L, (lastSpinTimestamp + 4 * 60 * 60 * 1000L) - System.currentTimeMillis())
        }
    }

    val isOnCooldown = remainingMs > 0
    val hours = remainingMs / (1000 * 60 * 60)
    val minutes = (remainingMs % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (remainingMs % (1000 * 60)) / 1000

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("spin_wheel_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Stats Bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SleekCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SleekRoseContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = SleekPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Lucky Wheel Master",
                                color = SleekTextMain,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Total Spins: $totalSpinsCompleted",
                                color = SleekPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SleekRoseContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SleekPrimary.copy(alpha = 0.5f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = SleekDarkTerracotta,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "1 SPIN / 4 HOURS",
                                color = SleekDarkTerracotta,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 4-Hour Cooldown Status Banner
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isOnCooldown) SleekRoseContainer else EmeraldSuccess.copy(alpha = 0.12f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isOnCooldown) SleekDarkTerracotta.copy(alpha = 0.3f) else EmeraldSuccess
                ),
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
                        Icon(
                            imageVector = if (isOnCooldown) Icons.Default.AccessTime else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isOnCooldown) SleekDarkTerracotta else EmeraldSuccess,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isOnCooldown) "৪ ঘণ্টা কুলডাউন একটিভ ⏳" else "স্পিন করার জন্য প্রস্তুত! 🎯",
                                color = if (isOnCooldown) SleekDarkTerracotta else EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (isOnCooldown) "প্রতি ৪ ঘণ্টা পর পর ১টি স্পিন করতে পারবেন।" else "এখনই SPIN চাপুন এবং কয়েন জিতে নিন!",
                                color = SleekTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (isOnCooldown) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SleekDarkTerracotta
                        ) {
                            Text(
                                text = String.format("%02dh %02dm %02ds", hours, minutes, seconds),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Wheel Component
        item {
            LuckySpinWheel(
                segments = segments,
                targetAngle = spinAngle,
                isSpinning = isSpinning,
                onSpinClick = onSpinClick
            )
        }

        // 150 Coin Jackpot Rules Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SleekCardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎁",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "প্রতিটি স্পিনে আকর্ষণীয় পুরষ্কার! এখনই স্পিন করুন এবং উপহার জিতে নিন। 🎁",
                        color = SleekDarkTerracotta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Animated Real-Time Live Winners Feed
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SleekCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Pulsating Live Dot
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "LIVE WINNERS FEED",
                                color = SleekTextMain,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldSuccess.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "🔴 1,482 Online",
                                color = EmeraldSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (liveWinners.isEmpty()) {
                        Text(
                            text = "লাইভ বিজয়ী তালিকা লোড হচ্ছে...",
                            color = SleekTextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            liveWinners.take(6).forEach { winner ->
                                LiveWinnerRowItem(winner = winner)
                            }
                        }
                    }
                }
            }
        }
    }

    // Winner Celebration Dialog
    if (winCoins != null) {
        Dialog(onDismissRequest = onDismissWinDialog) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = SleekCardBg,
                border = androidx.compose.foundation.BorderStroke(2.dp, SleekPrimary),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(SleekPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "CONGRATULATIONS!",
                        color = SleekDarkTerracotta,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "YOU WON +$winCoins COINS!",
                        color = SleekPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "কয়েনগুলো সরাসরি আপনার ওয়ালেট ব্যালেন্সে যুক্ত করা হয়েছে।",
                        color = SleekTextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onDismissWinDialog,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SleekPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("claim_spin_reward_button")
                    ) {
                        Text(
                            text = "CLAIM & CLOSE 🎁",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveWinnerRowItem(winner: LiveWinner) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (winner.isJackpot) SleekRoseContainer else Color.White.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (winner.isJackpot) SleekDarkTerracotta else SleekBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (winner.isJackpot) SleekDarkTerracotta else SleekPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (winner.isJackpot) Icons.Default.EmojiEvents else Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = winner.userName,
                            color = SleekTextMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        if (winner.userName == "You" || winner.userName == "Guest User") {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SleekPrimary
                            ) {
                                Text(
                                    text = "YOU",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = winner.timeAgo,
                        color = SleekTextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (winner.isJackpot) SleekDarkTerracotta else EmeraldSuccess.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (winner.isJackpot) SleekDarkTerracotta else EmeraldSuccess
                )
            ) {
                Text(
                    text = if (winner.isJackpot) "150 JACKPOT ⭐" else "+${winner.coinsWon} Coins",
                    color = if (winner.isJackpot) Color.White else EmeraldSuccess,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
