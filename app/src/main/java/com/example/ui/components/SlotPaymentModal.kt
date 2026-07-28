package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AdminAdConfig
import com.example.model.VipSlotPackage
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldDark
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekCardBg
import com.example.ui.theme.SleekDarkTerracotta
import com.example.ui.theme.SleekPrimary
import com.example.ui.theme.SleekRoseContainer
import com.example.ui.theme.SleekTextMain
import com.example.ui.theme.SleekTextMuted

@Composable
fun VipSlotDetailDialog(
    pkg: VipSlotPackage,
    userVipLevel: Int,
    isPendingApproval: Boolean = false,
    onBuyNowClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val isCurrentActive = userVipLevel == pkg.level
    val isUnlocked = userVipLevel >= pkg.level

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SleekCardBg),
            border = BorderStroke(1.dp, SleekBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("vip_slot_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(pkg.highlightColorHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = pkg.title,
                                fontWeight = FontWeight.Bold,
                                color = SleekDarkTerracotta,
                                fontSize = 16.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
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
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SleekTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price & Multiplier Highlight Banner
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SleekRoseContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("মূল্য (Price)", fontSize = 11.sp, color = SleekTextMuted)
                            Text(
                                text = if (pkg.level == 1) "ফ্রি (Free)" else "৳${pkg.priceBdt}",
                                fontWeight = FontWeight.Bold,
                                color = SleekPrimary,
                                fontSize = 18.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(SleekBorder)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("ইনকাম স্পিড", fontSize = 11.sp, color = SleekTextMuted)
                            Text(
                                text = "${pkg.multiplier}X",
                                fontWeight = FontWeight.Bold,
                                color = GoldDark,
                                fontSize = 18.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(SleekBorder)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("ডেইলি টাস্ক", fontSize = 11.sp, color = SleekTextMuted)
                            Text(
                                text = "${pkg.dailyVideoLimit}টি",
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "স্লটের বিশেষ সুবিধাসমূহ:",
                    fontWeight = FontWeight.Bold,
                    color = SleekDarkTerracotta,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                val features = listOf(
                    "প্রতিদিন সর্বোচ্চ ${pkg.dailyVideoLimit}টি ভিডিও টাস্ক সম্পন্ন করার সুযোগ",
                    "প্রতি ভিডিও টাস্কে ${pkg.multiplier}X বোনাস কয়েন রিওয়ার্ড",
                    "দ্রুততম পেমেন্ট প্রসেসিং ও অ্যাডমিন সাপোর্ট",
                    "আনলিমিটেড প্রতিদিনের স্পিন ও বোনাস অফার"
                )

                features.forEach { feat ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(EmeraldSuccess),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = feat,
                            fontSize = 12.sp,
                            color = SleekTextMain,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                when {
                    isCurrentActive -> {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = GoldDark,
                            contentColor = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "এই স্লটটি বর্তমানে একটিভ আছে (ACTIVE)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                    isPendingApproval -> {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF59E0B),
                            contentColor = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⏳ পেমেন্ট জমা দেওয়া হয়েছে! এডমিন ভেরিফিকেশন পেন্ডিং...",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 12.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                    isUnlocked -> {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = EmeraldSuccess,
                            contentColor = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "ইতোমধ্যে আনলক করা হয়েছে (UNLOCKED)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 12.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        Button(
                            onClick = onBuyNowClick,
                            colors = ButtonDefaults.buttonColors(containerColor = SleekPrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("buy_now_btn")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "এখনই কিনুন (৳${pkg.priceBdt})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SlotPaymentModal(
    pkg: VipSlotPackage,
    adminConfig: AdminAdConfig,
    onSubmitPayment: (method: String, senderNumber: String, trxId: String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedMethod by remember { mutableStateOf("bKash") }
    var senderNumber by remember { mutableStateOf("") }
    var trxId by remember { mutableStateOf("") }

    val currentNumber = when (selectedMethod) {
        "bKash" -> adminConfig.bkashNumber
        "Nagad" -> adminConfig.nagadNumber
        "Rocket" -> adminConfig.rocketNumber
        else -> adminConfig.bkashNumber
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SleekCardBg),
            border = BorderStroke(1.dp, SleekBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("slot_payment_modal")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SleekPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "পেমেন্ট সম্পন্ন করুন",
                                fontWeight = FontWeight.Bold,
                                color = SleekDarkTerracotta,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${pkg.title} • ৳${pkg.priceBdt}",
                                color = SleekTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SleekTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Payment Method Selector
                Text(
                    text = "পেমেন্ট মেথড নির্বাচন করুন:",
                    fontWeight = FontWeight.Bold,
                    color = SleekDarkTerracotta,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val methods = listOf(
                        Triple("bKash", "বিকাশ", Color(0xFFE2136E)),
                        Triple("Nagad", "নগদ", Color(0xFFF7921E)),
                        Triple("Rocket", "রকেট", Color(0xFF8C3494))
                    )

                    methods.forEach { (key, label, brandColor) ->
                        val isSelected = selectedMethod == key
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) brandColor else SleekRoseContainer,
                            border = BorderStroke(1.dp, if (isSelected) brandColor else SleekBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedMethod = key }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else SleekDarkTerracotta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Number Card with Copy
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SleekRoseContainer),
                    border = BorderStroke(1.dp, SleekBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "$selectedMethod Personal (Send Money) নম্বর:",
                                fontSize = 11.sp,
                                color = SleekTextMuted,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = currentNumber,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekPrimary
                            )
                        }

                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Admin Payment Number", currentNumber)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "$selectedMethod নম্বর কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SleekPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("کপি করুন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Instructions
                Text(
                    text = "১. ওপরের $selectedMethod নম্বরে ৳${pkg.priceBdt} টাকা Send Money করুন।\n" +
                           "২. সফলভাবে সেন্ড মানি করে আপনার নম্বর ও TrxID নিচে দিন।",
                    fontSize = 11.sp,
                    color = SleekTextMuted,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Sender Number Field
                OutlinedTextField(
                    value = senderNumber,
                    onValueChange = { senderNumber = it },
                    label = { Text("আপনার পেমেন্ট মোবাইল নম্বর") },
                    placeholder = { Text("017XXXXXXXX") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SleekPrimary) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPrimary,
                        unfocusedBorderColor = SleekBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Transaction ID Field
                OutlinedTextField(
                    value = trxId,
                    onValueChange = { trxId = it },
                    label = { Text("Transaction ID (TrxID)") },
                    placeholder = { Text("যেমন: 9J3K82L1") },
                    leadingIcon = { Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = SleekPrimary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPrimary,
                        unfocusedBorderColor = SleekBorder
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = SleekRoseContainer, contentColor = SleekDarkTerracotta),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Text("বাতিল", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (senderNumber.isBlank() || trxId.isBlank()) {
                                Toast.makeText(context, "অনুগ্রহ করে সেন্ডার নম্বর ও TrxID সঠিকভােব লিখুন!", Toast.LENGTH_SHORT).show()
                            } else {
                                onSubmitPayment(selectedMethod, senderNumber, trxId)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(44.dp)
                            .testTag("submit_slot_payment_btn")
                    ) {
                        Text("পেমেন্ট জমা দিন", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
