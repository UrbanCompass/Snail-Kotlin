//  Copyright © 2016 Compass. All rights reserved.

package com.compass.compasslibrary.snail

interface IObservable<T> {
    fun subscribe(thread: EventThread? = EventThread.OBSERVABLE, next: ((T) -> Unit)? = null, error: ((Throwable) -> Unit)? = null, done: (() -> Unit)? = null)
    fun next(value: T)
    fun error(error: Throwable)
    fun done()
}