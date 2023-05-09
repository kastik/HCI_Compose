package com.kastik.hci.ui.mainComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
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
import kotlin.random.Random

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

    val supplierNames = database.getSupplierNames().collectAsState(initial = emptyList())
    var selectedText by remember { mutableStateOf("Insert A Supplier First") }


    HCI_ComposeTheme() {
        Surface() {
            LazyColumn() {
                item() {
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
                            label = { Text("Supplier Name") }
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = supplierLocation,
                            onValueChange = { supplierLocation = it },
                            label = { Text("Supplier Location") }
                        )
                        FilledTonalButton(
                            modifier= Modifier
                                .align(Alignment.End)
                                .padding(10.dp),
                            onClick = {
                                database.insertSupplier(
                                    Supplier(
                                    Random.nextInt(),
                                    supplierName,supplierLocation
                                )
                                )
                                supplierName = ""
                                supplierLocation = ""
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
                            label = { Text("Product") }
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = manufacturer,
                            onValueChange = { manufacturer = it },
                            label = { Text("Manufacturer") }
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        OutlinedTextField(
                            shape= RoundedCornerShape(15.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Product Description") }
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
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                }
                            ) {
                                supplierNames.value.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedText = item
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        FilledTonalButton(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(10.dp),
                            onClick = {
                                database.insertStock(Stock(12,12))
                                database.insertProduct(
                                    Product(
                                    Random.nextInt(),productName, manufacturer,price.toInt(),description,697620083,12
                                )
                                )
                            }) { Text("Insert") }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun MainScreen() {
    Text(text = "Hi")
}


@Composable
@Preview
fun LocalDatabaseScreen() {
    val lazyListState = rememberLazyListState()
    val db = AppDatabase.getDatabase(LocalContext.current)
    val mdata = db.AppDao().getAllProducts().collectAsState(initial = emptyList())
    LazyColumn(Modifier.fillMaxSize()) {
        itemsIndexed(mdata.value) { index, item ->
            MyCard(product = item)
        }
    }
}



@Composable
fun RemoteDatabaseScreen() {
    val db = Firebase.firestore
    Text(text = "Remote Activity stuff")
}