package com.kastik.hci.ui.mainComponents

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.database.AppDatabase
import com.kastik.hci.database.CustomerData
import com.kastik.hci.database.Product
import com.kastik.hci.database.Stock
import com.kastik.hci.database.Supplier
import com.kastik.hci.database.Transactions
import com.kastik.hci.ui.theme.HCI_ComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class CardActions { Empty, Delete, Modify }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUI() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val topBarState = remember { (mutableStateOf(true)) }
    val selectedItem = remember { mutableStateOf(1) }
    val dropDownState = remember { mutableStateOf(false) }
    val drawerGestureEnabled = remember { mutableStateOf(true) }
    val showSelectionOnCard = remember { mutableStateOf(false) }
    val action = remember { mutableStateOf(CardActions.Empty) }
    val selectedProductId = remember { mutableStateOf(0) }

    HCI_ComposeTheme {
        Surface {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = { DrawerSheet(navController, selectedItem) },
                gesturesEnabled = drawerGestureEnabled.value,
                content = {
                    Scaffold(
                        topBar = {
                            MyTopBar(
                                scope = scope,
                                drawerState = drawerState,
                                navController = navController,
                                topBarState = topBarState,
                                dropDownState = dropDownState,
                                showSelectionOnCard = showSelectionOnCard,
                                action= action
                            )
                        },
                    ){ paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Screens.MainScreen.name,
                            Modifier.padding(paddingValues)
                        ) {
                            composable(Screens.MainScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 1
                                dropDownState.value = false
                                drawerGestureEnabled.value = true
                                MainScreen()
                            }
                            composable(Screens.LocalDatabaseScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 2
                                dropDownState.value = true
                                drawerGestureEnabled.value = true
                                LocalDatabaseScreen(showSelectionOnCard,action,selectedProductId,navController)
                            }
                            composable(Screens.RemoteDatabaseScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 3
                                dropDownState.value = true
                                drawerGestureEnabled.value = true
                                RemoteDatabaseScreen()
                            }
                            composable(Screens.InsertProducScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                Log.d("MyLog","Insert")
                                InsertScreen(productId = null)

                            }
                            composable(Screens.EditProductScreen.name){ //TODO
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                Log.d("MyLog","Edit with id ${it.arguments?.getInt("productId")}")
                                InsertScreen(productId = selectedProductId.value)

                            }
                            composable(Screens.InsertTransactionScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                InsertTransactionScreen()

                            }
                        }
                    }
                })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerSheet(navController: NavController, selectedItem: MutableState<Int>) {
    ModalDrawerSheet {
        Modifier.size(50.dp)
        Text("Drawer title", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Main") },
            selected = selectedItem.value == 1,
            onClick = {
                navController.navigate(Screens.MainScreen.name) {
                    popUpTo(Screens.MainScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Local") },
            selected = selectedItem.value == 2,
            onClick = {
                navController.navigate(Screens.LocalDatabaseScreen.name) {
                    popUpTo(Screens.MainScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Drawer Item") },
            selected = selectedItem.value == 3,
            onClick = {
                navController.navigate(Screens.RemoteDatabaseScreen.name) {
                    popUpTo(Screens.MainScreen.name)
                    launchSingleTop = true
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavController,
    topBarState: MutableState<Boolean>,
    dropDownState: MutableState<Boolean>,
    showSelectionOnCard: MutableState<Boolean>,
    action: MutableState<CardActions>
) {
    AnimatedVisibility(visible = topBarState.value) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "OurShop", maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                DropDownMenu(navController, dropDownState, showSelectionOnCard,action)
            }

        )
    }
}


@Composable
fun DropDownMenu(
    navController: NavController,
    dropDownState: MutableState<Boolean>,
    showSelectionOnCard: MutableState<Boolean>,
    action: MutableState<CardActions>
) {
    var expanded by remember { mutableStateOf(false) }

    AnimatedVisibility(visible = dropDownState.value) {
        IconButton(
            onClick = {
                expanded = true
            }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()
        ) {
            DropdownMenuItem(
                text = { Text("Insert") }, onClick = {
                    expanded = false
                    if(navController.currentDestination?.route==Screens.LocalDatabaseScreen.name){
                        navController.navigate(Screens.InsertProducScreen.name)
                    }else{
                        navController.navigate(Screens.InsertTransactionScreen.name)
                    }
                }, leadingIcon = {
                    Icon(
                        Icons.Outlined.Add, contentDescription = null
                    )
                })
            Divider()
            DropdownMenuItem(text = { Text("Edit") },
                onClick = {
                    expanded = false
                    action.value = CardActions.Modify
                    showSelectionOnCard.value = !showSelectionOnCard.value
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit, contentDescription = null
                    )
                })
            Divider()
            DropdownMenuItem(text = { Text("Delete") },
                onClick = {
                    expanded = false
                    action.value = CardActions.Delete
                    showSelectionOnCard.value = !showSelectionOnCard.value
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Delete, contentDescription = null
                    )
                },
                trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
        }

    }

}


@Composable
fun LocalDatabaseCard(
    product: Product,
    stock: Stock,
    supplier: Supplier,
    actionsEnabled: MutableState<Boolean>,
    action: MutableState<CardActions>,
    navController: NavController,
    selectedProductId: MutableState<Int>

    //,onClick: () -> Unit
) {
    val database = AppDatabase.getDatabase(LocalContext.current).AppDao()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(IntrinsicSize.Min)
        //shape = MaterialTheme.shapes.large,
        //border = BorderStroke(2.dp,MaterialTheme.colorScheme.inversePrimary),
        //elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row {
            AnimatedVisibility(visible = actionsEnabled.value) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .then(modifierBasedOnAction(action))
                        .padding(5.dp)
                        .clickable(
                            // enabled = actionsEnabled.value,
                            onClick = {
                                if (action.value == CardActions.Delete && actionsEnabled.value) {
                                    database.deleteProduct(product)
                                } else {
                                    if (action.value == CardActions.Modify && actionsEnabled.value) {
                                        selectedProductId.value = product.ProductId
                                        navController.navigate(Screens.EditProductScreen.name)
                                    }
                                }
                            }
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    if(action.value==CardActions.Delete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete the item")
                    }else{
                        Icon(Icons.Filled.Edit, contentDescription = "Delete the item")
                    }
                }
                //Checkbox(checked = checked.value, onCheckedChange = { checked.value = !checked.value })
            }

            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Product", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductName)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Manufacturer", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductManufacturer)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Description", style = (MaterialTheme.typography.labelSmall))
                Text(text = product.ProductDescription)
                Spacer(modifier = Modifier.padding(5.dp))

            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Supplier", style = (MaterialTheme.typography.labelSmall))
                Text(text = supplier.Name)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Supplier Location", style = (MaterialTheme.typography.labelSmall))
                Text(text = supplier.Location)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Stock Quantity", style = (MaterialTheme.typography.labelSmall))
                Text(text = stock.Stock.toString())
            }
        }

    }
}


@Composable
fun RemoteDatabaseCard(
    transactions: Transactions
) {
    var customerData by remember { mutableStateOf(CustomerData()) }


    val q = Firebase.firestore.collection("Customers")
        .whereEqualTo("customerId",transactions.customerId)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                customerData = (document.toObject(CustomerData::class.java))
                Log.d("MyLog", "${document.data}")
                Log.d("MyLog", document.id)
            }
        }
    Log.d("Mylog","Exited")
    var expand = remember { false }
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(IntrinsicSize.Min)
            .clickable(onClick = {
                expand = !expand
            })
        //shape = MaterialTheme.shapes.large,
        //border = BorderStroke(2.dp,MaterialTheme.colorScheme.inversePrimary),
        //elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Products Sold", style = (MaterialTheme.typography.labelSmall))
                Text(text = transactions.quantity.toString())
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Product Id", style = (MaterialTheme.typography.labelSmall))
                Text(text = transactions.productId.toString())
            }
        Column(
            Modifier
                .weight(1f)
                .padding(5.dp)
        ) {
            Text(text = "Customer Name", style = (MaterialTheme.typography.labelSmall))
            Text(text = customerData.customerName)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Customer Lastname", style = (MaterialTheme.typography.labelSmall))
            Text(text = customerData.customerLastName)
            Spacer(modifier = Modifier.padding(5.dp))
        }

    }
}



@Preview
@Composable
fun test(){
    RemoteDatabaseCard(transactions = Transactions(0, 0, 123))
}

fun modifierBasedOnAction(action: MutableState<CardActions>) :Modifier{
    return if(action.value==CardActions.Delete){
        Modifier.background(Color.Red)
    }else{
        Modifier.background(Color.Yellow)
    }
}