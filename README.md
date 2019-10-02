# Snail-Kotlin 🐌 ![Bitrise](https://app.bitrise.io/app/9064b20f3be78d27.svg?token=9q6cr3-Ugc1e56o1sjuYHA) [![codecov](https://codecov.io/gh/UrbanCompass/Snail-Kotlin/branch/master/graph/badge.svg)](https://codecov.io/gh/UrbanCompass/Snail-Kotlin) [![](https://jitpack.io/v/urbancompass/snail-kotlin.svg)](https://jitpack.io/#urbancompass/snail-kotlin)

A lightweight observables framework, also available in [Swift](https://github.com/UrbanCompass/Snail)

## Download

You can download a jar from GitHub's [releases page](https://github.com/UrbanCompass/Snail-Kotlin/releases).

Jitpack

```gradle
allprojects {
 repositories {
    ...
    maven { url "https://jitpack.io" }
 }
}

dependencies {
  compile 'com.github.urbancompass:snail-kotlin:x.x.x'
}
```

## Creating Observables

```kotlin
val observable = Observable<thing>()
```

## Subscribing to Observables

```kotlin
observable.subscribe(
    next = { thing in ... }, // do something with thing
    error = { error in ... }, // do something with error
    done = { ... } // do something when it's done
)
```

Closures are optional too...

```kotlin
observable.subscribe(
    next = { thing in ... } // do something with thing
)
```

```kotlin
observable.subscribe(
    error = { error in ... } // do something with error
)
```

## Creating Observables Variables

```kotlin
val variable = Variable<whatever>(some initial value)
```

```kotlin
val optionalString = Variable<String?>(null)
optionalString.asObservable().subscribe(
    next = { string in ... } // do something with value changes
)

optionalString.value = "something"
```

```kotlin
val int = Variable<Int>(12)
int.asObservable().subscribe(
    next = { int in ... } // do something with value changes
)

int.value = 42
```

## Miscellaneous Observables

```kotlin
val just = Just(1) // always returns the initial value (1 in this case)

val failure = Fail(RunTimeException()) // always returns error
failure.subscribe(
	error = { it }  //it is RuntimeException
)

val n = 5
let replay = Replay(n) // replays the last N events when a new observer subscribes
```

## Dispatchers

You can specify which dispatcher an observables will be notified on by using `.subscribe(dispatcher: <desired dispatcher>)`. If you don't specify, then the observable will be notified on the same dispatcher that the observable published on.

There are 3 scenarios:

1.  You don't specify the dispatcher. Your observer will be notified on the same dispatcher as the observable published on.

2.  You specified `Main` dispatcher AND the observable published on the `Main` dispatcher. Your observer will be notified synchronously on the `Main` dispatcher.

3.  You specified a dispatcher. Your observer will be notified async on the specified dispatcher.

### Examples

Subscribing on `Main`

```kotlin
observable.subscribe(Dispatchers.Main, next = {
    // do stuff with it...
})
```
