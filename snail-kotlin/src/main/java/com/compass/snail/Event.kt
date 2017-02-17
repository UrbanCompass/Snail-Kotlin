//  Copyright © 2016 Compass. All rights reserved.

package com.compass.compasslibrary.snail

data class Event<out T>(val next: T? = null, val error: Throwable? = null, val done: Boolean? = null)
