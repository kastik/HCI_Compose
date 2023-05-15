package com.kastik.hci.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.ui.components.CustomerComponent
import com.kastik.hci.ui.components.TransactionComponent
import com.kastik.hci.ui.theme.HCI_ComposeTheme

@Composable
fun InsertRemoteScreen() {

    val customerDb = Firebase.firestore.collection("Customers")
    val transactionDb = Firebase.firestore.collection("Transactions")

    HCI_ComposeTheme {
        Surface {
            val focusManager = LocalFocusManager.current
            LazyColumn {
                item {
                    CustomerComponent(focusManager = focusManager, customerDb)
                }
                item {
                    TransactionComponent(
                        focusManager, transactionDb, customerDb
                    )
                }

            }
        }
    }


}