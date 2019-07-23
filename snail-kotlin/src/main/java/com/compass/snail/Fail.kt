//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.CoroutineDispatcher

open class Fail<T>(private val _error: Throwable) : Observable<T>() {
    override fun subscribe(dispatcher: CoroutineDispatcher?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?): Subscriber<T> {
        val subscriber = Subscriber(dispatcher, createHandler(next, error, done))
        notify(subscriber, Event(error = _error))
        return subscriber
    }
}
