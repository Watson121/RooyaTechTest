package org.example
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Discount(
    var key: String,
    var value: Double,
    val stacks: String? = null
)

