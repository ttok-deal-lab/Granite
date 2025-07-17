package com.warehouseinhand.slug.util

import java.text.NumberFormat
import java.util.Locale

// TODO : i18n
fun numberToCurrency(number: Long, dropUnder10000: Boolean = true): String {
    if (number == 0L) {
        return "0원"
    }
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)

    val valueList = mutableListOf<String>()
    var left = number

    val underMan = left % 10_000
    left /= 10_000
    val rangeInMan = (left) % 10_000
    left /= 10_000
    val rangeInEog = (left) % 10_000
    val overTrillion = left / 10_000

    if (overTrillion > 0) {
        valueList.add("${formatter.format(overTrillion)}조")
    }
    if (rangeInEog > 0) {
        valueList.add("${formatter.format(rangeInEog)}억")
    }
    if (rangeInMan > 0) {
        valueList.add("${formatter.format(rangeInMan)}만")
    }
    if (underMan > 0 && !dropUnder10000) {
        valueList.add(formatter.format(underMan))
    }
    return valueList.joinToString(separator = " ") + "원"
}

