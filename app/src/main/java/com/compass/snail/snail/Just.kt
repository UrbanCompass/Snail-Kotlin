//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail.snail

class Just<T>(private val value: T) : Observable<T>() {
    override fun subscribe(thread: EventThread?, handler: (Event<T>) -> Unit) {
        fire(thread, handler, Event(next = value))
        fire(thread, handler, Event(done = true))
    }

    override fun subscribeOn(thread: EventThread?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        subscribe(thread, createHandler(next, error, done))
    }
}