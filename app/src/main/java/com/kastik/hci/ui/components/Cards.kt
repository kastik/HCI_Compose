package com.kastik.hci.ui.components

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.database.AppDatabase
import com.kastik.hci.database.CustomerData
import com.kastik.hci.database.Product
import com.kastik.hci.database.Stock
import com.kastik.hci.database.Supplier
import com.kastik.hci.database.Transactions
import com.kastik.hci.ui.screens.AvailableScreens
import com.kastik.hci.utils.modifierBasedOnAction


enum class CardActions { Empty, Delete, Modify }

@Composable
fun LocalDatabaseCard(
    product: Product,
    stock: Stock,
    supplier: Supplier,
    actionsEnabled: MutableState<Boolean>,
    action: MutableState<CardActions>,
    navController: NavController,
    selectedProductId: MutableState<Int>

    //,onClick: () -> Unit
) {
    val database = AppDatabase.getDatabase(LocalContext.current).AppDao()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(IntrinsicSize.Min)
        //shape = MaterialTheme.shapes.large,
        //border = BorderStroke(2.dp,MaterialTheme.colorScheme.inversePrimary),
        //elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
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
                                    database.deleteProduct(product)
                                } else {
                                    if (action.value == CardActions.Modify && actionsEnabled.value) {
                                        selectedProductId.value = product.ProductId
                                        navController.navigate(AvailableScreens.EditProductScreen.name)
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

            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Product", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductName)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Manufacturer", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductManufacturer)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Description", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductDescription)
                Spacer(modifier = Modifier.padding(5.dp))

            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Supplier", style = (MaterialTheme.typography.labelSmall))
                Text(text = supplier.Name)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Supplier Location", style = (MaterialTheme.typography.labelSmall))
                Text(text = supplier.Location)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Stock Quantity", style = (MaterialTheme.typography.labelSmall))
                Text(text = stock.Stock.toString())
            }
        }

    }
}

@Composable
fun RemoteDatabaseCard(
    transactions: Transactions
) {
    var customerData by remember { mutableStateOf(CustomerData()) }


    val q = Firebase.firestore.collection("Customers")
        .whereEqualTo("customerId",transactions.customerId)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                customerData = (document.toObject(CustomerData::class.java))
                Log.d("MyLog", "${document.data}")
                Log.d("MyLog", document.id)
            }
        }
    Log.d("Mylog","Exited")
    var expand = remember { false }
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(IntrinsicSize.Min)
            .clickable(onClick = {
                expand = !expand
            })
        //shape = MaterialTheme.shapes.large,
        //border = BorderStroke(2.dp,MaterialTheme.colorScheme.inversePrimary),
        //elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(5.dp)
        ) {
            Text(text = "Products Sold", style = (MaterialTheme.typography.labelSmall))
            Text(text = transactions.quantity.toString())
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Product Id", style = (MaterialTheme.typography.labelSmall))
            Text(text = transactions.product)
        }
        Column(
            Modifier
                .weight(1f)
                .padding(5.dp)
        ) {
            Text(text = "Customer Name", style = (MaterialTheme.typography.labelSmall))
            Text(text = customerData.customerName)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Customer Lastname", style = (MaterialTheme.typography.labelSmall))
            Text(text = customerData.customerLastName)
            Spacer(modifier = Modifier.padding(5.dp))
        }

    }
}
