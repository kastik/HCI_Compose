package com.kastik.hci.data

import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.DocumentId

@Keep
data class CustomerData(
    @DocumentId var customerId :String = "",
    var customerName: String = "",
    var customerLastName: String = ""
)
@Keep
data class Transaction(
    @DocumentId var transactionId :String ="",
    var customerId : String = "",
    var productName: String = "",
    var quantitySold: Int = 0,
    var productId: Int = 0
)