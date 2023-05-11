package com.kastik.hci.ui.mainComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.database.AppDatabase
import com.kastik.hci.database.Product
import com.kastik.hci.database.Stock
import com.kastik.hci.database.Supplier
import com.kastik.hci.ui.theme.HCI_ComposeTheme

enum class Screens {
    MainScreen,
    LocalDatabaseScreen,
    RemoteDatabaseScreen,
    InsertProducScreen,
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun InsertProducScreen() {
    val database = AppDatabase.getDatabase(LocalContext.current).AppDao()
    var supplierName by remember { mutableStateOf("") }
    var supplierLocation by remember { mutableStateOf("") }

    var productName by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val supplierNames = database.getAllSuppliers().collectAsState(initial = emptyList())
    var selectedText by remember { mutableStateOf("Insert/Select A Supplier First") }

    var selectedSupplier = Supplier(Name="",Location="")


    HCI_ComposeTheme() {
        Surface() {
            val focusManager = LocalFocusManager.current
            Column(
                Modifier
                    .clickable(
                        onClick = {
                            focusManager.clearFocus()
                        })
                    .verticalScroll(rememberScrollState())
            ){
                    Column(
                        Modifier
                            .fillMaxWidth()
                        //.fillMaxHeight()
                        //.verticalScroll(rememberScrollState())

                    ) {

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            text = "Insert Supplier"
                        )
                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = supplierName,
                            onValueChange = { supplierName = it },
                            label = { Text("Supplier Name") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    //focusRequester.requestFocus()
                                }
                            )

                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = supplierLocation,
                            onValueChange = { supplierLocation = it },
                            label = { Text("Supplier Location") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    //focusRequester.requestFocus()
                                }
                            ))
                        FilledTonalButton(

                            modifier= Modifier
                                .align(Alignment.End)
                                .padding(10.dp),
                            onClick = {
                                database.insertSupplier(Supplier(Name = supplierName,Location = supplierLocation))


                                supplierName = ""
                                supplierLocation = ""
                                focusManager.clearFocus()
                            }) { Text("Insert") }

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            text = "Create Product"
                        )


                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = productName,
                            onValueChange = { productName = it },
                            label = { Text("Product") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    //focusRequester.requestFocus()
                                }
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = manufacturer,
                            onValueChange = { manufacturer = it },
                            label = { Text("Manufacturer") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    //focusRequester.requestFocus()
                                }
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = price,
                            onValueChange =
                            {
                                if(it.isEmpty() || it.matches(Regex("^\\d+\$"))) {
                                    price = it }
                            },
                            label = { Text("Price") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    //focusRequester.requestFocus()
                                }
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Product Description") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    //focusRequester.requestFocus()
                                }
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        ExposedDropdownMenuBox(
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
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
                                }
                            )

                            ExposedDropdownMenu(
                                expanded = if(supplierNames.value.isEmpty()){ false }else{ expanded },
                                onDismissRequest = {
                                    expanded = false
                                }
                            ) {
                                supplierNames.value.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item.Name) },
                                        onClick = {
                                            selectedSupplier = item
                                            selectedText = item.Name
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        OutlinedTextField(

                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = stock,
                            onValueChange =
                            {
                                if(it.isEmpty() || it.matches(Regex("^\\d+\$"))) {
                                    stock = it }
                            },
                            label = { Text("Quantity") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    //focusRequester.requestFocus()
                                }
                            )
                        )
                        FilledTonalButton(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(10.dp),
                            onClick = {
                                    val stockId = database.insertStock(Stock(Stock = stock.toInt()))
                                    database.insertProduct(
                                        Product(
                                            SupplierId=selectedSupplier.SupplierId,
                                            ProductName=productName,
                                            ProductManufacturer = manufacturer,
                                            ProductPrice = price.toInt(),
                                            ProductDescription = description,
                                            StockId = stockId.toInt()))

                                productName=""
                                manufacturer=""
                                price=""
                                description=""
                                stock=""
                            }) { Text("Insert") }
                    }
                }
            }
        }
}


@Composable
fun MainScreen() {
    Text(text = "Hi")
}


@Composable
fun LocalDatabaseScreen() {
    val lazyListState = rememberLazyListState()
    val dao = AppDatabase.getDatabase(LocalContext.current).AppDao()
    val products = dao.getAllProducts().collectAsState(initial = emptyList())
    val stocks = dao.getAllStocks().collectAsState(initial = emptyList())
    val suppliers = dao.getAllSuppliers().collectAsState(initial = emptyList())

    //DataWrapper(productData,supplierData,stockData)
    LazyColumn(Modifier.fillMaxSize()) {
        items(products.value) { item ->
            LocalDatabaseCard(product = item, stock = dao.getStockOfProduct(item.StockId), supplier = dao.getSupplierInfo(item.SupplierId))
        }
    }
}



@Composable
fun RemoteDatabaseScreen() {
    val db = Firebase.firestore
    val transactions = db.collection("Transactions")
    val customers = db.collection("Customer").get()
    var chats = remember {
        mutableStateOf(emptyList<Transactions>())
    }


    transactions.addSnapshotListener { snapshot, e ->
        e?.let {
            // Handle error
            return@addSnapshotListener
        }

    }

    LazyColumn(){

    }

    Text(text = "Remote Activity stuff")
}