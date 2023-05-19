package com.kastik.hci.ui.screens.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
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
import com.kastik.hci.data.AppDao
import com.kastik.hci.data.Supplier
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSupplierScreen(
    dao: AppDao,
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {
    var supplierName by remember { mutableStateOf("") }
    var supplierLocation by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Supplier Register"
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
                })
            )

            Spacer(modifier = Modifier.padding(10.dp))

            FilledTonalButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    scope.launch {
                        dao.insertSupplier(
                            Supplier(
                                Name = supplierName,
                                Location = supplierLocation
                            )
                        )
                    }
                    navController.popBackStack()
                }
            ) {
                Text(text = "Insert")
            }
        }
    }
}
