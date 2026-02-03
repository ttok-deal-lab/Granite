package com.warehouseinhand.slug.util

fun String.extractDateFromDateAndTime(): String = this.substringBefore(" ")

fun String.extractTimeHHmmss(): String =
    this.substringAfter(" ", "")
fun String.extractTimeHHmm(): String =
    this.substringAfter(" ", "").substringBeforeLast(":", "")