package com.example.subscriber

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver

abstract class BaseObserver<T> : DisposableSingleObserver<T>() {

    override fun onSuccess(t: T) {
        onHandleResult(t)
    }

    override fun onError(e: Throwable) {
        onHandleError(e)
    }

    abstract fun onHandleError(t: Throwable)
    abstract fun onHandleResult(t: T)

}