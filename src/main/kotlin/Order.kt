package org.example
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Item(
    val sku: Int,
    val quantity: Long,
)

@Serializable
data class Order(
    val orderId: Long,
    val discount: String? = null,
    val items: List<Item> = emptyList(),
)


