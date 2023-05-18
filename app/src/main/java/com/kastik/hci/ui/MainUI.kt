package com.kastik.hci.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.data.AppDatabase
import com.kastik.hci.ui.components.cards.CardActions
import com.kastik.hci.ui.screens.AvailableScreens
import com.kastik.hci.ui.screens.HomeScreen
import com.kastik.hci.ui.screens.create.CreateCustomerScreen
import com.kastik.hci.ui.screens.create.CreateProductScreen
import com.kastik.hci.ui.screens.create.CreateSupplierScreen
import com.kastik.hci.ui.screens.create.CreateTransactionScreen
import com.kastik.hci.ui.screens.edit.EditProductScreen
import com.kastik.hci.ui.screens.edit.EditSupplierScreen
import com.kastik.hci.ui.screens.view.CustomerScreen
import com.kastik.hci.ui.screens.view.ProductScreen
import com.kastik.hci.ui.screens.view.SupplierScreen
import com.kastik.hci.ui.screens.view.TransactionScreen
import com.kastik.hci.ui.theme.HCI_ComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch




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
    val snackbarHostState = remember { SnackbarHostState() }

    val selectedSupplierId = remember { mutableStateOf(0) }
    val selectedProductId = remember { mutableStateOf(0) }
    val selectedTransactionId = remember { mutableStateOf("") }
    val selectedCustomerId =  remember { mutableStateOf("") }

    val dao = AppDatabase.getDatabase(LocalContext.current).AppDao()
    val firestore = Firebase.firestore
    val customerDb = firestore.collection("Customers")
    val transactionDb = firestore.collection("Transactions")

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
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        },
                    ){ paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = AvailableScreens.HomeScreen.name,
                            Modifier.padding(paddingValues)
                        ) {
                            composable(AvailableScreens.HomeScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 1
                                dropDownState.value = false
                                drawerGestureEnabled.value = true
                                showSelectionOnCard.value = false
                                HomeScreen()
                            }
                            composable(AvailableScreens.SupplierScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 2
                                dropDownState.value = true
                                drawerGestureEnabled.value = true
                                showSelectionOnCard.value = false
                                SupplierScreen(dao,showSelectionOnCard,action,selectedSupplierId,navController,snackbarHostState)
                            }
                            composable(AvailableScreens.ProductScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 3
                                dropDownState.value = true
                                drawerGestureEnabled.value = true
                                showSelectionOnCard.value = false
                                ProductScreen(dao,showSelectionOnCard,action,selectedProductId,navController,snackbarHostState)
                            }
                            composable(AvailableScreens.CustomerScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 4
                                dropDownState.value = true
                                drawerGestureEnabled.value = true
                                showSelectionOnCard.value = false
                                CustomerScreen(customerDb,showSelectionOnCard,action,selectedCustomerId,navController,snackbarHostState)
                            }
                            composable(AvailableScreens.TransactionScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 5
                                dropDownState.value = true
                                drawerGestureEnabled.value = true
                                showSelectionOnCard.value = false
                                TransactionScreen(showSelectionOnCard,action,selectedTransactionId,navController,snackbarHostState)
                            }
                            composable(AvailableScreens.CreateSupplierScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                showSelectionOnCard.value = false
                                CreateSupplierScreen(dao,snackbarHostState,navController)
                            }
                            composable(AvailableScreens.CreateProductScreen.name){ //TODO
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                showSelectionOnCard.value = false
                                CreateProductScreen(dao,snackbarHostState,navController)

                            }
                            composable(AvailableScreens.CreateCustomerScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                showSelectionOnCard.value = false
                                CreateCustomerScreen(customerDb,snackbarHostState,navController)

                            }
                            composable(AvailableScreens.CreateTransactionScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                showSelectionOnCard.value = false
                                CreateTransactionScreen(transactionDb,customerDb,snackbarHostState,navController,dao)

                            }
                            composable(AvailableScreens.EditSupplierScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                showSelectionOnCard.value = false
                                EditSupplierScreen(selectedSupplierId.value,dao,snackbarHostState)

                            }
                            composable(AvailableScreens.EditProductScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                showSelectionOnCard.value = false
                                EditProductScreen(selectedProductId.value,dao,snackbarHostState)

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
            label = { Text(text = "Home") },
            selected = selectedItem.value == 1,
            onClick = {
                navController.navigate(AvailableScreens.HomeScreen.name) {
                    popUpTo(AvailableScreens.HomeScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Supplier") },
            selected = selectedItem.value == 2,
            onClick = {
                navController.navigate(AvailableScreens.SupplierScreen.name) {
                    popUpTo(AvailableScreens.HomeScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Products") },
            selected = selectedItem.value == 3,
            onClick = {
                navController.navigate(AvailableScreens.ProductScreen.name) {
                    popUpTo(AvailableScreens.HomeScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Customers") },
            selected = selectedItem.value == 4,
            onClick = {
                navController.navigate(AvailableScreens.CustomerScreen.name) {
                    popUpTo(AvailableScreens.HomeScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Transactions") },
            selected = selectedItem.value == 5,
            onClick = {
                navController.navigate(AvailableScreens.TransactionScreen.name) {
                    popUpTo(AvailableScreens.HomeScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
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
                    when(navController.currentDestination?.route){
                        AvailableScreens.ProductScreen.name -> navController.navigate(AvailableScreens.CreateProductScreen.name)
                        AvailableScreens.SupplierScreen.name -> navController.navigate(AvailableScreens.CreateSupplierScreen.name)
                        AvailableScreens.CustomerScreen.name -> navController.navigate(AvailableScreens.CreateCustomerScreen.name)
                        AvailableScreens.TransactionScreen.name -> navController.navigate(AvailableScreens.CreateTransactionScreen.name)
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



