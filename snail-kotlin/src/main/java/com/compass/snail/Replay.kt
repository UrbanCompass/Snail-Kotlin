//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.experimental.CoroutineDispatcher

open class Replay<T>(private val threshold: Int) : Observable<T>() {
    private var values: MutableList<T> = mutableListOf()

    override fun subscribe(dispatcher: CoroutineDispatcher?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?) {
        super.subscribe(dispatcher, next, error, done)
        replay(dispatcher, createHandler(next, error, done))
    }

    override fun next(value: T) {
        values.add(value)
        values = values.takeLast(threshold).toMutableList()
        super.next(value)
    }

    private fun replay(dispatcher: CoroutineDispatcher?, handler: (Event<T>) -> Unit) {
        values.forEach { notify(Subscriber(dispatcher, handler), Event(next = Next(it))) }
    }
}
