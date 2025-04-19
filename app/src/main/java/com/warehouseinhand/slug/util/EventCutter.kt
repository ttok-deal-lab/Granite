package com.warehouseinhand.slug.util

class EventCutter(private val limitTimeMilliseconds: Long = DEFAULT_LIMIT_TIME) {
    private var lastTime: Long = 0L
    private val now: Long get() = System.currentTimeMillis()

    @Synchronized
    fun processEvent(event: () -> Unit) {
        if (now - lastTime >= limitTimeMilliseconds) {
            event.invoke()
            lastTime = now
        }
    }

    companion object {
        private const val DEFAULT_LIMIT_TIME = 300L
    }
}