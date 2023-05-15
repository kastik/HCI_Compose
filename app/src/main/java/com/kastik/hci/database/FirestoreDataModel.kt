package com.kastik.hci.database

import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.DocumentId

data class CustomerData(
    @DocumentId val customerId : String = "",
    val customerName: String = "",
    val customerLastName: String = ""
)
data class Transactions(
    @Keep
    val customerId : String = "" ,
    @Keep
    val product: String = "",
    @Keep
    val quantity: Int = 0
    )