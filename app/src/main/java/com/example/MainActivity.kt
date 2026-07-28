package com.example

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.notification.NotificationHelper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.filled.PersonAdd
import com.example.model.VipSlotPackage
import com.example.ui.components.AdPlayerDialog
import com.example.ui.components.AdminLoginModal
import com.example.ui.components.AdminSettingsModal
import com.example.ui.components.AuthModal
import com.example.ui.components.PayoutRedeemModal
import com.example.ui.components.SlotPaymentModal
import com.example.ui.screens.EarnHomeScreen
import com.example.ui.screens.HistoryWalletScreen
import com.example.ui.screens.RedeemStoreScreen
import com.example.ui.screens.SpinWheelScreen
import com.example.ui.theme.DarkNavyBg
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.WatchAndEarnTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Notification Channel
        NotificationHelper.createNotificationChannel(this)

        // Request POST_NOTIFICATIONS permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            WatchAndEarnTheme(darkTheme = false) {
                WatchAndEarnApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchAndEarnApp(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    if (uiState.showAdminDialog) {
        // FULL SCREEN STANDALONE ADMIN PANEL - NO USER UI SHOWN
        AdminSettingsModal(
            currentConfig = uiState.adminAdConfig,
            transactions = uiState.allRawTransactions,
            userAccounts = uiState.userAccounts,
            currentProfile = uiState.profile,
            tasks = uiState.taskOffers,
            onAddNewTask = { task -> viewModel.addNewTask(task) },
            onUpdateTask = { task -> viewModel.updateTask(task) },
            onDeleteTask = { taskId -> viewModel.deleteTask(taskId) },
            onUpdatePayoutStatus = { id, status, trxId -> viewModel.updatePayoutStatus(id, status, trxId) },
            onUpdateUserBalance = { userId, newBalance -> viewModel.updateUserCoinBalance(userId, newBalance) },
            onToggleUserBlocked = { userId, isBlocked -> viewModel.toggleUserBlockedStatus(userId, isBlocked) },
            onDeleteUserAccount = { userId -> viewModel.deleteUserAccount(userId) },
            onSendCheckInReminder = { viewModel.sendTestDailyCheckInReminder() },
            onSave = { rewardCoins, durationSeconds, enforceFullWatch, videoUrl, slot1Limit, slot2Price, slot2Limit, slot3Price, slot3Limit, slot4Price, slot4Limit, bkashNum, nagadNum, rocketNum, minPayoutBdt, coinsPerBdt ->
                viewModel.updateAdminConfig(
                    rewardCoins,
                    durationSeconds,
                    enforceFullWatch,
                    videoUrl,
                    slot1Limit,
                    slot2Price,
                    slot2Limit,
                    slot3Price,
                    slot3Limit,
                    slot4Price,
                    slot4Limit,
                    bkashNum,
                    nagadNum,
                    rocketNum,
                    minPayoutBdt,
                    coinsPerBdt
                )
            },
            onLogoutAdmin = { viewModel.logoutAdmin() },
            onDismiss = { viewModel.closeAdminDialog() }
        )
    } else {
        Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = com.example.ui.theme.SleekCanvasBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = com.example.ui.theme.SleekCanvasBg,
                    titleContentColor = com.example.ui.theme.SleekDarkTerracotta
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1784780615152),
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "OptiPay",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = com.example.ui.theme.SleekDarkTerracotta
                        )
                    }
                },
                actions = {
                    val animatedCoins by androidx.compose.animation.core.animateIntAsState(
                        targetValue = uiState.profile.coinBalance,
                        animationSpec = androidx.compose.animation.core.tween(
                            durationMillis = 1000,
                            easing = androidx.compose.animation.core.FastOutSlowInEasing
                        ),
                        label = "TopBarCoinCount"
                    )
                    val formattedCoins = java.text.NumberFormat.getNumberInstance(java.util.Locale.US).format(animatedCoins)
                    val usdEquivalent = String.format("%.2f", animatedCoins * 0.001)

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = com.example.ui.theme.SleekRoseContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { viewModel.setActiveTab(2) }
                            .testTag("top_coin_balance_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Coins",
                                tint = com.example.ui.theme.SleekPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$formattedCoins ($$usdEquivalent)",
                                color = com.example.ui.theme.SleekDarkTerracotta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // User Auth / Sign Up Button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (uiState.profile.isLoggedIn) Color(0xFFD1FAE5) else com.example.ui.theme.SleekRoseContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                if (uiState.profile.isLoggedIn) {
                                    viewModel.setActiveTab(3)
                                } else {
                                    viewModel.openAuthDialog(isSignUp = true)
                                }
                            }
                            .testTag("top_user_auth_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (uiState.profile.isLoggedIn) Icons.Default.Person else Icons.Default.PersonAdd,
                                contentDescription = "User Auth",
                                tint = if (uiState.profile.isLoggedIn) Color(0xFF047857) else com.example.ui.theme.SleekPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (uiState.profile.isLoggedIn) "Profile" else "Sign Up / Log In",
                                color = if (uiState.profile.isLoggedIn) Color(0xFF047857) else com.example.ui.theme.SleekDarkTerracotta,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = com.example.ui.theme.SleekCanvasBg,
                shadowElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder)
            ) {
                NavigationBar(
                    containerColor = com.example.ui.theme.SleekCanvasBg,
                    contentColor = com.example.ui.theme.SleekDarkTerracotta,
                    windowInsets = NavigationBarDefaults.windowInsets
                ) {
                    val navItems = listOf(
                        Triple(0, "Home", Icons.Default.Home),
                        Triple(1, "Spin", Icons.Default.Casino),
                        Triple(2, "Redeem", Icons.Default.CardGiftcard),
                        Triple(3, "Profile", Icons.Default.Person)
                    )

                    navItems.forEach { (index, label, icon) ->
                        val isSelected = uiState.activeTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setActiveTab(index) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) com.example.ui.theme.SleekPrimary else com.example.ui.theme.SleekTextMuted,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    color = if (isSelected) com.example.ui.theme.SleekDarkTerracotta else com.example.ui.theme.SleekTextMuted,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = com.example.ui.theme.SleekRoseContainer,
                                selectedIconColor = com.example.ui.theme.SleekPrimary,
                                unselectedIconColor = com.example.ui.theme.SleekTextMuted,
                                selectedTextColor = com.example.ui.theme.SleekDarkTerracotta,
                                unselectedTextColor = com.example.ui.theme.SleekTextMuted
                            ),
                            modifier = Modifier.testTag("nav_tab_$index")
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.activeTab) {
                0 -> EarnHomeScreen(
                    userCoinBalance = uiState.profile.coinBalance,
                    currentStreak = uiState.profile.currentStreakDays,
                    lastCheckInTimestamp = uiState.profile.lastCheckInTimestamp,
                    ads = viewModel.sampleAds,
                    tasks = viewModel.taskOffers,
                    adminAdConfig = uiState.adminAdConfig,
                    userVipLevel = uiState.profile.vipLevel,
                    onOpenAdmin = { viewModel.openAdminDialog() },
                    onWatchAdClick = { ad -> viewModel.startAdWatch(ad) },
                    onClaimCheckIn = { dayNum, coins -> viewModel.claimDailyCheckIn(dayNum, coins) },
                    onCompleteTask = { task -> viewModel.completeTaskOffer(task) },
                    onNavigateToSpin = { viewModel.setActiveTab(1) },
                    onUpgradeVipSlotPackage = { pkg -> viewModel.openSlotPaymentModal(pkg) }
                )
                1 -> SpinWheelScreen(
                    segments = viewModel.spinSegments,
                    spinAngle = uiState.spinAngle,
                    isSpinning = uiState.isSpinning,
                    winCoins = uiState.spinWinCoins,
                    totalSpinsCompleted = uiState.profile.totalSpinsCompleted,
                    lastSpinTimestamp = uiState.profile.lastSpinTimestamp,
                    liveWinners = viewModel.liveWinners.collectAsStateWithLifecycle().value,
                    onSpinClick = { viewModel.spinLuckyWheel() },
                    onDismissWinDialog = { viewModel.dismissSpinWinDialog() }
                )
                2 -> RedeemStoreScreen(
                    userCoinBalance = uiState.profile.coinBalance,
                    giftCards = viewModel.giftCards,
                    transactions = uiState.transactions,
                    adminConfig = uiState.adminAdConfig,
                    onSelectGiftCard = { card -> viewModel.openRedeemModal(card) },
                    onSelectDirectPayout = { method -> viewModel.openDirectPayoutModal(method) }
                )
                3 -> HistoryWalletScreen(
                    profile = uiState.profile,
                    transactions = uiState.transactions,
                    onCopyReferral = { code ->
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Referral Code", code)
                        clipboard.setPrimaryClip(clip)
                        viewModel.showToast("Referral code copied!")
                    },
                    onOpenAuthDialog = { isSignUp -> viewModel.openAuthDialog(isSignUp) },
                    onLogout = { viewModel.logoutUser() },
                    onOpenAdminLogin = { viewModel.openAdminLoginDialog() }
                )
            }

            // Main App Tab Back Handler
            val hasActiveOverlay = uiState.showAuthDialog ||
                uiState.showAdminLoginDialog ||
                uiState.activeAd != null ||
                uiState.selectedGiftCard != null ||
                uiState.selectedDirectPayoutMethod != null ||
                uiState.selectedSlotForPayment != null

            if (!hasActiveOverlay && uiState.canNavigateBackTab) {
                BackHandler {
                    viewModel.navigateBackTab()
                }
            }

            // User Auth Dialog Overlay (Sign Up / Log In)
            if (uiState.showAuthDialog) {
                BackHandler { viewModel.closeAuthDialog() }
                AuthModal(
                    initialTabIsSignUp = uiState.authDialogIsSignUp,
                    onSignUp = { name, contact, pass, confirmPass, refCode ->
                        viewModel.signUpUser(name, contact, pass, confirmPass, refCode)
                    },
                    onLogin = { contact, pass ->
                        viewModel.loginUser(contact, pass)
                    },
                    onDismiss = { viewModel.closeAuthDialog() }
                )
            }

            // Dedicated Admin Login Modal Overlay (Gmail & Password)
            if (uiState.showAdminLoginDialog) {
                BackHandler { viewModel.closeAdminLoginDialog() }
                AdminLoginModal(
                    onLogin = { gmail, pass -> viewModel.loginAdmin(gmail, pass) },
                    onDismiss = { viewModel.closeAdminLoginDialog() }
                )
            }

            // Ad Player Dialog Overlay
            uiState.activeAd?.let { activeAd ->
                BackHandler { viewModel.closeAdDialog(isExplicitCancel = true) }
                AdPlayerDialog(
                    ad = activeAd,
                    progressSeconds = uiState.adProgressSeconds,
                    isFinished = uiState.isAdFinished,
                    isMuted = uiState.isAdMuted,
                    onToggleMute = { viewModel.toggleAdMute() },
                    onClaimReward = { viewModel.claimAdReward() },
                    onClose = { viewModel.closeAdDialog(isExplicitCancel = true) }
                )
            }

            // Gift Card or Direct Cash/Recharge Payout Modal
            if (uiState.selectedGiftCard != null || uiState.selectedDirectPayoutMethod != null) {
                BackHandler { viewModel.closeRedeemModal() }
                PayoutRedeemModal(
                    giftCard = uiState.selectedGiftCard,
                    directMethod = uiState.selectedDirectPayoutMethod,
                    userCoinBalance = uiState.profile.coinBalance,
                    adminConfig = uiState.adminAdConfig,
                    onConfirmGiftCardRedeem = { account -> viewModel.processRedeem(account) },
                    onConfirmDirectPayout = { method, phone, amountBdt, coinCost ->
                        viewModel.submitDirectPayoutRequest(method, phone, amountBdt, coinCost)
                    },
                    onDismiss = { viewModel.closeRedeemModal() }
                )
            }

            // VIP Slot Upgrade Payment Modal
            uiState.selectedSlotForPayment?.let { selectedSlot ->
                BackHandler { viewModel.closeSlotPaymentModal() }
                SlotPaymentModal(
                    pkg = selectedSlot,
                    adminConfig = uiState.adminAdConfig,
                    onSubmitPayment = { method, senderNum, trxId ->
                        viewModel.submitSlotPayment(selectedSlot, method, senderNum, trxId)
                    },
                    onDismiss = { viewModel.closeSlotPaymentModal() }
                )
            }
        }
    }
}
}
