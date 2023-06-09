package com.kastik.hci.ui.screens.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCustomerScreen(
    customerDb: CollectionReference,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    scope: CoroutineScope

) {
    var customerName by remember { mutableStateOf("") }
    var customerLastname by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = "Register A New Customer"
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
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
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
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.padding(10.dp))

        FilledTonalButton(
            modifier = Modifier.padding(10.dp)
                .align(Alignment.End),
            onClick = {
                if (customerName.isNotEmpty() && customerLastname.isNotEmpty()) {
                    val newCustomer = CustomerData(
                        customerName = customerName,
                        customerLastName = customerLastname
                    )

                    customerDb.add(newCustomer)
                        .addOnSuccessListener {
                            scope.launch {
                                snackbarHostState.showSnackbar("Success!")
                            }
                        }

                    navController.popBackStack()
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Please enter both name and lastname.")
                    }
                }
            }
        ) {
            Text(text = "Insert")
        }
    }
}

