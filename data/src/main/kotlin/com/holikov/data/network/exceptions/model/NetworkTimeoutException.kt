package com.holikov.data.network.exceptions.model

import okhttp3.Request
import java.io.IOException

class NetworkTimeoutException(cause: Throwable, val request: Request) : IOException(cause)
