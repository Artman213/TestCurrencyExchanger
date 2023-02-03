package com.example.testcurrencyexchanger.util

import java.util.*
import kotlin.collections.HashMap

fun Double.multiply(course: Double): Double {
    return this * course
}


fun HashMap<String, Double>.sortByBalance(): HashMap<String, Double> {
    return this.map { it.key to it.value }.sortedWith { t, t2 -> t2.second.compareTo(t.second) }
        .toMap() as HashMap<String, Double>
}

fun Double.stringFormat2(): String {
    return String.format(
        Locale.ROOT,
        "%.2f",
        this
    )

}

