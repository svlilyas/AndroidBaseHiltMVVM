package com.pi.androidbasehiltmvvm.core.common

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    /**
     * Transform Date String
     * dd.MM.yyyy -> yyyy-MM-dd
     */
    fun formatDate(date: String): String = try {
        val birthDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance().apply { time = birthDate!! }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        "$year-$month-$day"
    } catch (e: Exception) {
        "-"
    }

    /**
     * Transform Date String
     * dd.MM.yyyy HH:mm -> yyyy-MM-dd HH:mm
     */
    fun formatDateTime(date: String): String = try {
        val birthDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance().apply { time = birthDate!! }
        val year = calendar.get(Calendar.YEAR)
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val hour = String.format("%02d", calendar.get(Calendar.HOUR))
        val min = String.format("%02d", calendar.get(Calendar.MINUTE))
        "$year-$month-$day $hour:$min"
    } catch (e: Exception) {
        "-"
    }

    /**
     * Format Date formatted in yyyy-MM-dd'T'HH:mm:ss -> dd.MM.yyyy
     */
    fun formatGetDateTimeString(date: String): String = try {
        val dateString =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ss", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance().apply { time = dateString!! }
        val year = calendar.get(Calendar.YEAR)
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        "$day.$month.$year"
    } catch (e: Exception) {
        "-"
    }

    fun formatGetMonthName(date: String): String = try {
        val dateString =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)
        DateFormat.format("MMMM", dateString).toString()
    } catch (e: Exception) {
        "-"
    }

    fun formatGetYear(date: String): String = try {
        val dateString = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance().apply { time = dateString!! }
        calendar.get(Calendar.YEAR).toString()
    } catch (e: Exception) {
        "-"
    }

    fun formatGetTime(date: String): String = try {
        val dateString =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance().apply {
            time = dateString!!
        }
        val hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))
        val min = String.format("%02d", calendar.get(Calendar.MINUTE))
        "$hour:$min"
    } catch (e: Exception) {
        "-"
    }

    /**
     * Format Date formatted in yyyy-MM-dd'T'HH:mm:ss.SSSSSS -> dd.MM.yyyy HH:mm
     */
    fun formatServerTime(date: String): String = try {
        val dateString =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()).parse(date)
        val calendar = Calendar.getInstance().apply { time = dateString!! }
        val year = calendar.get(Calendar.YEAR)
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val hour = String.format("%02d", calendar.get(Calendar.HOUR))
        val min = String.format("%02d", calendar.get(Calendar.MINUTE))
        "$day.$month.$year $hour:$min"
    } catch (e: Exception) {
        "-"
    }
}
