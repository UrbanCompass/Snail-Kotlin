//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.ExecutorCoroutineDispatcher

interface IObservable<T> {
    fun subscribe(dispatcher: ExecutorCoroutineDispatcher? = null, next: ((T) -> Unit)? = null, error: ((Throwable) -> Unit)? = null, done: (() -> Unit)? = null)
    fun on(dispatcher: ExecutorCoroutineDispatcher): Observable<T>
    fun next(value: T)
    fun error(error: Throwable)
    fun done()
    fun removeSubscribers()
    fun block(): BlockResult<T>
    fun throttle(delayMs: Long): Observable<T>
}
