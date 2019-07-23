// Copyright © 2019 Compass. All rights reserved.

package com.compass.snail.disposer.internal

import androidx.lifecycle.Lifecycle
import com.compass.snail.disposer.LifecycleDisposers
import java.util.WeakHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val disposers = WeakHashMap<Lifecycle, LifecycleDisposers>()
private val disposersLock = ReentrantLock()
internal operator fun LifecycleDisposers.Store.get(lifecycle: Lifecycle):
        LifecycleDisposers = disposersLock.withLock {
    disposers.getOrPut(lifecycle) {
        LifecycleDisposers.Factory.create(lifecycle)
    }
}