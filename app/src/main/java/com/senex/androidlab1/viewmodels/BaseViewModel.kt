package com.senex.androidlab1.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel : ViewModel() {
    protected val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun onDestroy() {
        coroutineScope.coroutineContext.cancelChildren()
    }
}