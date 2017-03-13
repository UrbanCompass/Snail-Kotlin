//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import org.junit.Assert.assertEquals
import org.junit.Test

class VariableTests {
    @Test
    fun testVariableChanges() {
        val events = mutableListOf<String?>()
        val subject = Variable<String?>(null)
        subject.asObservable().subscribe(next = { events.add(it) })
        subject.value = "a"
        subject.value = "b"
        assertEquals(events[0], null)
        assertEquals(events[1], "a")
        assertEquals(events[2], "b")
        assertEquals(subject.value, "b")
    }

    @Test
    fun testVariableNotifiesOnSubscribe() {
        val subject = Variable("Initial")
        subject.value = "new"
        var result: String? = null

        subject.asObservable().subscribe(next = { result = it })
        assertEquals("new", result)
    }

    @Test
    fun testVariableNotifiesInitialOnSubscribe() {
        val subject = Variable("initial")
        var result: String? = null
        subject.asObservable().subscribe(next = { result = it })
        assertEquals("initial", result)
    }
}
