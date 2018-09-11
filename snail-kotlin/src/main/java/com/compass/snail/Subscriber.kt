//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

import kotlinx.coroutines.experimental.ExecutorCoroutineDispatcher

data class Subscriber<in T>(val dispatcher: ExecutorCoroutineDispatcher?, val eventHandler: (Event<T>) -> Unit)
