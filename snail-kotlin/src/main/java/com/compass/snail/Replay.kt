//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import com.compass.snail.disposer.Disposable
import kotlinx.coroutines.CoroutineDispatcher

open class Replay<T>(private val threshold: Int) : Observable<T>() {
    private var values: MutableList<T> = mutableListOf()

    override fun subscribe(dispatcher: CoroutineDispatcher?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?): Disposable {
        replay(dispatcher, createHandler(next, error, done))
        return super.subscribe(dispatcher, next, error, done)
    }

    override fun next(value: T) {
        values.add(value)
        values = values.takeLast(threshold).toMutableList()
        super.next(value)
    }

    private fun replay(dispatcher: CoroutineDispatcher?, handler: (Event<T>) -> Unit) {
        values.forEach { notify(Subscriber(dispatcher, handler, this), Event(next = Next(it))) }
    }
}
