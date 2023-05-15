package com.kastik.hci.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.kastik.hci.database.AppDatabase
import com.kastik.hci.ui.components.CardActions
import com.kastik.hci.ui.components.LocalDatabaseCard

@Composable
fun LocalDataViewScreen(
    showSelectionOnCard: MutableState<Boolean>,
    action: MutableState<CardActions>,
    selectedProductId: MutableState<Int>,
    navController: NavController
) {
    val lazyListState = rememberLazyListState()
    val dao = AppDatabase.getDatabase(LocalContext.current).AppDao()
    val products = dao.getAllProducts().collectAsState(initial = emptyList())
    val stocks = dao.getAllStocks().collectAsState(initial = emptyList())
    val suppliers = dao.getAllSuppliers().collectAsState(initial = emptyList())

    //DataWrapper(productData,supplierData,stockData)
    LazyColumn(Modifier.fillMaxSize()) {
        items(products.value) { item ->
            LocalDatabaseCard(
                product = item,
                stock = dao.getStockOfProduct(item.StockId),
                supplier = dao.getSupplierInfo(item.SupplierId),
                actionsEnabled = showSelectionOnCard,
                action = action,
                navController = navController,
                selectedProductId = selectedProductId
            )
        }
    }
}