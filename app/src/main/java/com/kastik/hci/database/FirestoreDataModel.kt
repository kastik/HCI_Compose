package com.kastik.hci.database

class CustomerData(
    val customerId : Int = 0,
    val customerName: String,
    val customerLastName: String
)

class Transactions(
    val customerId : Int = 0,
    val productId: Int = 0,
    val quantity: Int = 0
    )