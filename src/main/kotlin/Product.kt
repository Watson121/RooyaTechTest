package org.example
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Product(
    val sku: Int,
    val price: Double
)
