package org.example
import kotlinx.serialization.json.Json


public lateinit var products : List<Product>
public lateinit var discounts : List<Discount>
public lateinit var orders : List<Order>

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    importJsonData()

}

// Function responsible for Importing Json Data
fun importJsonData(){

    val json = Json{ignoreUnknownKeys = true}

    // Importing in Product Data
    products = json.decodeFromString<List<Product>>(getJsonStringData("/products.json"));

    // Importing in Discount Data
    discounts = json.decodeFromString<List<Discount>>(getJsonStringData("/discounts.json"));

    // Importing in Order Data
    orders = json.decodeFromString<List<Order>>(getJsonStringData("/orders.json"));

}

fun getJsonStringData(inputString : String) : String {
    return object {}.javaClass.getResource(inputString)?.readText()
        ?: throw IllegalArgumentException("Json File has not been found or not valid")
}