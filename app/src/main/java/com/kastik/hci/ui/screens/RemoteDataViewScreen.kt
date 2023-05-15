package com.kastik.hci.ui.screens

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.database.Transactions
import com.kastik.hci.ui.components.RemoteDatabaseCard

@Composable
fun RemoteDataViewScreen() {
    val transactions = Firebase.firestore.collection("Transactions")

    val myData = remember { mutableStateListOf<Transactions>() }

    transactions.get().addOnSuccessListener { documents ->
        for (document in documents) {
            myData.add(document.toObject(Transactions::class.java))
            Log.d("MyLog", "Got Transactions ${document.data}")
        }
    }

    LazyColumn {
        items(myData) { transactions ->
            Log.d("MyLog", "Card with customerId ${transactions.customerId}")
            RemoteDatabaseCard(transactions = transactions)

        }
    }


}