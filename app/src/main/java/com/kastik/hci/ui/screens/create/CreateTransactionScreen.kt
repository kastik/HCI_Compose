package com.kastik.hci.ui.screens.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.google.firebase.firestore.CollectionReference
import com.kastik.hci.data.AppDao
import com.kastik.hci.data.CustomerData
import com.kastik.hci.data.Product
import com.kastik.hci.data.Transaction
import com.kastik.hci.utils.checkNumberInput
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(
    transactionDb: CollectionReference,
    customerDb: CollectionReference,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    dao: AppDao
) {
    val quantity = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var customerExpanded by remember { mutableStateOf(false) }
    var productExpanded by remember { mutableStateOf(false) }
    val customerData = remember { mutableStateListOf<CustomerData>() }
    val productData = remember { mutableStateOf(dao.getAllProducts()) }
    val selectedCustomerText = remember { mutableStateOf("") }
    val selectedProductIdText = remember { mutableStateOf("") }
    val selectedCustomer = remember { mutableStateOf(CustomerData()) }
    val selectedProduct = remember { mutableStateOf(Product(0, 0, 0, "", "", 0, "")) }
    var quantityError by rememberSaveable { mutableStateOf(false) }
    var showPopUp by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        customerDb.get().addOnSuccessListener { documents ->
            for (document in documents) {
                customerData.add(document.toObject(CustomerData::class.java))
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    text = "Insert A Transaction"
                )

                Spacer(modifier = Modifier.padding(10.dp))

                OutlinedTextField(
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    isError = quantityError,
                    value = quantity.value,
                    onValueChange = {
                        quantity.value = it
                        quantityError = checkNumberInput(it)
                    },
                    supportingText = {
                        if (quantityError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Please provide only numbers",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    label = { Text("Quantity") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    trailingIcon = {
                        if (quantityError)
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    expanded = customerExpanded,
                    onExpandedChange = {
                        customerExpanded = !customerExpanded
                        productExpanded = false
                    }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor().padding(10.dp),
                        value = selectedCustomerText.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = customerExpanded
                            )
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = if (customerData.isEmpty()) {
                            false
                        } else {
                            customerExpanded
                        },
                        onDismissRequest = {
                            customerExpanded = false
                        }
                    ) {
                        customerData.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item.customerName) }, onClick = {
                                selectedCustomer.value = item
                                selectedCustomerText.value = item.customerName
                                customerExpanded = false
                            })
                        }
                    }
                }

                AnimatedVisibility(visible = showPopUp) {
                    Popup(alignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(200.dp, 70.dp)
                                .background(
                                    MaterialTheme.colorScheme.error,
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "You don't have enough stock for this transaction")
                            FilledTonalButton(onClick = {
                                showPopUp = false
                            }) {
                                Text(text = "OK")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    expanded = productExpanded,
                    onExpandedChange = {
                        productExpanded = !productExpanded
                        customerExpanded = false
                    }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor().padding(10.dp),
                        value = selectedProductIdText.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = productExpanded
                            )
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = if (productData.value.collectAsState(emptyList()).value.isEmpty()) {
                            false
                        } else {
                            productExpanded
                        },
                        onDismissRequest = {
                            productExpanded = false
                        }
                    ) {
                        productData.value.collectAsState(emptyList()).value.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item.ProductName) }, onClick = {
                                selectedProduct.value = item
                                selectedProductIdText.value = item.ProductName
                                productExpanded = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                FilledTonalButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(10.dp),
                    onClick = {
                        if (!quantityError) {
                            val stock = dao.getStockOfProduct(selectedProduct.value.StockId)
                            if (stock.Stock >= quantity.value.toInt()) {
                                transactionDb.add(
                                    Transaction(
                                        customerId = selectedCustomer.value.customerId,
                                        productName = selectedProduct.value.ProductName,
                                        quantitySold = quantity.value.toInt(),
                                        productId = selectedProduct.value.ProductId
                                    )
                                ).addOnSuccessListener {
                                    scope.launch { snackbarHostState.showSnackbar("Success!") }
                                    dao.updateStock(
                                        stock.copy(
                                            Stock = stock.Stock - quantity.value.toInt()
                                        )
                                    )
                                }
                                navController.popBackStack()
                            } else {
                                showPopUp = true
                            }
                        }
                    }
                ) {
                    Text(text = "Insert")
                }
            }
        }
    }
    }