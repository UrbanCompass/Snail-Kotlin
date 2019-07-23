//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import com.compass.snail.disposer.Disposable
import kotlinx.coroutines.CoroutineDispatcher

class Subscriber<T>(val dispatcher: CoroutineDispatcher?, val eventHandler: (Event<T>) -> Unit, val owner: Observable<T>) : Disposable {
    override fun dispose() {
        owner.removeSubscriber(this)
    }
}

