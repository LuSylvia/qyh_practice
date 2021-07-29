package com.example.module_common.retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//ViewModel扩展函数
fun ViewModel.launch(
    block: suspend CoroutineScope.() -> Unit,
    onError: (e: Throwable) -> Unit = {},
    onComplete: () -> Unit = {}
) {
    viewModelScope.launch(CoroutineExceptionHandler { _, e -> onError(e) }) {
        try {
            block.invoke(this)
        } finally {
            onComplete()
        }
    }
}