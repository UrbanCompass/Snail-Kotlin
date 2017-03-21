//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import android.os.Handler
import android.os.Looper
import android.util.Log

open class Observable<T> : IObservable<T> {
    var stoppedEvent: Event<T>? = null
    var subscribers: MutableList<Subscriber<T>> = mutableListOf()

    override fun subscribe(thread: EventThread?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        stoppedEvent?.let {
            notify(Subscriber(thread, createHandler(next, error, done)), it)
            return
        }
        subscribers.add(Subscriber(thread, createHandler(next, error, done)))
    }

    override fun next(value: T) {
        on(Event(next = Next(value)))
    }

    override fun error(error: Throwable) {
        on(Event(error = error))
    }

    override fun done() {
        on(Event(done = true))
    }

    override fun removeSubscribers() {
        subscribers.clear()
    }

    private fun on(event: Event<T>) {
        if (stoppedEvent != null) return

        event.next?.let {
            subscribers.forEach { notify(it, event) }
        }
        event.error?.let {
            subscribers.forEach { notify(it, event) }
            stoppedEvent = event
        }
        event.done?.let {
            subscribers.forEach { notify(it, event) }
            stoppedEvent = event
        }
    }

    protected fun createHandler(next: ((T) -> Unit)? = null, error: ((Throwable) -> Unit)? = null, done: (() -> Unit)? = null): (Event<T>) -> Unit {
        return { event ->
            event.next?.let { next?.invoke(it.value) }
            event.error?.let { error?.invoke(it) }
            event.done?.let { done?.invoke() }
        }
    }

    fun notify(subscriber: Subscriber<T>, event: Event<T>) {
        if (subscriber.thread == EventThread.MAIN) {
            Handler(Looper.getMainLooper()).post { safeNotify(subscriber, event) }
        } else {
            safeNotify(subscriber, event)
        }
    }

    private fun safeNotify(subscriber: Subscriber<T>, event: Event<T>) {
        try {
            subscriber.eventHandler(event)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Snail Observable", "Removing subscriber $subscriber")
            subscribers.remove(subscriber)
        }
    }
}
