package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekCardBg
import com.example.ui.theme.SleekDarkTerracotta
import com.example.ui.theme.SleekPrimary
import com.example.ui.theme.SleekRoseContainer
import com.example.ui.theme.SleekTextMain
import com.example.ui.theme.SleekTextMuted
import com.example.util.StreakUtils

@Composable
fun DailyStreakCard(
    currentStreak: Int,
    lastCheckInTimestamp: Long = 0L,
    onClaimCheckIn: (dayNumber: Int, coinReward: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysDiff = remember(lastCheckInTimestamp) { StreakUtils.getCalendarDaysDifference(lastCheckInTimestamp) }
    val isClaimedToday = remember(lastCheckInTimestamp) { StreakUtils.isClaimedToday(lastCheckInTimestamp) }
    val activeStreakDay = remember(currentStreak, lastCheckInTimestamp) {
        StreakUtils.getEffectiveStreakDay(currentStreak, lastCheckInTimestamp)
    }
    val todayReward = remember(activeStreakDay) { StreakUtils.getRewardForDay(activeStreakDay) }

    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCardBg),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_streak_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = SleekPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Daily Check-In Streak 🗓️",
                            color = SleekTextMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (isClaimedToday) "Day $activeStreakDay Streak Claimed Today ✅" else "Day $activeStreakDay Ready to Claim 🔥",
                            color = if (isClaimedToday) EmeraldSuccess else SleekPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SleekRoseContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorder)
                ) {
                    Text(
                        text = "Day $activeStreakDay / 7",
                        color = SleekDarkTerracotta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle banner message
            if (isClaimedToday) {
                Text(
                    text = "✅ আজকের ডেইলি বোনাস ক্লেইম সফল হয়েছে! আগামীকাল আবার আসুন Day ${if (activeStreakDay >= 7) 1 else activeStreakDay + 1} বোনাসের জন্য।",
                    fontSize = 11.sp,
                    color = EmeraldSuccess,
                    fontWeight = FontWeight.Medium
                )
            } else if (daysDiff > 1 && lastCheckInTimestamp > 0) {
                Text(
                    text = "⚠️ আপনি আগের দিন চেক-ইন মিস করেছেন! তাই স্ট্রাইক রিসেট হয়ে আবার D1 থেকে শুরু হচ্ছে।",
                    fontSize = 11.sp,
                    color = Color(0xFFDC2626),
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "🔥 টানা ৭ দিন চেক-ইন করে D1 থেকে D7 পর্যন্ত বোনাস পয়েন্ট রিডিম করুন!",
                    fontSize = 11.sp,
                    color = SleekTextMuted,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 7 Day Calendar Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StreakUtils.STREAK_REWARDS.forEachIndexed { index, coins ->
                    val dayNum = index + 1
                    val isClaimed = if (isClaimedToday) {
                        dayNum <= activeStreakDay
                    } else {
                        dayNum < activeStreakDay
                    }
                    val isToday = !isClaimedToday && dayNum == activeStreakDay

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "D$dayNum",
                            color = if (isToday) SleekPrimary else if (isClaimed) EmeraldSuccess else SleekTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isClaimed -> EmeraldSuccess
                                        isToday -> SleekPrimary
                                        else -> SleekRoseContainer
                                    }
                                )
                                .border(
                                    width = if (isToday) 2.dp else 0.dp,
                                    color = if (isToday) SleekDarkTerracotta else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable(enabled = isToday) {
                                    onClaimCheckIn(activeStreakDay, todayReward)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isClaimed) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Claimed",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = "+$coins",
                                    color = if (isToday) Color.White else SleekDarkTerracotta,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onClaimCheckIn(activeStreakDay, todayReward) },
                enabled = !isClaimedToday,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekPrimary,
                    disabledContainerColor = EmeraldSuccess.copy(alpha = 0.85f),
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("claim_daily_checkin_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isClaimedToday) Icons.Default.Check else Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isClaimedToday) "CLAIMED TODAY (DAY $activeStreakDay DONE) ✅" else "CLAIM DAY $activeStreakDay BONUS (+$todayReward COINS)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
