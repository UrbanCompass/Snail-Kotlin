//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

interface IObservable<T> {
    fun subscribe(thread: EventThread? = EventThread.OBSERVABLE, next: ((T) -> Unit)? = null, error: ((Throwable) -> Unit)? = null, done: (() -> Unit)? = null)
    fun on(thread: EventThread): Observable<T>
    fun next(value: T)
    fun error(error: Throwable)
    fun done()
    fun removeSubscribers()
    fun block(): Pair<T?, Throwable?>
    fun throttle(delay: Double): Observable<T>
}
