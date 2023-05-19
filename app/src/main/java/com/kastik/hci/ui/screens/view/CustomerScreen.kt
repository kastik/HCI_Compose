package com.kastik.hci.ui.screens.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.google.firebase.firestore.CollectionReference
import com.kastik.hci.data.CustomerData
import com.kastik.hci.ui.components.cards.CardActions
import com.kastik.hci.ui.components.cards.CustomerCard


@Composable
fun CustomerScreen(customerDb: CollectionReference,
                   showSelectionOnCard: MutableState<Boolean>,
                   action: MutableState<CardActions>,
                   selectedCustomerId: MutableState<String>,
                   navController: NavController,
                   snackbarHostState: SnackbarHostState
){
    val customers = remember { mutableStateListOf<CustomerData>() }

    customerDb.get().addOnSuccessListener {
        for (customer in it) {
            customers.add(customer.toObject(CustomerData::class.java))
        }
    }

    LazyColumn(){
        itemsIndexed(customers){index, customer ->
            CustomerCard(customer,selectedCustomerId,showSelectionOnCard,action, navController,snackbarHostState,customerDb)
        }
    }
}