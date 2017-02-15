//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import com.compass.snail.snail.Just
import org.junit.Assert.assertEquals
import org.junit.Test

class JustTests {
    @Test
    fun testJustOnNext() {
        var result: Int? = null
        val subject = Just(1)
        subject.subscribeOn(next = { value ->
            result = value
        })
        assertEquals(1, result)
    }

    @Test
    fun testJustEvent() {
        var result: Int? = null
        val subject = Just(1)
        subject.subscribe { event ->
            event.next?.let { result = it }
        }
        assertEquals(1, result)
    }
}