package qamalyan.aren.domain.utils

interface Mapper<IN, OUT> {
    fun map(from: IN): OUT
    fun map(from: List<IN>): List<OUT> = from.map { map(it) }

}