package com.kastik.hci.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.kastik.hci.database.AppDatabase
import com.kastik.hci.ui.components.ProductComponent
import com.kastik.hci.ui.components.SupplierComponent
import com.kastik.hci.ui.theme.HCI_ComposeTheme

@Composable
fun InsertLocal(productId: Int?) {
    val database = AppDatabase.getDatabase(LocalContext.current).AppDao()
    val product = database.getProductWithId(productId)

    HCI_ComposeTheme {
        Surface {
            val focusManager = LocalFocusManager.current
            LazyColumn {
                item {
                    SupplierComponent(dao = database, focusManager = focusManager)
                }
                item {
                    ProductComponent(dao = database, focusManager = focusManager, product = product)
                }

            }
        }
    }
}