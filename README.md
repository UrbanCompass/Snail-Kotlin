# Snail-Kotlin 🐌 ![CircleCI](https://circleci.com/gh/UrbanCompass/Snail-Kotlin/tree/master.svg?style=shield&circle-token=02af7805c3430ec7945e0895b2108b4d9b348e85) [![codecov](https://codecov.io/gh/UrbanCompass/Snail-Kotlin/branch/master/graph/badge.svg)](https://codecov.io/gh/UrbanCompass/Snail-Kotlin) [![](https://jitpack.io/v/urbancompass/snail-kotlin.svg)](https://jitpack.io/#urbancompass/snail-kotlin)

A lightweight observables framework, also available in [Swift](https://github.com/UrbanCompass/Snail)

Download
--------
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
  compile 'com.github.urbancompass:snail-kotlin:0.0.2'
}
```

Or Maven:
```xml
<dependency>
  <groupId>com.compass.snail</groupId>
  <artifactId>snail</artifactId>
  <version>0.0.1</version>
  <type>pom</type>
</dependency>
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

## Threads

You can specify which thread an observables will be notified on by using `.subscribe(thread: <desired thread>)`. If you don't specify, then the observable will be notified on the same thread that the observable published on.

There are 3 scenarios:

1. You don't specify the thread. Your observer will be notified on the same thread as the observable published on.

2. You specified `MAIN` thread AND the observable published on the `MAIN` thread. Your observer will be notified synchronously on the `MAIN` thread.

3. You specified a thread. Your observer will be notified async on the specified thread.

### Examples

Subscribing on `EventThread.MAIN`

```kotlin
observable.subscribe(EventThread.MAIN,
    next = { thing in ... }
)
```
