//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import com.compass.snail.disposer.Disposable
import kotlinx.coroutines.CoroutineDispatcher
import java.util.concurrent.LinkedBlockingQueue

open class Replay<T>(private val threshold: Int) : Observable<T>() {
    private val queue = LinkedBlockingQueue<T>(threshold)

    override fun subscribe(dispatcher: CoroutineDispatcher?, next: ((T) -> Unit)?, error: ((Throwable) -> Unit)?, done: (() -> Unit)?): Disposable {
        replay(dispatcher, createHandler(next, error, done))
        return super.subscribe(dispatcher, next, error, done)
    }

    override fun next(value: T) {
        if (queue.size == threshold) {
            queue.poll()
        }
        queue.offer(value)
        super.next(value)
    }

    private fun replay(dispatcher: CoroutineDispatcher?, handler: (Event<T>) -> Unit) {
        queue.forEach { notify(Subscriber(dispatcher, handler, this), Event(next = Next(it))) }
    }
}
