package org.example

data class Order(
    val orderId: Long,
    val discount: String?,
    val items: List<Item>,
)

data class Item(
    val sku: Long,
    val quantity: Long,
)
