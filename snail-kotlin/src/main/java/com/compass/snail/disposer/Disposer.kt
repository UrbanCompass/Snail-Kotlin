// Copyright © 2019 Compass. All rights reserved.

package com.compass.snail.disposer

interface Disposer : Disposable {
    fun add(disposable: Disposable)
}

fun Disposable.disposeBy(disposer: Disposer) = this.apply {
    disposer.add(this)
}
