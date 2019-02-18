//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.CoroutineDispatcher

open class Just<T>(private val value: T) : Observable<T>() {
    override fun subscribe(dispatcher: CoroutineDispatcher?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        notify(Subscriber(dispatcher, createHandler(next, error, done)), Event(next = Next(value)))
        notify(Subscriber(dispatcher, createHandler(next, error, done)), Event(done = true))
    }
}
