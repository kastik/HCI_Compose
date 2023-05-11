package com.kastik.hci.ui.mainComponents

data class CustomerData(
    val customerId : Int
)

data class Transactions(
    val customerId : Int,
    val productId: Int,
    val quantity: Int
)