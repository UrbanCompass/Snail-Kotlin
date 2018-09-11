package com.compass.snail

data class BlockResult<out T>(val value: T?, val error: Throwable?)
