package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AdminAdConfig
import com.example.model.GiftCardOffer
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekCardBg
import com.example.ui.theme.SleekDarkTerracotta
import com.example.ui.theme.SleekPrimary
import com.example.ui.theme.SleekRoseContainer
import com.example.ui.theme.SleekTextMain
import com.example.ui.theme.SleekTextMuted

@Composable
fun PayoutRedeemModal(
    giftCard: GiftCardOffer? = null,
    directMethod: String? = null,
    userCoinBalance: Int,
    adminConfig: AdminAdConfig = AdminAdConfig(),
    onConfirmGiftCardRedeem: (accountInput: String) -> Unit = {},
    onConfirmDirectPayout: (methodName: String, phone: String, amountBdt: Int, coinCost: Int) -> Unit = { _, _, _, _ -> },
    onDismiss: () -> Unit
) {
    var phoneOrAccountInput by remember { mutableStateOf("") }
    
    // Direct payout options
    val defaultAmountBdt = adminConfig.minPayoutBdt
    var selectedAmountBdtInput by remember { mutableStateOf(defaultAmountBdt.toString()) }
    
    val selectedAmountBdt = selectedAmountBdtInput.toIntOrNull() ?: defaultAmountBdt
    val requiredCoins = if (giftCard != null) giftCard.coinCost else (selectedAmountBdt * adminConfig.coinsPerBdt)
    val userBdtBalance = userCoinBalance / adminConfig.coinsPerBdt.toDouble()
    
    val isMinPayoutSatisfied = if (giftCard != null) true else selectedAmountBdt >= adminConfig.minPayoutBdt
    val hasEnoughCoins = userCoinBalance >= requiredCoins && isMinPayoutSatisfied

    val presetAmounts = listOf(20, 50, 100, 200, 500)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = com.example.ui.theme.SleekCanvasBg,
            border = BorderStroke(1.dp, SleekBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("payout_redeem_modal")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SleekRoseContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (giftCard != null) Icons.Default.CardGiftcard else Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = SleekDarkTerracotta,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = giftCard?.title ?: "${directMethod ?: "Payout"} Request",
                                color = SleekTextMain,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "পেমেন্ট পাওয়ার রিকোয়েস্ট উইথড্রয়াল",
                                color = SleekTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SleekDarkTerracotta
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // User Balance Info Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = SleekCardBg),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, SleekBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "আপনার বর্তমান ব্যালেন্স:",
                                color = SleekTextMuted,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "৳${String.format("%.2f", userBdtBalance)} BDT ($userCoinBalance Coins)",
                                color = SleekPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }

                        if (giftCard == null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "সর্বনিম্ন পেআউট লিমিট:",
                                    color = SleekTextMuted,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "৳${adminConfig.minPayoutBdt} BDT (${adminConfig.minPayoutBdt * adminConfig.coinsPerBdt} Coins)",
                                    color = SleekDarkTerracotta,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Direct payout amount selection
                if (giftCard == null) {
                    Text(
                        text = "টাকার পরিমাণ সিলেক্ট করুন (BDT):",
                        color = SleekDarkTerracotta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presetAmounts.forEach { amount ->
                            val isSelected = selectedAmountBdtInput == amount.toString()
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) SleekPrimary else SleekRoseContainer,
                                border = BorderStroke(1.dp, if (isSelected) SleekPrimary else SleekBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedAmountBdtInput = amount.toString() }
                            ) {
                                Text(
                                    text = "৳$amount",
                                    color = if (isSelected) Color.White else SleekDarkTerracotta,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = selectedAmountBdtInput,
                        onValueChange = { selectedAmountBdtInput = it.filter { c -> c.isDigit() } },
                        label = { Text("অন্যান্য পরিমাণ (৳ BDT)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPrimary,
                            unfocusedBorderColor = SleekBorder,
                            focusedTextColor = SleekTextMain,
                            unfocusedTextColor = SleekTextMain
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Account / Phone Number Field
                Text(
                    text = when {
                        giftCard != null -> "Payout Destination Email / Account ID:"
                        directMethod?.contains("Recharge", ignoreCase = true) == true -> "মোবাইল রিচার্জ নম্বর লিখুন:"
                        else -> "$directMethod পার্সোনাল নম্বর লিখুন:"
                    },
                    color = SleekDarkTerracotta,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = phoneOrAccountInput,
                    onValueChange = { phoneOrAccountInput = it },
                    placeholder = {
                        Text(
                            text = when {
                                giftCard != null -> "user@email.com"
                                else -> "017XXXXXXXX"
                            },
                            color = SleekTextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = SleekPrimary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (giftCard != null) KeyboardType.Text else KeyboardType.Phone
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SleekPrimary,
                        unfocusedBorderColor = SleekBorder,
                        focusedTextColor = SleekTextMain,
                        unfocusedTextColor = SleekTextMain
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("payout_account_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Summary Cost / Minimum Error Banner
                if (!isMinPayoutSatisfied) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEE2E2),
                        border = BorderStroke(1.dp, Color(0xFFEF4444)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "❌ সর্বনিম্ন ৳${adminConfig.minPayoutBdt} BDT উইথড্র করা যাবে।",
                            color = Color(0xFFDC2626),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                } else if (!hasEnoughCoins) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEE2E2),
                        border = BorderStroke(1.dp, Color(0xFFEF4444)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "❌ পর্যাপ্ত কয়েন নেই! পেআউটের জন্য ${requiredCoins - userCoinBalance} টি কয়েন প্রয়োজন।",
                            color = Color(0xFFDC2626),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("প্রয়োজনীয় কয়েন কস্ট:", color = SleekTextMuted, fontSize = 12.sp)
                        Text("$requiredCoins Coins", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Confirm Submit Button
                Button(
                    onClick = {
                        if (giftCard != null) {
                            onConfirmGiftCardRedeem(phoneOrAccountInput)
                        } else {
                            onConfirmDirectPayout(
                                directMethod ?: "Payout",
                                phoneOrAccountInput.trim(),
                                selectedAmountBdt,
                                requiredCoins
                            )
                        }
                    },
                    enabled = hasEnoughCoins && phoneOrAccountInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_payout_button")
                ) {
                    Text(
                        text = if (hasEnoughCoins) "SUBMIT PAYOUT ORDER 💸" else "INSUFFICIENT BALANCE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
