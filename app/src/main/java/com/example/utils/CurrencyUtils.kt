package com.example.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    fun formatRupiah(amount: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(amount).replace("Rp", "Rp ")
    }
}
