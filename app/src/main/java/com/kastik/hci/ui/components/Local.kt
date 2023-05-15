package com.kastik.hci.ui.components

import android.util.Log
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kastik.hci.database.AppDao
import com.kastik.hci.database.Product
import com.kastik.hci.database.Stock
import com.kastik.hci.database.Supplier
import com.kastik.hci.utils.checkNumberInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductComponent(dao: AppDao, focusManager: FocusManager, product: Product?) {
    var productName by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    if (product != null) {
        Log.d("paok", "Product ! null")
        productName = product.ProductName
        manufacturer = product.ProductManufacturer
        price = product.ProductPrice.toString()
        description = product.ProductDescription
        stock = dao.getStockOfProduct(product.StockId).Stock.toString()
    }


    var expanded by remember { mutableStateOf(false) }
    val supplierNames = dao.getAllSuppliers().collectAsState(initial = emptyList())
    var selectedText by remember { mutableStateOf("Insert/Select A Supplier First") }

    val selectedSupplier = remember { mutableStateOf(Supplier(Name = "", Location = "")) }


    var priceError by rememberSaveable { mutableStateOf(false) }
    var quantityError by rememberSaveable { mutableStateOf(false) }

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
            value = productName,
            onValueChange = { productName = it },
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
            value = manufacturer,
            onValueChange = { manufacturer = it },
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
            value = price,
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
                price = it
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
            value = description,
            onValueChange = { description = it },
            label = { Text("Product Description") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.moveFocus(FocusDirection.Down)
                //focusRequester.requestFocus()
            })
        )
        Spacer(modifier = Modifier.padding(10.dp))
        ExposedDropdownMenuBox(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }) {
            OutlinedTextField(modifier = Modifier
                .menuAnchor()
                //.align(Alignment.End)
                .padding(10.dp),
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                })

            ExposedDropdownMenu(expanded = if (supplierNames.value.isEmpty()) {
                false
            } else {
                expanded
            }, onDismissRequest = {
                expanded = false
            }) {
                supplierNames.value.forEach { item ->
                    DropdownMenuItem(text = { Text(text = item.Name) }, onClick = {
                        selectedSupplier.value = item
                        selectedText = item.Name
                        expanded = false
                    })
                }
            }
        }
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
            value = stock,
            onValueChange = {
                stock = it
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
            val stockId = dao.insertStock(Stock(Stock = stock.toInt()))

            Log.d("selectedSupplier.SupplierId", selectedSupplier.value.SupplierId.toString())
            Log.d("stockId.toInt()", stockId.toInt().toString())
            if (!priceError || !quantityError) {
                if (product == null) {
                    dao.insertProduct(
                        Product(
                            SupplierId = selectedSupplier.value.SupplierId,
                            ProductName = productName,
                            ProductManufacturer = manufacturer,
                            ProductPrice = price.toInt(),
                            ProductDescription = description,
                            StockId = stockId.toInt()
                        )
                    )
                } else {
                    dao.updateProduct(
                        Product(
                            ProductId = product.ProductId,
                            SupplierId = selectedSupplier.value.SupplierId,
                            ProductName = productName,
                            ProductManufacturer = manufacturer,
                            ProductPrice = price.toInt(),
                            ProductDescription = description,
                            StockId = stockId.toInt()
                        )
                    )
                }
            }


            productName = ""
            manufacturer = ""
            price = ""
            description = ""
            stock = ""
        }) { Text("Insert") }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierComponent(dao: AppDao, focusManager: FocusManager) {

    var supplierName by remember { mutableStateOf("") }
    var supplierLocation by remember { mutableStateOf("") }
    Column(
        Modifier.wrapContentSize()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = "Insert Supplier"
        )
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = supplierName,
            onValueChange = { supplierName = it },
            label = { Text("Supplier Name") },
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
            value = supplierLocation,
            onValueChange = { supplierLocation = it },
            label = { Text("Supplier Location") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                //focusRequester.requestFocus()
            })
        )
        FilledTonalButton(

            modifier = Modifier
                .align(Alignment.End)
                .padding(10.dp), onClick = {
                dao.insertSupplier(Supplier(Name = supplierName, Location = supplierLocation))


                supplierName = ""
                supplierLocation = ""
                focusManager.clearFocus()
            }) { Text("Insert") }
    }
}