package com.kastik.hci.ui.screens.view

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.data.Transaction
import com.kastik.hci.ui.components.cards.CardActions
import com.kastik.hci.ui.components.cards.TransactionCard

@Composable
fun TransactionScreen(
    showSelectionOnCard: MutableState<Boolean>,
    action: MutableState<CardActions>,
    selectedTransactionId: MutableState<String>,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {




    val transactions = Firebase.firestore.collection("Transactions")

    var test = 0
    val myData = remember { mutableStateListOf<Transaction>() }

    transactions.get().addOnSuccessListener { documents ->
        for (document in documents) {
            myData.add(document.toObject(Transaction::class.java))
        }
    }

    LazyColumn {
        itemsIndexed(myData) { index,transactions ->
            Log.d("MyLog",index.toString())
            TransactionCard(
                transactionId = selectedTransactionId,
                actionsEnabled = showSelectionOnCard,
                action=action,
                navController=navController,
                transaction = transactions,
                snackbarHostState = snackbarHostState)
        }
    }


}