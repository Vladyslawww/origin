package com.holikov.data.network.exceptions.model

/** Server error with code [CODE] */
class BadRequestError(val message: String?): Failure.FeatureFailure(message) {
    companion object { const val CODE = 400 }
}

/** Server error with code [CODE] */
class ValidationError : Failure.FeatureFailure() {
    companion object { const val CODE = 404 }
}

/** Server error with code [CODE] */
class InternalServerError : Failure.FeatureFailure() {
    companion object { const val CODE = 500 }
}

/** Server error with codes [CODES] */
class InternalServerErrors : Failure.FeatureFailure() {
    companion object { val CODES = 500..599 }
}

/** Local error with code [CODE] to cancel request after all tokens expires */
class CancelRequestError : Failure.FeatureFailure() {
    companion object { const val CODE = 600 }
}

/** Server error with code [CODE] */
class UnauthorizedError : Failure.FeatureFailure() {
    companion object { const val CODE = 401 }
}

/** Server error with code [CODE] */
class ForbiddenError : Failure.FeatureFailure() {
    companion object { const val CODE = 403 }
}
