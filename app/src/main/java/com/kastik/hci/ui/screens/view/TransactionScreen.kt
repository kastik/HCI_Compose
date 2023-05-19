package com.kastik.hci.ui.screens.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.google.firebase.firestore.CollectionReference
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
    snackbarHostState: SnackbarHostState,
    customerDb: CollectionReference
) {



    val transactionsCollection = Firebase.firestore.collection("Transactions")

    val myData = remember { mutableStateListOf<Transaction>() }

    LaunchedEffect(Unit) {
        transactionsCollection.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val transaction = document.toObject(Transaction::class.java)
                myData.add(transaction)
            }
        }
    }

    LazyColumn {
        items(myData) { transaction ->
            TransactionCard(
                selectedTransactionId = selectedTransactionId,
                actionsEnabled = showSelectionOnCard,
                action = action,
                navController = navController,
                transaction = transaction,
                customerDb = customerDb,
                snackbarHostState = snackbarHostState
            )
        }
    }


}