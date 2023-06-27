package com.kastik.hci.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.data.CustomerData
import com.kastik.hci.data.Transaction
import com.kastik.hci.ui.screens.AvailableScreens
import com.kastik.hci.utils.modifierBasedOnAction
import kotlinx.coroutines.launch





@Composable
fun TransactionCard(
    selectedTransactionId: MutableState<String>,
    transaction: Transaction,
    actionsEnabled: MutableState<Boolean>,
    action: MutableState<CardActions>,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    customerDb: CollectionReference,
)

{
    val scope = rememberCoroutineScope()
    var customerData by remember { mutableStateOf(CustomerData()) }


    LaunchedEffect(Unit) {
        customerDb.document(transaction.customerId)
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()) {
                    customerData = document.toObject(CustomerData::class.java)!!
                }else{
                    customerData = CustomerData("0","Deleted Customer","Deleted Customer")
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure case if needed
                scope.launch {
                    snackbarHostState.showSnackbar("Failed to fetch customer data: ${exception.message}")
                }
            }
    }



    val transactionDb = Firebase.firestore.collection("Transactions")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(IntrinsicSize.Min),
        shape = MaterialTheme.shapes.large,
        //border = BorderStroke(2.dp, MaterialTheme.colorScheme.inversePrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row {
            AnimatedVisibility(visible = actionsEnabled.value) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .then(modifierBasedOnAction(action))
                        .padding(5.dp)
                        .clickable(
                            // enabled = actionsEnabled.value,
                            onClick = {
                                if (action.value == CardActions.Delete && actionsEnabled.value) {
                                    transactionDb.document(transaction.transactionId).delete().addOnSuccessListener {
                                        scope.launch { snackbarHostState.showSnackbar("Success!") }
                                    }.addOnFailureListener{
                                        scope.launch { snackbarHostState.showSnackbar("Something went wrong") }
                                    }
                                } else {
                                    if (action.value == CardActions.Modify && actionsEnabled.value) {
                                        selectedTransactionId.value = transaction.transactionId
                                        navController.navigate(AvailableScreens.EditTransactionScreen.name)
                                    }
                                }
                            }
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    if(action.value== CardActions.Delete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete the item")
                    }else{
                        Icon(Icons.Filled.Edit, contentDescription = "Delete the item")
                    }
                }
                //Checkbox(checked = checked.value, onCheckedChange = { checked.value = !checked.value })
            }


            Row {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    Text(text = "Product", style = (MaterialTheme.typography.labelMedium))
                    Text(text = transaction.productName,style = (MaterialTheme.typography.bodyLarge))
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(text = "Products Sold",style = (MaterialTheme.typography.labelMedium))
                    Text(text = transaction.quantitySold.toString(),style = (MaterialTheme.typography.bodyLarge))
                }
                Column(
                    Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    Text(text = "Customer Name", style = (MaterialTheme.typography.labelMedium))
                    Text(text = customerData.customerName,style = (MaterialTheme.typography.bodyLarge))
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(text = "Customer Lastname", style = (MaterialTheme.typography.labelMedium))
                    Text(text = customerData.customerLastName,style = (MaterialTheme.typography.bodyLarge))
                }
            }
        }

    }
}

