package com.example.baselibrary.http

import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Created by dumingwei on 2020/5/16.
 *
 * Desc:
 */

interface CoroutineErrorListener {

    fun onError(throwable: Throwable)
}

class UncaughtCoroutineExceptionHandler(val errorListener: CoroutineErrorListener? = null) :
    CoroutineExceptionHandler, AbstractCoroutineContextElement(CoroutineExceptionHandler.Key) {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        exception.printStackTrace()
        errorListener?.let {
            it.onError(exception)
        }
    }
}

class SafeCoroutineScope(context: CoroutineContext, errorListener: CoroutineErrorListener? = null) :
    CoroutineScope, Closeable {

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + context + UncaughtCoroutineExceptionHandler(
            errorListener
        )

    override fun close() {
        coroutineContext.cancelChildren()
    }

}

fun uiScope(errorListener: CoroutineErrorListener? = null) =
    SafeCoroutineScope(
        Dispatchers.Main,
        errorListener
    )

