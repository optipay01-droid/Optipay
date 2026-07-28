package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.EarningEntity
import com.example.model.AdminAdConfig
import com.example.model.GiftCardOffer
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.ui.components.AnimatedCoinCounter
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekCardBg
import com.example.ui.theme.SleekDarkTerracotta
import com.example.ui.theme.SleekPrimary
import com.example.ui.theme.SleekRoseContainer
import com.example.ui.theme.SleekTextMain
import com.example.ui.theme.SleekTextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PayoutChannel(
    val name: String,
    val subtitle: String,
    val badge: String,
    val colorHex: Long,
    val iconVector: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun RedeemStoreScreen(
    userCoinBalance: Int,
    giftCards: List<GiftCardOffer>,
    transactions: List<EarningEntity> = emptyList(),
    adminConfig: AdminAdConfig = AdminAdConfig(),
    onSelectGiftCard: (GiftCardOffer) -> Unit,
    onSelectDirectPayout: (methodName: String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Mobile Payout", "PayPal", "Amazon", "Crypto")

    val filteredCards = if (selectedCategory == "All" || selectedCategory == "Mobile Payout") {
        giftCards
    } else {
        giftCards.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    val payoutRequests = remember(transactions) {
        transactions.filter { it.type == TransactionType.PAYOUT_REDEEMED }
    }

    val minPayoutCoins = adminConfig.minPayoutBdt * adminConfig.coinsPerBdt
    val userBdtValue = userCoinBalance / adminConfig.coinsPerBdt.toDouble()
    val goalProgress = (userCoinBalance.toFloat() / minPayoutCoins.coerceAtLeast(1)).coerceIn(0f, 1f)

    val directPayoutChannels = listOf(
        PayoutChannel("bKash Personal", "ইনস্ট্যান্ট বিকাশ পার্সোনাল ক্যাশ আউট", "bKash", 0xFFE2136E, Icons.Default.AccountBalanceWallet),
        PayoutChannel("Nagad Personal", "ইনস্ট্যান্ট নগদ পার্সোনাল ক্যাশ আউট", "Nagad", 0xFFF7931E, Icons.Default.AccountBalanceWallet),
        PayoutChannel("Rocket Personal", "ইনস্ট্যান্ট রকেট পার্সোনাল ক্যাশ আউট", "Rocket", 0xFF8C3494, Icons.Default.AccountBalanceWallet),
        PayoutChannel("Mobile Recharge", "জিপি, বাংলালিংক, রবি, এয়ারটেল, টেলিটক", "Recharge", 0xFF0284C7, Icons.Default.PhoneAndroid)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("redeem_store_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Goal Tracker Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SleekCardBg),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, SleekBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = SleekPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "পেআউট ব্যালেন্স: ৳${String.format("%.2f", userBdtValue)} BDT",
                                    color = SleekTextMain,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                AnimatedCoinCounter(
                                    targetCoins = userCoinBalance,
                                    suffix = " / $minPayoutCoins Coins (সর্বনিম্ন ৳${adminConfig.minPayoutBdt})",
                                    color = SleekPrimary,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SleekRoseContainer,
                            border = BorderStroke(1.dp, SleekBorder),
                            contentColor = SleekDarkTerracotta
                        ) {
                            Text(
                                text = "${(goalProgress * 100).toInt()}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { goalProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = SleekPrimary,
                        trackColor = SleekRoseContainer,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (goalProgress >= 1f) "🎉 সর্বনিম্ন লিমিট পূর্ণ হয়েছে! নিচে থেকে বিকাশ, নগদ বা রিচার্জ এর মাধ্যমে টাকা তুলুন।" else "সর্বনিম্ন ৳${adminConfig.minPayoutBdt} (${minPayoutCoins} Coins) ব্যালেন্স হলে টাকা উইথড্র করা যাবে।",
                        color = SleekTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Direct Cash & Recharge Payout Channels Section
        item {
            Text(
                text = "💸 ক্যাশ পেমেন্ট উইথড্র অপশন (বিকাশ, নগদ, রকেট ও রিচার্জ)",
                color = SleekDarkTerracotta,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                directPayoutChannels.forEach { channel ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SleekCardBg),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, SleekBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectDirectPayout(channel.name) }
                            .testTag("payout_channel_${channel.badge}")
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
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(channel.colorHex),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = channel.iconVector,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = channel.name,
                                        color = SleekTextMain,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = channel.subtitle,
                                        color = SleekTextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { onSelectDirectPayout(channel.name) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SleekPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("উইথড্র", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Tabs
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🎁 গিফট কার্ড ও অন্যান্য পুরস্কার",
                color = SleekDarkTerracotta,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) SleekPrimary else SleekCardBg,
                        contentColor = if (isSelected) Color.White else SleekTextMuted,
                        border = if (!isSelected) BorderStroke(1.dp, SleekBorder) else null,
                        modifier = Modifier.clickable { selectedCategory = category }
                    ) {
                        Text(
                            text = category,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Gift Card Offers List
        items(filteredCards) { giftCard ->
            GiftCardOfferRowItem(
                giftCard = giftCard,
                userCoinBalance = userCoinBalance,
                onRedeemClick = { onSelectGiftCard(giftCard) }
            )
        }

        // My Payout Requests & Admin Status Section (Always Visible)
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📋 আমার পেআউট হিস্ট্রি ও স্ট্যাটাস (PAYOUT HISTORY)",
                    color = SleekDarkTerracotta,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SleekRoseContainer,
                    border = BorderStroke(1.dp, SleekBorder)
                ) {
                    Text(
                        text = "${payoutRequests.size} Requested",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SleekDarkTerracotta,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        if (payoutRequests.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SleekCardBg),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, SleekBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "আপনার কোনো পেন্ডিং বা পূর্বের পেআউট রিকোয়েস্ট নেই 📭",
                            fontWeight = FontWeight.Bold,
                            color = SleekTextMain,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "উপরে বিকাশ, নগদ, রকেট বা মোবাইল রিচার্জ থেকে উইথড্র করুন। এডমিন দ্রুত টাকা পাঠাবে!",
                            color = SleekTextMuted,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(payoutRequests) { payout ->
                PayoutRequestStatusCard(payout = payout)
            }
        }
    }
}

@Composable
fun PayoutRequestStatusCard(payout: EarningEntity) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(payout.timestamp) { dateFormat.format(Date(payout.timestamp)) }

    val (statusLabel, statusBg, statusTextColor) = when (payout.status) {
        TransactionStatus.PENDING -> Triple("⏳ এডমিন পেন্ডিং (AWAITING APPROVAL)", Color(0xFFFEF3C7), Color(0xFFD97706))
        TransactionStatus.COMPLETED -> Triple("✅ এপ্রুভড ও পেমেন্ট সম্পন্ন (APPROVED & PAID)", Color(0xFFD1FAE5), Color(0xFF059669))
        TransactionStatus.REJECTED -> Triple("❌ রিজেক্টেড ও কয়েন রিফান্ডেড (REJECTED & REFUNDED)", Color(0xFFFEE2E2), Color(0xFFDC2626))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCardBg),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, SleekBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("payout_status_card_${payout.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = payout.title,
                        color = SleekTextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = payout.description,
                        color = SleekTextMuted,
                        fontSize = 12.sp
                    )
                    if (payout.transactionTrxId.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFD1FAE5),
                            border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "Tnx ID / TrxID: ${payout.transactionTrxId}",
                                color = Color(0xFF059669),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "${payout.coins} Coins",
                    color = SleekDarkTerracotta,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    color = SleekTextMuted,
                    fontSize = 11.sp
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = statusBg,
                    border = BorderStroke(1.dp, statusTextColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusTextColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GiftCardOfferRowItem(
    giftCard: GiftCardOffer,
    userCoinBalance: Int,
    onRedeemClick: () -> Unit
) {
    val canAfford = userCoinBalance >= giftCard.coinCost

    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCardBg),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, SleekBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("giftcard_card_${giftCard.id}")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            when (giftCard.category) {
                                "PayPal" -> Color(0xFF003087)
                                "Amazon" -> Color(0xFF232F3E)
                                "Google Play" -> Color(0xFF0F9D58)
                                else -> SleekPrimary
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = giftCard.title,
                            color = SleekTextMain,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        if (giftCard.isPopular) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SleekRoseContainer,
                                border = BorderStroke(1.dp, SleekBorder),
                                contentColor = SleekDarkTerracotta
                            ) {
                                Text(
                                    text = "POPULAR",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${giftCard.coinCost} Coins • $${giftCard.usdValue} USD",
                        color = SleekPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = onRedeemClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canAfford) EmeraldSuccess else SleekRoseContainer,
                    contentColor = if (canAfford) Color.White else SleekTextMuted
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (canAfford) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (canAfford) "REDEEM" else "LOCKED",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
