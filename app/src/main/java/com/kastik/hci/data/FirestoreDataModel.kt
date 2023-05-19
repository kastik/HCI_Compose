package com.kastik.hci.data

import com.google.firebase.firestore.DocumentId

data class CustomerData(
    @DocumentId val customerId :String = "",
    val customerName: String = "",
    val customerLastName: String = ""
)
data class Transaction(
    @DocumentId val transactionId :String ="",
    val customerId : String = "",
    val productName: String = "",
    val quantitySold: Int = 0,
    val productId: Int = 0
)