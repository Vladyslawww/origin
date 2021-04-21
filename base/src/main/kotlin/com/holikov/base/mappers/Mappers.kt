package com.holikov.base.mappers

inline fun <F, T> List<F>?.mapList(objectMapper: (F) -> T) = this?.map(objectMapper) ?: emptyList()

inline fun <F, T> F.mapItem(objectMapper: (F) -> T) = run(objectMapper)
