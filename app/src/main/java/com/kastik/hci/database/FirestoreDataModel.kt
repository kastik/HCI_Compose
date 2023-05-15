package com.kastik.hci.database

import com.google.errorprone.annotations.Keep

class CustomerData(
    @Keep
    val customerId : Int = 0,
    @Keep
    val customerName: String = "",
    @Keep
    val customerLastName: String = ""
)
class Transactions(
    @Keep
    val customerId : Int = 0,
    @Keep
    val productId: Int = 0,
    @Keep
    val quantity: Int = 0
    )