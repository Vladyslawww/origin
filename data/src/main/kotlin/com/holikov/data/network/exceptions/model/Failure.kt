package com.holikov.data.network.exceptions.model

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    object NetworkConnection : Failure()

    object Unknown: Failure()

    /** Server error with code [CODE] */
    class NotModifiedError: Failure() {
        companion object { const val CODE = 304 }
    }

    /** Server error fired when [java.net.SocketTimeoutException] received */
    object SocketTimeoutError: Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure(message: String? = null): Failure()
}
