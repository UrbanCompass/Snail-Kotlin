// Copyright © 2018 Compass. All rights reserved.

package com.compass.snail

import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

internal class Scheduler(private val delay: Long, private val repeats: Boolean = true) {
    val event = Observable<Unit?>()
    private var timer: Timer? = null

    fun start() {
        stop()
        timer = Timer()
        timer?.scheduleAtFixedRate(delay, delay) {
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
