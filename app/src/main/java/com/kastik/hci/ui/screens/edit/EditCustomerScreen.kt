package com.kastik.hci.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.CollectionReference
import com.kastik.hci.data.CustomerData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCustomerScreen(
    customerDb: CollectionReference,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    selectedCustomerId: MutableState<String>,
) {
    var customerName by remember { mutableStateOf("") }
    var customerLastname by remember { mutableStateOf("") }

    val customer = remember { mutableStateListOf<CustomerData>() }

    LaunchedEffect(Unit) {
        customerDb.document(selectedCustomerId.value).get().addOnSuccessListener { customers ->
            customers.toObject(CustomerData::class.java)?.let {
                customer.add(it)
                customerName= it.customerName
                customerLastname = it.customerLastName
            }
        }
    }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Column(
        Modifier.wrapContentSize()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = "Edit Customer"
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
            })
        )

        FilledTonalButton(
            onClick = {
                customerDb.document(selectedCustomerId.value).update(
                    mapOf(
                        "customerName" to customerName,
                        "customerLastName" to customerLastname
                    )
                ).addOnSuccessListener {
                    scope.launch { snackbarHostState.showSnackbar("Success!") }
                }.addOnFailureListener {
                    scope.launch { snackbarHostState.showSnackbar("Something Happened. Try Again.") }
                }
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(10.dp)
        ) {
            Text("Update")
        }
    }
}