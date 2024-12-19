package org.example
import kotlinx.serialization.json.Json
import java.text.DecimalFormat


lateinit var products : List<Product>
lateinit var discounts : MutableList<Discount>
lateinit var orders : List<Order>

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
    discounts = json.decodeFromString<MutableList<Discount>>(getJsonStringData("/discounts.json"))

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

        var discount : Discount? = null

        // Checking if the discount code contains a comma or not
        // If it does, it needs to be brokendown
        if(order.discount?.contains(",") == true){
            breakDownDiscountCode(order.discount)
        }

        discount = discounts.find{it.key == order.discount}

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

// Breaking down the discount code, and adding this new discount to the discount list
fun breakDownDiscountCode(key : String){

    var newDiscountKey : String = ""
    var newDiscountValue : Double = 0.0;
    val discountCodes = key.split(",")
    var canStack : Boolean = false;

    // Iterating through the discount codes to see if they can be stacked or not
    for(discountCode in discountCodes){
        val discount = discounts.find{it.key == discountCode}

        if(discount != null){

            // Making sure the can be stacked cannot be reset once activated
            if(!canStack) {
                canStack = canStack(discount)
            }

            // If discount codes can be stacked, then add on discount value
            if(canStack){
                newDiscountKey = key
                newDiscountValue += discount.value
            }else{
                newDiscountValue = discount.value
            }
        }
    }

    // Formating the Double
    val df = DecimalFormat("#.0")

    // Adding a new discount to the discount list
    discounts.add(Discount(newDiscountKey, df.format(newDiscountValue).toDouble()))
}

// Checking if discounts can be stacked or not
fun canStack(discount: Discount?) : Boolean{

    if(discount?.stacks != null){

        if(discount.stacks == "TRUE"){
            return true
        }else if(discount.stacks == "FALSE"){
            return false
        }

    }

    return false
}

// Calculating the average discount per a customer
fun averageDiscount() : Double{

    var averageDiscount : Double = 0.0
    var customersWithDiscount : Int = 0

    for(order in orders){
        val discount = discounts.find{it.key == order.discount}

        if(discount != null){
            customersWithDiscount++
            averageDiscount += discount.value
        }
    }

    return (averageDiscount / customersWithDiscount) * 100.0
}

//endregion


