package com.holikov.data.network.exceptions.model

class NetworkUnprocessableException(throwable: Throwable,
                                    val apiError: NetworkUnprocessableApiError) : NetworkException(throwable)