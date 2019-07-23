// Copyright © 2019 Compass. All rights reserved.

package com.compass.snail.disposer.internal

import com.compass.snail.disposer.Disposable
import com.compass.snail.disposer.Disposer
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

internal class LockedDisposer : Disposer {
    private val disposablesLock = ReentrantLock()
    private val disposables = mutableListOf<Disposable>()
    private val disposeLock = ReentrantLock()

    override fun add(disposable: Disposable): Unit = disposablesLock.withLock {
        disposables.add(disposable)
    }

    override fun dispose(): Unit = disposeLock.withLock {
        val toDispose = disposablesLock.withLock { disposables.toList() }

        toDispose.forEach { it.dispose() }
        disposables.clear()
    }
}