package com.sevban.common.helper

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class StandardDispatcherProvider @Inject constructor() : DispatcherProvider {
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
    override val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default
    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
    override val unconfinedDispatcher: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}