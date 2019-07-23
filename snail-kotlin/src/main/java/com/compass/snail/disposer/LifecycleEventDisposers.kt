// Copyright © 2019 Compass. All rights reserved.
@file:Suppress("unused")

package com.compass.snail.disposer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.compass.snail.disposer.internal.get

val Lifecycle.onCreate get() = disposers.onCreate
val Lifecycle.onStart get() = disposers.onStart
val Lifecycle.onResume get() = disposers.onResume
val Lifecycle.onPause get() = disposers.onPause
val Lifecycle.onStop get() = disposers.onStop
val Lifecycle.onDestroy get() = disposers.onDestroy

val LifecycleOwner.onCreate get() = lifecycle.onCreate
val LifecycleOwner.onStart get() = lifecycle.onStart
val LifecycleOwner.onResume get() = lifecycle.onResume
val LifecycleOwner.onPause get() = lifecycle.onPause
val LifecycleOwner.onStop get() = lifecycle.onStop
val LifecycleOwner.onDestroy get() = lifecycle.onDestroy

val Lifecycle.disposers: LifecycleDisposers get() = LifecycleDisposers.Store[this]
val LifecycleOwner.disposers: LifecycleDisposers get() = lifecycle.disposers