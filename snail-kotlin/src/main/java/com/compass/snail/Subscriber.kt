//  Copyright © 2016 Compass. All rights reserved.

package com.compass.snail

data class Subscriber<T>(val thread: EventThread?, val eventHandler: (Event<T>) -> Unit)
