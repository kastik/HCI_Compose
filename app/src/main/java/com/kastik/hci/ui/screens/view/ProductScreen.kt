package com.kastik.hci.ui.screens.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kastik.hci.data.AppDao
import com.kastik.hci.ui.components.cards.CardActions
import com.kastik.hci.ui.components.cards.ProductCard

@Composable
fun ProductScreen(
    dao : AppDao,
    showSelectionOnCard: MutableState<Boolean>,
    action: MutableState<CardActions>,
    selectedProductId: MutableState<Int>,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    //val lazyListState = rememberLazyListState()
    val products = dao.getAllProducts().collectAsState(initial = emptyList())


    LazyColumn(Modifier.fillMaxSize()) {
        items(products.value) { item ->
            ProductCard(
                product = item,
                stock = dao.getStockOfProduct(item.StockId),
                actionsEnabled = showSelectionOnCard,
                action = action,
                navController = navController,
                selectedProductId = selectedProductId,
                snackbarHostState = snackbarHostState,
                dao = dao
            )
        }
    }
}