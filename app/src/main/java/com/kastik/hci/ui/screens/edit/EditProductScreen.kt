package com.kastik.hci.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kastik.hci.data.AppDao
import com.kastik.hci.data.Product
import com.kastik.hci.data.Stock
import com.kastik.hci.utils.checkNumberInput
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(productId: Int,dao: AppDao,snackbarHostState: SnackbarHostState,navController: NavController){

    val product = dao.getProductWithId(productId)

    val productName = remember { mutableStateOf(product.ProductName) }
    val manufacturer = remember { mutableStateOf(product.ProductManufacturer) }
    val price = remember { mutableStateOf(product.ProductPrice.toString()) }
    val description = remember { mutableStateOf(product.ProductDescription) }
    val stock = remember { mutableStateOf(dao.getStockOfProduct(product.StockId).Stock.toString()) }
    val focusManager = LocalFocusManager.current

    var priceError by rememberSaveable { mutableStateOf(false) }
    var quantityError by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()



    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentSize()
        //.fillMaxHeight()
        //.verticalScroll(rememberScrollState())

    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = "Insert Product"
        )
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = productName.value,
            onValueChange = { productName.value = it },
            label = { Text("Product") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.moveFocus(FocusDirection.Down)
                //focusRequester.requestFocus()
            })
        )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = manufacturer.value,
            onValueChange = { manufacturer.value = it },
            label = { Text("Manufacturer") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.moveFocus(FocusDirection.Down)
                //focusRequester.requestFocus()
            })
        )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = price.value,
            supportingText = {
                if (priceError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Please provide only numbers",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            onValueChange = {
                price.value = it
                priceError = checkNumberInput(it)
            },
            isError = priceError,
            label = { Text("Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.moveFocus(FocusDirection.Down)
                //focusRequester.requestFocus()
            }),
            trailingIcon = {
                if (quantityError)
                    Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
            }
        )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Product Description") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.moveFocus(FocusDirection.Down)
                //focusRequester.requestFocus()
            })
        )
        Spacer(modifier = Modifier.padding(10.dp))

        OutlinedTextField(
            isError = quantityError,
            supportingText = {
                if (quantityError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Please provide only numbers",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = stock.value,
            onValueChange = {
                stock.value = it
                quantityError = checkNumberInput(it)
            },
            label = { Text("Quantity") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                //focusRequester.requestFocus()
            }),
            trailingIcon = {
                if (quantityError)
                    Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
            }
        )
        FilledTonalButton(modifier = Modifier
            .align(Alignment.End)
            .padding(10.dp), onClick = {


            if (!priceError || !quantityError) {
                    dao.updateStock(Stock(Stock = stock.value.toInt(), StockId = product.StockId))
                    if (0 <
                        dao.updateProduct(
                            Product(
                                ProductId = product.ProductId,
                                SupplierId = product.SupplierId,
                                ProductName = productName.value,
                                ProductManufacturer = manufacturer.value,
                                ProductPrice = price.value.toInt(),
                                ProductDescription = description.value,
                                StockId = product.StockId
                            )
                        )
                    ) {
                        scope.launch {  snackbarHostState.showSnackbar("Success!")}
                        navController.popBackStack()
                    } else {
                        scope.launch {  snackbarHostState.showSnackbar("Something Happened Try Again")}
                    }

            }

            productName.value = ""
            manufacturer.value = ""
            price.value = ""
            description.value = ""
            stock.value = ""
        }) { Text("Insert") }
    }



}