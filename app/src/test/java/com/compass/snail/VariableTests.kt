//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import com.compass.snail.snail.Variable
import org.junit.Assert.assertEquals
import org.junit.Test

class VariableTests {
    @Test
    fun testVariableChanges() {
        var events: MutableList<String?> = mutableListOf()
        val subject = Variable<String?>(null)
        subject.asObservable().subscribeOn(next = {
            value -> events.add(value)
        })
        subject.value = "a"
        subject.value = "b"
        assertEquals(events[0], "a")
        assertEquals(events[1], "b")
        assertEquals(subject.value, "b")
    }

    @Test
    fun testVariableNotifiesOnSubscribe() {
        val subject = Variable("Initial")
        subject.value = "new"
        var result: String? = null

        subject.asObservable().subscribeOn(next = { value -> result = value })

        assertEquals("new", result)
    }

    @Test
    fun testVariableNotifiesInitialOnSubscribe() {
        val subject = Variable("initial")
        var result: String? = null
        subject.asObservable().subscribeOn(next = { value -> result = value })
        assertEquals("initial", result)
    }
}