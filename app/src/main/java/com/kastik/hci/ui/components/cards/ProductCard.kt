package com.kastik.hci.ui.components.cards

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kastik.hci.data.AppDao
import com.kastik.hci.data.AppDatabase
import com.kastik.hci.data.Product
import com.kastik.hci.data.Stock
import com.kastik.hci.data.Supplier
import com.kastik.hci.ui.screens.AvailableScreens
import com.kastik.hci.ui.theme.HCI_ComposeTheme
import com.kastik.hci.utils.modifierBasedOnAction
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun ProductCardPreview(){
    HCI_ComposeTheme() {
        Surface() {
            ProductCard(
                mutableStateOf(0),
                Product(1,1,1,"Pixel 2 XL","Google",100,"The latest phone from google"),
                Stock(1,20),
                Supplier(1,"",""),
                mutableStateOf(true),
                mutableStateOf(CardActions.Empty),
                AppDatabase.getDatabase(LocalContext.current).AppDao(),
                rememberNavController(),
                SnackbarHostState()
            )
        }
    }

}

@Composable
fun ProductCard(
    selectedProductId: MutableState<Int>,
    product: Product,
    stock: Stock,
    supplier: Supplier,
    actionsEnabled: MutableState<Boolean>,
    action: MutableState<CardActions>,
    dao: AppDao,
    navController: NavController,
    snackbarHostState: SnackbarHostState,


    //,onClick: () -> Unit
) {
    val database = AppDatabase.getDatabase(LocalContext.current).AppDao()
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(IntrinsicSize.Min),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(2.dp,MaterialTheme.colorScheme.inversePrimary),
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
                                    if (0 < database.deleteProduct(product)) {
                                        scope.launch { snackbarHostState.showSnackbar("Success!") }
                                    } else {
                                        scope.launch { snackbarHostState.showSnackbar("Something Happened Try Again") }
                                    }

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
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Stock Quantity", style = (MaterialTheme.typography.labelSmall))
                Text(text = stock.Stock.toString())

                Spacer(modifier = Modifier.padding(5.dp))

            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Description", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductDescription)
                Spacer(modifier = Modifier.padding(5.dp))

            }
        }

    }
}
