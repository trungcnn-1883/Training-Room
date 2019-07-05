package com.example.subscriber

import com.example.entity.Note
import io.reactivex.subscribers.DisposableSubscriber

abstract class BaseSubscriber : DisposableSubscriber<List<Note>>() {

     override fun onComplete() {
     }

     override fun onNext(t: List<Note>) {
         onHandleResult(t)
     }

     override fun onError(t: Throwable) {
         onHandleError(t)
     }

     abstract fun onHandleError(t: Throwable)
     abstract fun onHandleResult(t: List<Note>)

 }