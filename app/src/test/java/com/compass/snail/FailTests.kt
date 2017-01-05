//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import com.compass.snail.snail.Fail
import com.compass.snail.snail.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FailTests {
    private var subject: Observable<String>? = null
    private var strings: MutableList<String>? = null
    private var error: Throwable? = null

    @Before
    fun before() {
        subject = Fail(RuntimeException())
        strings = mutableListOf()
    }

    @Test
    fun testEvent() {
        var more: MutableList<String> = mutableListOf()
        subject?.subscribe { event ->
            event.next?.let { string -> more.add(string) }
        }
        subject?.next("1")
        assertEquals(0, more.size)
    }

    @Test
    fun testOnNext() {
        var more: MutableList<String> = mutableListOf()
        subject?.subscribeOn(next = { string ->
            more.add(string)
        })
        subject?.next("1")
        assertEquals(0, more.size)
    }

    @Test
    fun testOnError() {
        subject?.subscribeOn(error = { error ->
            this.error = error
        })
        assert(this.error is RuntimeException)
    }
}