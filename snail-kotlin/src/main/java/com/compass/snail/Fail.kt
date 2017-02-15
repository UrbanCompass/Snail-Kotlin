//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail.snail

class Fail<T>(private val error: Throwable) : Observable<T>() {
    override fun subscribe(thread: EventThread?, handler: (Event<T>) -> Unit) {
        fire(thread, handler, Event(error = error))
    }

    override fun subscribeOn(thread: EventThread?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        subscribe(thread, createHandler(next, error, done))
    }
}