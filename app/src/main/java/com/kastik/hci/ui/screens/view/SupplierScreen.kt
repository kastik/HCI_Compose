package com.kastik.hci.ui.screens.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.kastik.hci.data.AppDao
import com.kastik.hci.ui.components.cards.CardActions
import com.kastik.hci.ui.components.cards.SupplierCard


@Composable
fun SupplierScreen(
    dao : AppDao,
    showSelectionOnCard: MutableState<Boolean>,
    action: MutableState<CardActions>,
    selectedSupplierId: MutableState<Int>,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    val suppliers by dao.getAllSuppliers().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    LazyColumn {
        items(suppliers){
            SupplierCard(it,showSelectionOnCard,action,dao,snackbarHostState,navController,selectedSupplierId,scope)
        }
    }



}