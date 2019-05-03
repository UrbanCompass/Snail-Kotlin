package com.compass.snail

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class SchedulerTests {
    @Test
    fun testRepeat() {
        val timer = Scheduler(100L)
        val future = CompletableFuture<Boolean>()
        var count = 0

        timer.event.subscribe(next = { count++ })
        timer.start()

        GlobalScope.async {
            delay(200)
            future.complete(true)
        }

        future.get(1, TimeUnit.SECONDS)

        assert(count == 3)
    }

    @Test
    fun testRepeatFalse() {
        val timer = Scheduler(100L, false)
        val future = CompletableFuture<Boolean>()
        var count = 0

        timer.event.subscribe(next = { count++ })
        timer.start()

        GlobalScope.async {
            delay(200)
            future.complete(true)
        }

        future.get(1, TimeUnit.SECONDS)

        assert(count == 1)
    }
}