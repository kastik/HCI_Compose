package com.kastik.hci.ui.screens.edit

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kastik.hci.data.AppDao
import com.kastik.hci.data.Supplier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSupplierScreen(
    supplierId: Int,
    dao: AppDao,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    scope: CoroutineScope
) {


    val supplier = dao.getSupplierWithId(supplierId)

    val supplierName = remember { mutableStateOf(supplier.Name) }
    val supplierLocation = remember { mutableStateOf(supplier.Location) }

    val focusManager = LocalFocusManager.current
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
            text = "Edit Supplier"
        )
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = supplierName.value,
            onValueChange = { supplierName.value = it },
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
            value = supplierLocation.value,
            onValueChange = { supplierLocation.value = it },
            label = { Text("Supplier Location") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.moveFocus(FocusDirection.Down)
                //focusRequester.requestFocus()
            })
        )
        Spacer(modifier = Modifier.padding(10.dp))

        FilledTonalButton(modifier = Modifier
            .align(Alignment.End)
            .padding(10.dp), onClick = {



                if (0 <
                    dao.updateSupplier(
                        Supplier(
                            supplierId,
                            supplierName.value,
                            supplierLocation.value
                        )
                    )
                ) {
                    scope.launch {  snackbarHostState.showSnackbar("Success!")}
                    navController.popBackStack()
                } else {
                    scope.launch {  snackbarHostState.showSnackbar("Something Happened Try Again")}
                }



                supplierName.value = ""
                supplierLocation.value = ""
        }) { Text("Update") }
    }



}