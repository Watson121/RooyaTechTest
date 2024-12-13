package org.example
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString


public lateinit var products : List<Product>
public lateinit var discounts : List<Discount>
public lateinit var orders : List<Order>

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    ImportJsonData()

}

// Function responsible for Importing Json Data
fun ImportJsonData(){

    val json = Json{ignoreUnknownKeys = true}

    // Importing in Product Data
    products = json.decodeFromString<List<Product>>(GetJsonStringData("/products.json"));

    // Importing in Discount Data
    discounts = json.decodeFromString<List<Discount>>(GetJsonStringData("/discounts.json"));

    // Importing in Order Data
    orders = json.decodeFromString<List<Order>>(GetJsonStringData("/orders.json"));

}

fun GetJsonStringData(inputString : String) : String {
    return object {}.javaClass.getResource(inputString)?.readText()
        ?: throw IllegalArgumentException("Json File has not been found or not valid")
}