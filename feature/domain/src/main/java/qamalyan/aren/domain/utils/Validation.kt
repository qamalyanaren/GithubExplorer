package qamalyan.aren.domain.utils

@JvmInline
value class Validation<T : Any>(val value: T)


fun Validation<String>.isValidNotBlank(): Boolean {
    return this.value.isNotBlank()
}