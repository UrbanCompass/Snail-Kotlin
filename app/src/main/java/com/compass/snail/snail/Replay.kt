//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail.snail

class Replay<T>(val threshold: Int) : Observable<T>() {
    private var values: MutableList<T> = mutableListOf()

    override fun subscribe(thread: EventThread?, handler: (Event<T>) -> Unit) {
        super.subscribe(thread, handler)
        replay(thread, handler)
    }

    override fun subscribeOn(thread: EventThread?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        super.subscribeOn(thread, next, error, done)
        replay(thread, createHandler(next, error, done))
    }

    override fun next(value: T) {
        values.add(value)
        values = values.takeLast(threshold).toMutableList()
        super.next(value)
    }

    private fun replay(thread: EventThread?, handler: (Event<T>) -> Unit) {
        values.forEach { value -> fire(thread, handler, Event(next = value)) }
    }
}