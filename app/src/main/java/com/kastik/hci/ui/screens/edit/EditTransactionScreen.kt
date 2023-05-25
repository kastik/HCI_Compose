package com.kastik.hci.ui.screens.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.firebase.firestore.CollectionReference
import com.kastik.hci.data.AppDao
import com.kastik.hci.data.CustomerData
import com.kastik.hci.data.Product
import com.kastik.hci.utils.checkNumberInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    customerDb: CollectionReference,
    transactionDb: CollectionReference,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    selectedTransactionId: MutableState<String>,
    dao: AppDao,
    scope: CoroutineScope
) {
    val quantity = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    var customerExpanded by remember { mutableStateOf(false) }
    var productExpanded by remember { mutableStateOf(false) }

    val productData = remember { mutableStateOf(dao.getAllProducts()) }
    val customerData = remember { mutableStateListOf<CustomerData>() }

    LaunchedEffect(Unit) {
        customerDb.get().addOnSuccessListener { customers ->
            for (customer in customers) {
                customerData.add(customer.toObject(CustomerData::class.java))
            }
        }
    }


    val selectedCustomerText = remember { mutableStateOf("Select A Customer") }
    val selectedProductIdText = remember { mutableStateOf("Select A Product") }


    val selectedCustomer = remember { mutableStateOf(CustomerData()) }
    val selectedProduct = remember { mutableStateOf(Product(0, 0, 0, "", "", 0, "")) }
    var quantityError by rememberSaveable { mutableStateOf(false) }
    var showPopUp by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = "Edit the Transaction"
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
                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error)
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
                modifier = Modifier
                    .menuAnchor()
                    .padding(10.dp),
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
                    DropdownMenuItem(
                        text = { Text(text = item.customerName) },
                        onClick = {
                            selectedCustomer.value = item
                            selectedCustomerText.value = item.customerName
                            customerExpanded = false
                        }
                    )
                }
            }
        }

        AnimatedVisibility(visible = showPopUp) {
            Box(
                modifier = Modifier
                    .size(200.dp, 70.dp)
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "There isn't enough stock for this transaction",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onError
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showPopUp = false
                        }
                    ) {
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
                modifier = Modifier
                    .menuAnchor()
                    .padding(10.dp),
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
                expanded = if (productData.value.collectAsState(initial = emptyList()).value.isEmpty()) {
                    false
                } else {
                    productExpanded
                },
                onDismissRequest = {
                    productExpanded = false
                }
            ) {
                productData.value.collectAsState(initial = emptyList()).value.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.ProductName) },
                        onClick = {
                            selectedProduct.value = item
                            selectedProductIdText.value = item.ProductName
                            productExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(10.dp),
            onClick = {
                if (!quantityError) {
                    transactionDb.document(selectedTransactionId.value).update(
                        mapOf(
                            "customerId" to selectedCustomer.value.customerId,
                            "productName" to selectedProduct.value.ProductName,
                            "quantitySold" to quantity.value.toInt(),
                            "productId" to selectedProduct.value.ProductId
                        )
                    ).addOnSuccessListener {
                        scope.launch { snackbarHostState.showSnackbar("Success") }
                    }.addOnFailureListener {
                        scope.launch { snackbarHostState.showSnackbar("Something Happened. Please Try Again") }
                    }
                    navController.popBackStack()
                }
            }
        ) {
            Text("Update")
        }
    }
}