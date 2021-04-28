package com.holikov.base.base.viewmodel

sealed class ProgressEvent {
    object Show : ProgressEvent()
    object Hide : ProgressEvent()
}