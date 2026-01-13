package com.warehouseinhand.slug.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

 fun calculateDaysLeft(
    salesDateTime: String,
    nowMillis: Long
): Int =
    runCatching {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val saleDate = LocalDateTime.parse(salesDateTime, formatter)
        val diffMillis =
            saleDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - nowMillis
        TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()
    }.getOrDefault(0)
