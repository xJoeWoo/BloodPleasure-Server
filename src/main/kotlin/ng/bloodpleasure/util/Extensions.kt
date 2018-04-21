package ng.bloodpleasure.util

import java.util.*

inline fun <T, R> Optional<T>.takeOrDo(fail: () -> R, success: (T) -> R): R = if (isPresent) success(get()) else fail()

inline fun <T, R> T?.takeOrDo(fail: () -> R, success: (T) -> R): R = if (this != null) success(this) else fail()

inline fun <T> T?.takeOrDefault(fail: () -> T): T = takeOrDo(fail, { it })