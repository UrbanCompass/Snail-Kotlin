// Copyright © 2018 Compass. All rights reserved.

package com.compass.snail

import java.util.Timer
import kotlin.concurrent.timerTask


internal class Scheduler(private val delay: Double, private val repeats: Boolean = true) {
    val event = Observable<Unit?>()
    private var timer: Timer? = null

    fun onNext() {
        event.next(null)
    }

    fun start() {
        stop()
        timer = Timer()
        val period: Long = if(repeats) 0.0001.toLong() else 0
        timer?.schedule(timerTask { onNext() }, 0, period)
    }

    fun stop() {
        timer?.cancel()
    }
}
