package org.example
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Discount(
    val key: String,
    val value: Double,
    val stacks: String?
)

