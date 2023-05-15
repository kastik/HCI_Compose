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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.google.firebase.firestore.CollectionReference
import com.kastik.hci.database.CustomerData
import com.kastik.hci.database.Transactions
import com.kastik.hci.utils.checkNumberInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerComponent(focusManager: FocusManager, customerDb: CollectionReference) {
    var customerName by remember { mutableStateOf("") }
    var customerLastname by remember { mutableStateOf("") }
    Column(
        Modifier.wrapContentSize()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = "Insert Customer"
        )
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name") },
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
            value = customerLastname,
            onValueChange = { customerLastname = it },
            label = { Text("Customer Lastname") },
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
                customerDb.add(
                    CustomerData(
                        customerName = customerName, customerLastName = customerLastname
                    )
                ).addOnSuccessListener {
                    Log.d("MyLog", "DataComplete")
                }.addOnFailureListener {
                    Log.d("MyLog", "Fail ${it.message}")
                }


                customerName = ""
                customerLastname = ""
                focusManager.clearFocus()
            }) { Text("Insert") }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionComponent(
    focusManager: FocusManager, transactionDb: CollectionReference, customerDb: CollectionReference
) {

    var customerId by remember { mutableStateOf("") }
    var product by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    /*
    if (product != null) {
        customerId = product.ProductName
        customerId = product.ProductManufacturer
        customerId =product.ProductPrice.toString()

    } */


    var expanded by remember { mutableStateOf(false) }
    val customerData = remember { mutableStateListOf<CustomerData>() }
    val temp = customerDb.get().addOnSuccessListener { documents ->
        for (document in documents) {
            customerData.add((document.toObject(CustomerData::class.java)))
        }
    }
    var selectedText by remember { mutableStateOf("Insert/Select A Supplier First") }

    val selectedSupplier = remember { mutableStateOf("") }



    var quantityError by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentSize()
        //.fillMaxHeight()
        //.verticalScroll(rememberScrollState())

    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(10.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = "Insert/Edit Transaction"
        )
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = product,
            onValueChange = { product = it },
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
            isError = quantityError,
            value = quantity,
            onValueChange = {
                quantity = it
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
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

            ExposedDropdownMenu(expanded = if (customerData.isEmpty()) {
                false
            } else {
                expanded
            }, onDismissRequest = {
                expanded = false
            }) {
                customerData.forEach { item ->
                    DropdownMenuItem(text = { Text(text = item.customerName) }, onClick = {
                        selectedSupplier.value = item.customerName
                        selectedText = item.customerName
                        expanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))

        FilledTonalButton(modifier = Modifier
            .align(Alignment.End)
            .padding(10.dp), onClick = {

            if(!quantityError){
                transactionDb.add(
                    Transactions(
                        customerId = customerData[0].customerId,
                        product = product,
                        quantity = quantity.toInt()
                    )
                )

                product = ""
                quantity = ""
            } }){ Text("Insert") }
    }


}