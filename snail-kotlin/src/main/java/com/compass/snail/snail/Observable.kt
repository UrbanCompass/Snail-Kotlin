//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail.snail

import android.os.Handler
import android.os.Looper

open class Observable<T> : IObservable<T> {
    private var isStopped = false
    var eventHandlers: MutableList<Pair<EventThread?, (Event<T>) -> Unit>> = mutableListOf()

    override fun subscribe(thread: EventThread?, handler: (Event<T>) -> Unit) {
        eventHandlers.add(Pair(thread, handler))
    }

    override fun subscribeOn(thread: EventThread?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        eventHandlers.add(Pair(thread, createHandler(next, error, done)))
    }

    override fun next(value: T) {
        on(Event(next = value))
    }

    override fun error(error: Throwable) {
        on(Event(error = error))
    }

    override fun done() {
        on(Event(done = true))
    }

    private fun on(event: Event<T>) {
        if (isStopped) return

        event.next?.let {
            eventHandlers.forEach { handler ->
                fire(handler.first, handler.second, event)
            }
        }
        event.error?.let {
            eventHandlers.forEach { handler -> fire(handler.first, handler.second, event) }
            isStopped = true
        }
        event.done?.let {
            eventHandlers.forEach { handler -> fire(handler.first, handler.second, event) }
            isStopped = true
        }
    }

    fun createHandler(next: ((T) -> Unit)? = null, error: ((Throwable) -> Unit)? = null, done: (() -> Unit)? = null): (Event<T>) -> Unit {
        return { event ->
            event.next?.let { next?.invoke(it) }
            event.error?.let { error?.invoke(it) }
            event.done?.let { done?.invoke() }
        }
    }

    fun fire(thread: EventThread?, handler: (Event<T>) -> Unit, event: Event<T>) {
        thread?.let {
            if (it == EventThread.MAIN) {
                Handler(Looper.getMainLooper()).post({
                    handler(event)
                })
                return
            }
        }
        handler(event)
    }
}