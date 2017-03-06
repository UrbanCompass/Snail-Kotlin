//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

class Fail<T>(private val _error: Throwable) : Observable<T>() {
    override fun subscribe(thread: EventThread?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        notify(Subscriber(thread, createHandler(next, error, done)), Event(error = _error))
    }
}
