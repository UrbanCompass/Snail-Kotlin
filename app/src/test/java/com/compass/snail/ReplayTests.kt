//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import com.compass.snail.snail.Replay
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ReplayTests {
    private var subject: Replay<String>? = null

    @Before
    fun before() {
        subject = Replay(2)
    }

    @Test
    fun testEvent() {
        var strings = mutableListOf<String>()
        subject?.next("1")
        subject?.subscribe { event ->
            event.next?.let { strings.add(it) }
        }
        assertEquals(1, strings.size)
        assertEquals("1", strings.firstOrNull())
    }

    @Test
    fun testOnNext() {
        var strings = mutableListOf<String>()
        subject?.next("1")
        subject?.next("2")
        subject?.subscribeOn(next = { strings.add(it) })
        assertEquals(2, strings.size)
        assertEquals("1", strings[0])
        assertEquals("2", strings[1])
    }

    @Test
    fun testMultipleSubscribers() {
        var a = mutableListOf<String>()
        var b = mutableListOf<String>()
        subject?.next("1")
        subject?.next("2")
        subject?.subscribeOn(next = { a.add(it) })
        subject?.next("3")
        subject?.subscribeOn(next = { b.add(it) })
        assertEquals("1", a[0])
        assertEquals("2", b[0])
        assertEquals(2, b.size)
    }
}