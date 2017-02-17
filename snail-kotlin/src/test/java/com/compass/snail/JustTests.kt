//  Copyright © 2016 Compass. All rights reserved.

package com.compass.compasslibrary.snail

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JustTests {
    @Test
    fun testJustOnNext() {
        var result: Int? = null
        val subject = Just(1)
        subject.subscribe(next = { result = it })
        assertEquals(1, result)
    }

    @Test
    fun testJustEvent() {
        var done = false
        val subject = Just(1)
        subject.subscribe(done = { done = true })
        assertTrue(done)
    }
}
