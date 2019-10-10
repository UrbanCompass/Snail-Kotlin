//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import com.compass.snail.disposer.Disposable
import kotlinx.coroutines.CoroutineDispatcher
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

open class Replay<T>(private val threshold: Int) : Observable<T>() {
    private var values: MutableList<T> = mutableListOf()
    private val lock = ReentrantLock()

    override fun subscribe(dispatcher: CoroutineDispatcher?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?): Disposable {
        replay(dispatcher, createHandler(next, error, done))
        return super.subscribe(dispatcher, next, error, done)
    }

    override fun next(value: T) {
        lock.withLock {
            values.add(value)
            values = values.takeLast(threshold).toMutableList()
        }
        super.next(value)
    }

    private fun replay(dispatcher: CoroutineDispatcher?, handler: (Event<T>) -> Unit) {
        lock.withLock {
            values.forEach { notify(Subscriber(dispatcher, handler, this), Event(next = Next(it))) }
        }
    }
}
