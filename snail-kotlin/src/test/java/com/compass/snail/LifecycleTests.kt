// Copyright © 2019 Compass. All rights reserved.

package com.compass.snail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.compass.snail.disposer.disposeBy
import com.compass.snail.disposer.onDestroy
import com.compass.snail.disposer.onPause
import com.compass.snail.disposer.onStart
import com.compass.snail.disposer.onStop
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
    fun testOnDestroy() {
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

    @Test
    fun testOnPause() {
        lifecycle.markState(Lifecycle.State.RESUMED)
        val listOfString = mutableListOf<String>()
        subject?.subscribe(next = { listOfString.add(it) })?.disposeBy(lifecycle.onPause)

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        subject?.next("1")
        Assert.assertEquals(0, listOfString.size)
    }

    @Test
    fun testOnStop() {
        lifecycle.markState(Lifecycle.State.STARTED)
        val listOfString = mutableListOf<String>()
        subject?.subscribe(next = { listOfString.add(it) })?.disposeBy(lifecycle.onStop)

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        subject?.next("1")
        Assert.assertEquals(0, listOfString.size)
    }
}