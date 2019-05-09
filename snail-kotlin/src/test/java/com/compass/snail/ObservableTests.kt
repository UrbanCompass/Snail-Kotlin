//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
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
        val future = CompletableFuture<Boolean>()

        GlobalScope.async {
            subject?.subscribe(dispatcher = Dispatchers.Default, next = {
                future.complete(Thread.currentThread().name.toLowerCase().contains("default"))
            })
            subject?.next("1")
        }

        assert(future.get(2, TimeUnit.SECONDS))
    }

    @Test
    fun testOnDispatcher() {
        val future = CompletableFuture<Boolean>()

        val threadName = "pool-1-thread-1"
        val dispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
        val observable = Observable<Boolean>()
        observable.on(dispatcher).subscribe(next = {
            future.complete(Thread.currentThread().name.contains(threadName))
        })

        observable.next(true)
        assert(future.get(2, TimeUnit.SECONDS))
    }

    @Test
    fun testBlockSuccess() {
        val result = runBlocking {
            Just(1).block()
        }
        assertEquals(result.value, 1)
        assertNull(result.error)
    }

    @Test
    fun testBlockFail() {
        val result = runBlocking {
            Fail<Unit>(RuntimeException()).block()
        }
        assertNull(result.value)
        assert(result.error is RuntimeException)
    }

    @Test
    fun testBlockDone() {
        val observable = Observable<String>()
        observable.done()
        val result = runBlocking {
            observable.block()
        }
        assertNull(result.value)
        assertNull(result.error)
    }

    @Test
    fun testThrottle() {
        val observable = Observable<String>()
        val received = mutableListOf<String>()

        val future = CompletableFuture<Boolean>()
        val delayMs = 100L

        GlobalScope.async {
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

    @Test
    fun testDebounce() {
        val observable = Observable<String>()
        val received = mutableListOf<String>()

        val future = CompletableFuture<Boolean>()
        val delayMs = 200L

        GlobalScope.async {
            delay(delayMs / 2)
            observable.next("2")
            GlobalScope.async {
                delay(delayMs / 2)
                observable.next("3")
                GlobalScope.async {
                    delay(delayMs)
                    future.complete(true)
                }
            }
        }

        observable.debounce(delayMs).subscribe( next = {
            received.add(it)
        })

        observable.next("1")

        future.get(1, TimeUnit.SECONDS)

        assertEquals(1, received.count())
        assertEquals("3", received.first())
    }

    @Test
    fun testSkipFirst() {
        val observable = Observable<String>()
        var received = mutableListOf<String>()

        observable.skip(2).subscribe(next = {
            received.add(it)

            assertEquals(received.count(), 1)
            assertEquals(received.first(), "3")
        })

        observable.next("1")
        observable.next("2")
        observable.next("3")
    }

    @Test
    fun testSkipFirstInvalidArgument() {
        val observable = Observable<String>()

        observable.skip(-1).subscribe(error = {
            assert(it.localizedMessage == "Parameter value must be nonzero")
            assert(it is IllegalArgumentException)
        })

        observable.next("1")
    }
}
