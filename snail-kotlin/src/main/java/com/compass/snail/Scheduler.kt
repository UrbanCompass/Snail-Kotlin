// Copyright © 2018 Compass. All rights reserved.

package com.compass.snail

import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.concurrent.timerTask

internal class Scheduler(private val delay: Long, private val repeats: Boolean = true) {
    val event = Observable<Unit?>()
    private var timer: Timer? = null

    fun start() {
        stop()
        timer = Timer()
        timer?.scheduleAtFixedRate(0, delay) {
            event.next(null)
            if (!repeats) {
                stop()
            }
        }
    }

    fun stop() {
        timer?.cancel()
        timer = null
    }
}
