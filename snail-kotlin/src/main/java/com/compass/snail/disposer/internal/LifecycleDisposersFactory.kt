// Copyright © 2019 Compass. All rights reserved.

package com.compass.snail.disposer.internal

import androidx.lifecycle.Lifecycle
import com.compass.snail.disposer.LifecycleDisposers

internal fun LifecycleDisposers.Factory.create(lifecycle: Lifecycle): LifecycleDisposers {
    return LifecycleDisposers(
            onCreate = LifecycleDisposer(lifecycle, Lifecycle.Event.ON_CREATE),
            onStart = LifecycleDisposer(lifecycle, Lifecycle.Event.ON_START),
            onResume = LifecycleDisposer(lifecycle, Lifecycle.Event.ON_RESUME),
            onPause = LifecycleDisposer(lifecycle, Lifecycle.Event.ON_PAUSE),
            onStop = LifecycleDisposer(lifecycle, Lifecycle.Event.ON_STOP),
            onDestroy = LifecycleDisposer(lifecycle, Lifecycle.Event.ON_DESTROY))
}