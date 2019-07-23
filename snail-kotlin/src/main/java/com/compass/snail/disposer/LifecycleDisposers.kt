// Copyright © 2019 Compass. All rights reserved.

package com.compass.snail.disposer

class LifecycleDisposers(
        val onCreate: Disposer,
        val onStart: Disposer,
        val onResume: Disposer,
        val onPause: Disposer,
        val onStop: Disposer,
        val onDestroy: Disposer) {
    internal object Factory
    internal object Store
}