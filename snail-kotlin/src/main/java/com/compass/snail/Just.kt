//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

class Just<T>(private val value: T) : Observable<T>() {
    override fun subscribe(thread: EventThread?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        notify(Subscriber(thread, createHandler(next, error, done)), Event(next = value))
        notify(Subscriber(thread, createHandler(next, error, done)), Event(done = true))
    }
}
