package com.example.util

import java.util.Calendar

object StreakUtils {

    val STREAK_REWARDS = listOf(25, 30, 40, 50, 60, 75, 100)

    /**
     * Calculates the number of calendar days between lastCheckInTimestamp and currentTimestamp.
     * 0 -> Same calendar day (already claimed today)
     * 1 -> Yesterday (consecutive streak continues)
     * > 1 or < 0 -> Missed one or more days, or never claimed (streak resets)
     */
    fun getCalendarDaysDifference(lastTimestamp: Long, currentTimestamp: Long = System.currentTimeMillis()): Int {
        if (lastTimestamp <= 0L) return 999
        val calLast = Calendar.getInstance().apply {
            timeInMillis = lastTimestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val calNow = Calendar.getInstance().apply {
            timeInMillis = currentTimestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val diffMillis = calNow.timeInMillis - calLast.timeInMillis
        val days = (diffMillis / (24 * 60 * 60 * 1000L)).toInt()
        return if (days < 0) 0 else days
    }

    /**
     * Returns true if user has already claimed the daily check-in bonus today.
     */
    fun isClaimedToday(lastTimestamp: Long, currentTimestamp: Long = System.currentTimeMillis()): Boolean {
        return getCalendarDaysDifference(lastTimestamp, currentTimestamp) == 0
    }

    /**
     * Calculates the active day number (1 to 7) for today.
     * @param storedStreak The stored currentStreakDays from DB (1 to 7).
     * @param lastTimestamp The timestamp when last check-in occurred.
     */
    fun getEffectiveStreakDay(storedStreak: Int, lastTimestamp: Long, currentTimestamp: Long = System.currentTimeMillis()): Int {
        val daysDiff = getCalendarDaysDifference(lastTimestamp, currentTimestamp)
        return when {
            daysDiff == 0 -> {
                // Already claimed today: show current stored streak (1..7)
                if (storedStreak in 1..7) storedStreak else 1
            }
            daysDiff == 1 -> {
                // Claimed yesterday: next day in streak sequence!
                if (storedStreak >= 7) 1 else storedStreak + 1
            }
            else -> {
                // Missed a day or first check-in: reset streak to Day 1
                1
            }
        }
    }

    /**
     * Get the bonus reward coins for a specific day (1-7).
     */
    fun getRewardForDay(dayNumber: Int): Int {
        val index = (dayNumber - 1).coerceIn(0, STREAK_REWARDS.size - 1)
        return STREAK_REWARDS[index]
    }
}
