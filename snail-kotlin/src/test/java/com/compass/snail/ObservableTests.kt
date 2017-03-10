//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

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
}