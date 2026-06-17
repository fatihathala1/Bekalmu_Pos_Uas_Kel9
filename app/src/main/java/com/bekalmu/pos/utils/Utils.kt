package com.bekalmu.pos.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object CurrencyUtils {
    private val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    fun format(amount: Long): String {
        return formatter.format(amount).replace("Rp", "Rp").replace(",00", "")
    }

    fun formatShort(amount: Long): String {
        return when {
            amount >= 1_000_000 -> "Rp${amount / 1_000_000}jt"
            amount >= 1_000 -> "Rp${amount / 1_000}rb"
            else -> "Rp$amount"
        }
    }
}

object DateUtils {
    fun getStartOfDay(date: Date = Date()): Long {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getEndOfDay(date: Date = Date()): Long {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    fun getStartOfMonth(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    fun formatMonthYear(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    fun generateOrderNumber(): String {
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
        return "ORD-${sdf.format(Date()).takeLast(6)}"
    }
}
