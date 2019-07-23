// Copyright © 2019 Compass. All rights reserved.

package com.compass.snail.disposer.internal

import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.compass.snail.disposer.Disposer

internal class LifecycleDisposer(
        lifecycle: Lifecycle,
        vararg events: Lifecycle.Event) : Disposer by LockedDisposer() {

    private val observer: LifecycleObserver = GenericLifecycleObserver { _, event ->
        if (events.contains(event)) {
            dispose()
        }
    }

    init {
        lifecycle.addObserver(observer)
    }
}