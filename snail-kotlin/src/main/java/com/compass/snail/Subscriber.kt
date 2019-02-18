//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.CoroutineDispatcher

data class Subscriber<in T>(val dispatcher: CoroutineDispatcher?, val eventHandler: (Event<T>) -> Unit)
