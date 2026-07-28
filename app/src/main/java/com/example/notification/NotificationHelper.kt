package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.R

object NotificationHelper {
    private const val CHANNEL_ID = "optipay_notifications"
    private const val CHANNEL_NAME = "OptiPay Task & Payout Notifications"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for payout approvals and daily task check-ins"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun triggerPayoutStatusNotification(context: Context, payoutTitle: String, isApproved: Boolean) {
        createNotificationChannel(context)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = if (isApproved) "Payout Approved! 🎉" else "Payout Update ⚠️"
        val body = if (isApproved) {
            "Your payout request for '$payoutTitle' was approved and money transferred!"
        } else {
            "Your payout request for '$payoutTitle' was rejected. Coins have been refunded."
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_app_icon_1784780615152)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    fun triggerSlotStatusNotification(context: Context, slotTitle: String, isApproved: Boolean) {
        createNotificationChannel(context)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = if (isApproved) "VIP Slot Upgrade Approved! 👑" else "Slot Payment Request Rejected ❌"
        val body = if (isApproved) {
            "Your payment for '$slotTitle' was verified! Your VIP slot and daily task limit have been activated!"
        } else {
            "Your payment submission for '$slotTitle' was rejected. Please verify payment details and try again."
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_app_icon_1784780615152)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    fun triggerDailyCheckInReminder(context: Context) {
        createNotificationChannel(context)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_app_icon_1784780615152)
            .setContentTitle("Daily Bonus Ready! 🎁")
            .setContentText("Claim your daily check-in reward now and earn free coins!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        manager.notify(1001, builder.build())
    }

    fun triggerNewTaskNotification(context: Context, taskTitle: String, taskCode: String) {
        createNotificationChannel(context)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_app_icon_1784780615152)
            .setContentTitle("New Task Available! 🎯 [$taskCode]")
            .setContentText("Complete '$taskTitle' now and earn coins instantly!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
