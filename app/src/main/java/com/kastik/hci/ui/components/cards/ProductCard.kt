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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kastik.hci.data.AppDao
import com.kastik.hci.data.Product
import com.kastik.hci.data.Stock
import com.kastik.hci.ui.screens.AvailableScreens
import com.kastik.hci.utils.modifierBasedOnAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun ProductCard(
    selectedProductId: MutableState<Int>,
    product: Product,
    stock: Stock,
    actionsEnabled: MutableState<Boolean>,
    action: MutableState<CardActions>,
    dao: AppDao,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {

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
                            onClick = {
                                scope.launch {
                                    if (action.value == CardActions.Delete && actionsEnabled.value) {
                                        if (0 < dao.deleteProduct(product)) {
                                            snackbarHostState.showSnackbar("Success!")
                                        } else {
                                            snackbarHostState.showSnackbar("Something Happened. Try Again.")
                                        }
                                    } else if (action.value == CardActions.Modify && actionsEnabled.value) {
                                        selectedProductId.value = product.ProductId
                                        navController.navigate(AvailableScreens.EditProductScreen.name)
                                    }
                                }
                            }
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    if (action.value == CardActions.Delete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete the item")
                    } else {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit the item")
                    }
                }
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
                Text(text = "Price", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductPrice.toString())
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Supplier", style = (MaterialTheme.typography.labelSmall))
                Text(text = dao.getSupplierWithId(product.SupplierId).Name)
            }

            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Description", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductDescription)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Stock", style = (MaterialTheme.typography.labelSmall))
                Text(text = stock.Stock.toString())
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
    }
}