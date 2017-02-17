//  Copyright © 2016 Compass. All rights reserved.

package com.compass.compasslibrary.snail

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
    fun testOnNext() {
        var more: MutableList<String> = mutableListOf()
        subject?.subscribe(next = { more.add(it) })
        subject?.next("1")
        assertEquals(0, more.size)
    }

    @Test
    fun testOnError() {
        subject?.subscribe(error = { error = it })
        assert(error is RuntimeException)
    }
}
