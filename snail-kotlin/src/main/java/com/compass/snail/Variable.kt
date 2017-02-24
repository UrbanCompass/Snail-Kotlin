//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Variable<T>(private var _value: T) {
    private val subject: Replay<T> = Replay(1)
    private val lock = ReentrantLock()

    var value: T
        get() = lock.withLock { return _value }
        set(value) {
            lock.withLock {
                _value = value
            }
            subject.next(_value)
        }

    init {
        value = _value
    }

    fun asObservable(): Observable<T> {
        return subject
    }
}
