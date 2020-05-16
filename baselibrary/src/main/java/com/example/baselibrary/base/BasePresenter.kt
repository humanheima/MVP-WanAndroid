package com.example.baselibrary.base


import android.util.Log
import com.example.baselibrary.http.CoroutineErrorListener
import com.example.baselibrary.http.uiScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

open class BasePresenter<V : IBaseView>(view: V) :
    IBasePresenter<V> {

    protected val TAG = javaClass.name

    protected var view: V? = view
    private var compositeDisposable: CompositeDisposable? = null

    protected val scope: CoroutineScope = uiScope(object : CoroutineErrorListener {
        override fun onError(throwable: Throwable) {
            Log.d(TAG, "coroutine onError: ${throwable.message}")
        }
    })

    init {
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreate() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onDestroy() {
        compositeDisposable?.clear()
        scope.cancel()
    }

    protected fun addSubscribe(disposable: Disposable) {
        compositeDisposable?.add(disposable)
    }

}