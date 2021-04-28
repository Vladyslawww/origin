package com.holikov.base.extensions

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

fun <T> asyncWith(
    context: CoroutineContext = Dispatchers.Default,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return GlobalScope.async(context = context, start = start, block = block)
}

fun launchWith(
    context: CoroutineContext = Dispatchers.Default,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return GlobalScope.launch(context = context, start = start, block = block)
}
suspend fun Job?.invokeOnSuccess( job : (Job) -> Unit) {
    this?.invokeOnCompletion { if (it == null) job(this) }
}

fun Job.error(onError: (Throwable) -> Unit) {
    invokeOnCompletion { it?.let { if (it !is CancellationException) onError(it) } }
}

val WORKER_POOL = Executors.newFixedThreadPool(64.coerceAtLeast(Runtime.getRuntime().availableProcessors())).asCoroutineDispatcher()
val UI_POOL = Dispatchers.Main

