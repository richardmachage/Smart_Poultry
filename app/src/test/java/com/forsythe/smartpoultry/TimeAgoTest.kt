package com.forsythe.smartpoultry

import com.forsythe.smartpoultry.utils.getTimeAgo
import junit.framework.Assert.assertEquals
import org.junit.Test

class TimeAgoTest {
    @Test
    fun testSecondsAgo() {
        val pastTime = System.currentTimeMillis() - 15000 // 15 seconds ago
        val result = getTimeAgo(pastTime)
        assertEquals("15 seconds ago", result)
    }

    @Test
    fun testMinutesAgo() {
        val pastTime = System.currentTimeMillis() - 120000 // 2 minutes ago
        val result = getTimeAgo(pastTime)
        assertEquals("2 minutes ago", result)
    }

    @Test
    fun testHoursAgo() {
        val pastTime = System.currentTimeMillis() - 7200000 // 2 hours ago
        val result = getTimeAgo(pastTime)
        assertEquals("2 hours ago", result)
    }

    @Test
    fun testOneDayAgo() {
        val pastTime = System.currentTimeMillis() - 86400000 // 1 day ago
        val result = getTimeAgo(pastTime)
        assertEquals("1 day ago", result)
    }

    @Test
    fun testMultipleDaysAgo() {
        val pastTime = System.currentTimeMillis() - 172800000 // 2 days ago
        val result = getTimeAgo(pastTime)
        assertEquals("2 days ago", result)
    }
}