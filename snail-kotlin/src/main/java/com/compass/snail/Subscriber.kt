//  Copyright © 2016 Compass. All rights reserved.

package com.compass.compasslibrary.snail

data class Subscriber<T>(val thread: EventThread?, val eventHandler: (Event<T>) -> Unit)
