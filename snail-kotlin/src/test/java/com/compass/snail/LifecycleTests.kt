// Copyright © 2019 Compass. All rights reserved.

package com.compass.snail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.compass.snail.disposer.disposeBy
import com.compass.snail.disposer.onDestroy
import org.junit.Assert
import org.junit.Before
import org.junit.Test

object TestLifecycle {
    fun create(): LifecycleRegistry = TestLifecycleOwner().lifecycle
}

private class TestLifecycleOwner : LifecycleOwner {
    val lifecycle = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }
}

class LifecycleTests {
    private lateinit var lifecycle: LifecycleRegistry
    private var subject: Observable<String>? = null

    @Before
    fun before() {
        lifecycle = TestLifecycle.create()
        subject = Observable()
    }

    @Test
    fun testRemoveSubscriber() {
        lifecycle.markState(Lifecycle.State.CREATED)
        val listOfString = mutableListOf<String>()
        val listOfInt = mutableListOf<Int>()
        val listOfInt2 = mutableListOf<Int>()
        subject?.subscribe(next = { listOfString.add(it) })?.disposeBy(lifecycle.onDestroy)
        subject?.subscribe(next = { listOfInt.add(it.toInt()) })?.disposeBy(lifecycle.onDestroy)
        val intSubscriber = subject?.subscribe(next = { listOfInt2.add(it.toInt()) })
        subject?.next("1")

        Assert.assertEquals(1, listOfString.size)
        Assert.assertEquals(1, listOfInt.size)
        Assert.assertEquals(1, listOfInt2.size)
        Assert.assertEquals("1", listOfString[0])
        Assert.assertEquals(1, listOfInt[0])
        Assert.assertEquals(1, listOfInt2[0])

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

        subject?.next("2")
        Assert.assertEquals(1, listOfString.size)
        Assert.assertEquals(1, listOfInt.size)
        Assert.assertEquals(2, listOfInt2.size)
        Assert.assertEquals("1", listOfString[0])
        Assert.assertEquals(1, listOfInt[0])
        Assert.assertEquals(1, listOfInt2[0])
        Assert.assertEquals(2, listOfInt2[1])

        intSubscriber?.let { subject?.removeSubscriber(it) }
    }
}