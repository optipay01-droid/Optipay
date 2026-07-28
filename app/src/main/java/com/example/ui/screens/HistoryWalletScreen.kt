package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.ui.components.AnimatedCoinCounter
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.EarningEntity
import com.example.data.db.UserProfileEntity
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoseError
import com.example.ui.theme.RoyalIndigo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton

@Composable
fun HistoryWalletScreen(
    profile: UserProfileEntity,
    transactions: List<EarningEntity>,
    onCopyReferral: (code: String) -> Unit,
    onOpenAuthDialog: (isSignUp: Boolean) -> Unit = {},
    onLogout: () -> Unit = {},
    onOpenAdminLogin: () -> Unit = {}
) {
    var filterType by remember { mutableStateOf("All") }
    val filters = listOf("All", "Earned", "Payouts")

    val filteredList = when (filterType) {
        "Earned" -> transactions.filter { it.coins > 0 }
        "Payouts" -> transactions.filter { it.coins < 0 }
        else -> transactions
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("history_wallet_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(com.example.ui.theme.SleekRoseContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = com.example.ui.theme.SleekPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = if (profile.isLoggedIn) profile.userName else "Guest User",
                                    color = com.example.ui.theme.SleekTextMain,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = if (profile.isLoggedIn) "🇧🇩 ${profile.userContact}" else "Not Logged In • Sign Up Below",
                                    color = com.example.ui.theme.SleekPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = com.example.ui.theme.SleekRoseContainer,
                            border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
                            contentColor = com.example.ui.theme.SleekDarkTerracotta
                        ) {
                            AnimatedCoinCounter(
                                targetCoins = profile.coinBalance,
                                suffix = " COINS",
                                color = com.example.ui.theme.SleekDarkTerracotta,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Login / Sign Up or Log Out Actions
                    if (profile.isLoggedIn) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFD1FAE5),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981))
                            ) {
                                Text(
                                    text = "✓ Logged In (সংযুক্ত)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }

                            Button(
                                onClick = onLogout,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp).testTag("logout_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "Log Out",
                                    modifier = Modifier.size(14.dp).padding(end = 4.dp)
                                )
                                Text("Log Out (সাইন আউট)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onOpenAuthDialog(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.SleekPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).height(38.dp).testTag("profile_signup_button")
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp).padding(end = 4.dp))
                                Text("Sign Up (সাইন আপ)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { onOpenAuthDialog(false) },
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekPrimary),
                                modifier = Modifier.weight(1f).height(38.dp).testTag("profile_login_button")
                            ) {
                                Icon(Icons.Default.Login, contentDescription = null, tint = com.example.ui.theme.SleekPrimary, modifier = Modifier.size(16.dp).padding(end = 4.dp))
                                Text("Log In (লগইন)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = com.example.ui.theme.SleekPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Grid Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WalletStatBox(
                            title = "Total Earned",
                            value = "${profile.totalEarnedCoins}",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        WalletStatBox(
                            title = "Ads Watched",
                            value = "${profile.totalAdsWatched}",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        WalletStatBox(
                            title = "Paid Out",
                            value = "$${profile.totalPayoutsUsd}",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Referral Box
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = com.example.ui.theme.SleekRoseContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Your Referral Code",
                                    color = com.example.ui.theme.SleekTextMuted,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = profile.referralCode,
                                    color = com.example.ui.theme.SleekDarkTerracotta,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = com.example.ui.theme.SleekPrimary,
                                    contentColor = Color.White,
                                    modifier = Modifier.clickable { onCopyReferral(profile.referralCode) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "COPY",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Comprehensive User Profile Account Details Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_details_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = com.example.ui.theme.SleekPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Account Profile Details (প্রোফাইল তথ্য)",
                            color = com.example.ui.theme.SleekTextMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    ProfileDetailRow(
                        label = "Full Name (নাম)",
                        value = if (profile.isLoggedIn) profile.userName else "Guest User"
                    )
                    ProfileDetailRow(
                        label = "Contact (ইমেইল/নম্বর)",
                        value = if (profile.isLoggedIn) profile.userContact else "Not linked"
                    )
                    ProfileDetailRow(
                        label = "Account Status",
                        value = if (profile.isLoggedIn) "Verified Member ✅" else "Guest Mode 👤"
                    )
                    ProfileDetailRow(
                        label = "My Shareable Referral Code",
                        value = profile.referralCode
                    )
                    ProfileDetailRow(
                        label = "Joined With Referral Code",
                        value = if (profile.appliedReferralCode.isNotEmpty()) profile.appliedReferralCode else "None (সরাসরি যুক্ত)"
                    )
                    ProfileDetailRow(
                        label = "Current Coin Balance",
                        value = "${profile.coinBalance} Coins ($${String.format("%.2f", profile.coinBalance / 1000.0)})"
                    )
                    ProfileDetailRow(
                        label = "Daily Streak Level",
                        value = "🔥 Day ${profile.currentStreakDays} Streak"
                    )
                    ProfileDetailRow(
                        label = "Total Video Ads Watched",
                        value = "🎬 ${profile.totalAdsWatched} Ads"
                    )
                    ProfileDetailRow(
                        label = "Total Wheel Spins",
                        value = "🎡 ${profile.totalSpinsCompleted} Spins",
                        isLast = true
                    )
                }
            }
        }

        // History Section Header & Filter Tabs
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = com.example.ui.theme.SleekPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Activity Log",
                            color = com.example.ui.theme.SleekTextMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filters) { f ->
                            val isSelected = filterType == f
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) com.example.ui.theme.SleekPrimary else com.example.ui.theme.SleekCardBg,
                                contentColor = if (isSelected) Color.White else com.example.ui.theme.SleekTextMuted,
                                border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder) else null,
                                modifier = Modifier.clickable { filterType = f }
                            ) {
                                Text(
                                    text = f,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // History Transactions List
        items(filteredList) { tx ->
            TransactionRowItem(tx = tx)
        }
    }
}

@Composable
fun WalletStatBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = com.example.ui.theme.SleekRoseContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = com.example.ui.theme.SleekTextMuted,
                fontSize = 10.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = com.example.ui.theme.SleekDarkTerracotta,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TransactionRowItem(tx: EarningEntity) {
    val isEarned = tx.coins > 0
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val dateStr = dateFormat.format(Date(tx.timestamp))

    Card(
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekCardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("transaction_item_${tx.id}")
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
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            if (isEarned) EmeraldSuccess.copy(alpha = 0.15f) else RoseError.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (tx.type) {
                            TransactionType.AD_WATCH -> Icons.Default.Videocam
                            TransactionType.SPIN_WHEEL -> Icons.Default.Casino
                            TransactionType.DAILY_STREAK -> Icons.Default.LocalFireDepartment
                            else -> if (isEarned) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
                        },
                        contentDescription = null,
                        tint = if (isEarned) EmeraldSuccess else RoseError,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = tx.title,
                        color = com.example.ui.theme.SleekTextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$dateStr • ${tx.description}",
                        color = com.example.ui.theme.SleekTextMuted,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isEarned) "+${tx.coins}" else "${tx.coins}",
                    color = if (isEarned) EmeraldSuccess else RoseError,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                if (tx.status == TransactionStatus.PENDING) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = com.example.ui.theme.SleekRoseContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder)
                    ) {
                        Text(
                            text = "PENDING",
                            color = com.example.ui.theme.SleekPrimary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = com.example.ui.theme.SleekTextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = com.example.ui.theme.SleekTextMain,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (!isLast) {
            androidx.compose.material3.HorizontalDivider(
                color = com.example.ui.theme.SleekBorder,
                thickness = 1.dp
            )
        }
    }
}
