package ru.yandex.practicum.moviessearch.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,
    coroutinesScope: CoroutineScope,
    useLastParams: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParams) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParams) {
            debounceJob = coroutinesScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}