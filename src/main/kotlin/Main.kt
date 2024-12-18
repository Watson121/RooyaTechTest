package org.example
import kotlinx.serialization.json.Json


public lateinit var products : List<Product>
public lateinit var discounts : List<Discount>
public lateinit var orders : List<Order>

fun main() {

    importJsonData()
    val totalSalesBeforeDiscount : Double = totalSalesBeforeDiscount()
    val totalSalesAfterDiscount : Double = totalSalesAfterDiscount()
    val totalAmountOfMoneyLost = totalSalesBeforeDiscount - totalSalesAfterDiscount
    val averageDiscount : Double = averageDiscount()

    println("Total Sales Before Discount: £${"%.2f".format(totalSalesBeforeDiscount)}")
    println("Total Sales After Discount: £${"%.2f".format(totalSalesAfterDiscount)}")
    println("Total Amount of Money Lost Via Discount Codes: £${"%.2f".format(totalAmountOfMoneyLost)}")
    println("Average Discount Per Customer: ${"%.2f".format(averageDiscount)}%")
}

//region Importing Json

// Function responsible for Importing Json Data
fun importJsonData(){

    val json = Json{ignoreUnknownKeys = true}

    // Importing in Product Data
    products = json.decodeFromString<List<Product>>(getJsonStringData("/products.json"))

    // Importing in Discount Data
    discounts = json.decodeFromString<List<Discount>>(getJsonStringData("/discounts.json"))

    // Importing in Order Data
    orders = json.decodeFromString<List<Order>>(getJsonStringData("/orders.json"))

}

// Getter function for Getting Json Data
fun getJsonStringData(inputString : String) : String {
    return object {}.javaClass.getResource(inputString)?.readText()
        ?: throw IllegalArgumentException("Json File has not been found or not valid")
}

//endregion

//region Calculations

// Calculating the total amount of sales, before the discount is applied
fun totalSalesBeforeDiscount() : Double{

    var totalSales : Double = 0.0

    for(order in orders){
        for(item in order.items){
            val product = products.find { it.sku == item.sku }

            if(product != null){
                totalSales += (product.price * item.quantity)
            }else{
                throw IllegalStateException("Product is missing")
            }
        }
    }

    return totalSales
}

// Calculating the total sales amount, after the discount is applied
fun totalSalesAfterDiscount() : Double {

    var totalSales : Double = 0.0

    for(order in orders){

        val discount = discounts.find{it.key == order.discount}

        if(discount != null){

            for(item in order.items){
                val product = products.find { it.sku == item.sku }

                if(product != null){
                    val productPrice = product.price * (1 - discount.value)
                    totalSales += (productPrice * item.quantity)
                }
            }
        }else{

            for(item in order.items) {
                val product = products.find { it.sku == item.sku }

                if (product != null) {
                    totalSales += (product.price * item.quantity)
                }
            }
        }
    }

    return totalSales
}

// Calculating the average discount per a customer
fun averageDiscount() : Double{

    var averageDiscount : Double = 0.0;
    var customersWithDiscount : Int = 0;

    for(order in orders){
        val discount = discounts.find{it.key == order.discount}

        if(discount != null){
            customersWithDiscount++
            averageDiscount += discount.value
        }
    }

    return (averageDiscount / customersWithDiscount) * 100.0;
}

//endregion


