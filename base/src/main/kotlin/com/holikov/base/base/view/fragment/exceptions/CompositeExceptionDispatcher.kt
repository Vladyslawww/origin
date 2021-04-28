package com.holikov.base.base.view.fragment.exceptions

class CompositeExceptionDispatcher : ExceptionDispatcher {

    private var exceptionDispatchers = mutableListOf<ExceptionDispatcher>()

    fun addDispatcher(exceptionDispatcher: ExceptionDispatcher) {
        if (exceptionDispatchers.contains(exceptionDispatcher).not()) {
            exceptionDispatchers.add(exceptionDispatcher)
        }
    }

    fun removeDispatcher(exceptionDispatcher: ExceptionDispatcher) {
        if (exceptionDispatchers.contains(exceptionDispatcher).not()) {
            exceptionDispatchers.remove(exceptionDispatcher)
        }
    }

    override fun dispatch(exception: Throwable) {
        exceptionDispatchers.forEach { it.dispatch(exception)}
    }
}