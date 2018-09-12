//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


class ObservableTests {
    private var subject: Observable<String>? = null
    private var strings: MutableList<String>? = null
    private var error: Throwable? = null
    private var done: Boolean? = null

    @Before
    fun before() {
        subject = Observable()
        strings = mutableListOf()
        error = null
        done = null
        subject?.subscribe(
            next = { strings?.add(it) },
            error = { error = it },
            done = { done = true }
        )
    }

    @Test
    fun testNext() {
        subject?.next("1")
        assertEquals("1", strings?.firstOrNull())
    }

    @Test
    fun testNextNull() {
        val unitSubject = Observable<Unit?>()
        var gotIt = false
        unitSubject.subscribe(next = {
            gotIt = true
        })
        unitSubject.next(null)
        assertEquals(true, gotIt)
    }

    @Test
    fun testDone() {
        subject?.next("1")
        subject?.next("2")
        subject?.done()
        subject?.next("3")

        assertEquals(2, strings?.size)
        assertEquals("1", strings?.get(0))
        assertEquals("2", strings?.get(1))
        assertEquals(true, done)
    }

    @Test
    fun testError() {
        subject?.next("1")
        subject?.next("2")
        subject?.error(RuntimeException())
        subject?.next("3")

        assertEquals(2, strings?.size)
        assertEquals("1", strings?.get(0))
        assertEquals("2", strings?.get(1))
        assert(error is RuntimeException)
    }

    @Test
    fun testRemoveSubscribers() {
        subject?.next("1")
        subject?.next("2")
        subject?.removeSubscribers()
        subject?.next("3")

        assertEquals(2, strings?.size)
        assertEquals("1", strings?.get(0))
        assertEquals("2", strings?.get(1))
    }

    @Test
    fun testMultipleSubscribers() {
        val more = mutableListOf<String>()
        subject?.subscribe(next = { more.add(it) })
        subject?.next("1")
        assertNotNull(more.firstOrNull())
        assertEquals(strings?.firstOrNull(), more.firstOrNull())
    }

    @Test
    fun testFiresStoppedEventOnSubscribeIfStopped() {
        subject?.error(RuntimeException())

        var oldError: Exception? = null
        subject?.subscribe(error = { error -> oldError = error as? Exception })
        assert(oldError is RuntimeException)
    }

    @Test
    fun testSubscribeOnRequestedDispatcher() {
        var future = CompletableFuture<Boolean>()

        async {
            subject?.subscribe(dispatcher = CommonPool, next = {
                future.complete(Thread.currentThread().name.toLowerCase().contains("commonpool"))
            })
            subject?.next("1")
        }

        assert(future.get(2, TimeUnit.SECONDS))
    }

    @Test
    fun testBlockSuccess() {
        val result = Just(1).block()
        assertEquals(result.value, 1)
        assertNull(result.error)
    }

    @Test
    fun testBlockFail() {
        val result = Fail<Unit>(RuntimeException()).block()
        assertNull(result.value)
        assert(result.error is RuntimeException)
    }

    @Test
    fun testBlockDone() {
        val observable = Observable<String>()
        observable.done()
        val result = observable.block()
        assertNull(result.value)
        assertNull(result.error)
    }

    @Test
    fun testThrottle() {
        val observable = Observable<String>()
        var received = mutableListOf<String>()

        var future = CompletableFuture<Boolean>()
        val delayMs = 100L

        async {
            delay(delayMs)
            future.complete(true)
        }

        observable.throttle(delayMs).subscribe( next = {
            received.add(it)
        })

        observable.next("1")
        observable.next("2")

        future.get(2, TimeUnit.SECONDS)

        assertEquals(received.count(), 1)
        assertEquals(received.first(), "2")
    }
}
