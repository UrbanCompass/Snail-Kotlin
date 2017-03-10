//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

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
    fun testOnNext() {
        val strings = mutableListOf<String>()
        subject?.next("1")
        subject?.next("2")
        subject?.subscribe(next = { strings.add(it) })
        assertEquals(2, strings.size)
        assertEquals("1", strings[0])
        assertEquals("2", strings[1])
    }

    @Test
    fun testMultipleSubscribers() {
        val a = mutableListOf<String>()
        val b = mutableListOf<String>()
        subject?.next("1")
        subject?.next("2")
        subject?.subscribe(next = { a.add(it) })
        subject?.next("3")
        subject?.subscribe(next = { b.add(it) })
        assertEquals("1", a[0])
        assertEquals("2", b[0])
        assertEquals(2, b.size)
    }
}