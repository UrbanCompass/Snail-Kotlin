//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.CoroutineDispatcher

open class Just<T>(private val value: T) : Observable<T>() {
    override fun subscribe(dispatcher: CoroutineDispatcher?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?): Subscriber<T> {
        val subscriber = Subscriber(dispatcher, createHandler(next, error, done))
        notify(subscriber, Event(next = Next(value)))
        notify(subscriber, Event(done = true))
        return subscriber
    }
}
