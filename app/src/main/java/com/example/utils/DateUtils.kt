package com.example.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatTanggal(timestamp: Long, format: String = "dd MMM yyyy HH:mm"): String {
        val sdf = SimpleDateFormat(format, Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }
    
    fun getCurrentWeekStartEnd(): Pair<Long, Long> {
        // Simplified start/end of the current week just based on 1 week range relative to now for simplicity,
        // or a strict calendar Sunday to Saturday. Using a strict calendar:
        val calendar = java.util.Calendar.getInstance()
        calendar.firstDayOfWeek = java.util.Calendar.MONDAY
        calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY)
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        val startOfWeek = calendar.timeInMillis
        
        calendar.add(java.util.Calendar.DAY_OF_YEAR, 6)
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
        calendar.set(java.util.Calendar.MINUTE, 59)
        calendar.set(java.util.Calendar.SECOND, 59)
        val endOfWeek = calendar.timeInMillis
        
        return Pair(startOfWeek, endOfWeek)
    }
}
