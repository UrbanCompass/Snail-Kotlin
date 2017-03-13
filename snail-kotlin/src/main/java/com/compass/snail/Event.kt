//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

data class Event<out T>(val next: Next<T>? = null, val error: Throwable? = null, val done: Boolean? = null)
