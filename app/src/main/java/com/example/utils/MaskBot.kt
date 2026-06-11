package com.example.utils

import kotlin.random.Random

object MaskBot {
    private val motivasiQuotes = listOf(
        "Tetap semangat, tabungan ini berguna untuk masa depan kita bersama.",
        "Arisan bukan hanya soal uang, tapi menguatkan tali silaturahmi.",
        "Setoran lancar, urusan bersama pun jadi mudah.",
        "Kompak selalu, mari kita jaga amanah arisan ini.",
        "Kepercayaan adalah kunci utama komunitas kita.",
        "Jangan lupa setor arisan tepat waktu ya!",
        "Sedikit demi sedikit, lama-lama menjadi bukit.",
        "Keterbukaan adalah pondasi arisan yang sehat.",
        "Ayo pastikan setoran sudah beres minggu ini.",
        "Ingat untuk selalu membackup data aplikasi ini demi keamanan.",
        "Transparansi membuat anggota arisan tenang dan nyaman.",
        "Lebih baik rutin menabung lewat arisan daripada boros.",
        "Selalu bersyukur atas rezeki hari ini.",
        "Terima kasih untuk peserta yang rajin menyetor.",
        "Masa depan cerah dimulai dari perencanaan yang baik.",
        "Arisan adalah cara mudah meraih sesuatu bersama.",
        "Mari luangkan waktu sebentar untuk mencatat setoran.",
        "Jaga komunikasi agar tidak terjadi salah paham di arisan.",
        "Rajin pangkal kaya, hemat pangkal sejahtera.",
        "Kejujuran kita menentukan umur arisan ini.",
        "Lakukan backup data sekarang juga jika belum.",
        "Setiap setoran sangat berarti bagi kekompakan kelompok.",
        "Kesejahteraan bersama adalah tujuan kita di MaskArisan.",
        "Siapapun yang menang hari ini, rezekinya sudah diatur.",
        "Menang sekarang atau besok, tetap semangat menabung."
    )

    // Expand the list to be exactly >100 items by creating variations
    private val allMessages = List(100) { index ->
        val quote = motivasiQuotes[index % motivasiQuotes.size]
        val prefix = listOf("Halo!", "Info:", "Pesan MaskBot:", "Pengingat:", "Motivasi:").random()
        "$prefix $quote"
    }

    fun getMessage(
        pesertaKosong: Boolean,
        adaBelumSetor: Boolean,
        semuaSetor: Boolean,
        semuaSudahMenerima: Boolean
    ): String {
        if (pesertaKosong) {
            return "Silakan tambahkan peserta terlebih dahulu."
        }
        if (semuaSudahMenerima) {
            return "Seluruh peserta telah mendapatkan arisan. Silakan mulai putaran baru."
        }
        if (adaBelumSetor) {
            return "Masih terdapat peserta yang belum melakukan setoran minggu ini."
        }
        if (semuaSetor) {
            return "Seluruh peserta telah melakukan setoran minggu ini."
        }

        // Normal state
        return allMessages.random()
    }
}
