package com.kastik.hci.ui.components

import android.util.Log
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kastik.hci.ui.screens.AvailableScreens
import com.kastik.hci.ui.screens.HomeScreen
import com.kastik.hci.ui.screens.InsertLocal
import com.kastik.hci.ui.screens.InsertRemoteScreen
import com.kastik.hci.ui.screens.LocalDataViewScreen
import com.kastik.hci.ui.screens.RemoteDataViewScreen
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
                            startDestination = AvailableScreens.MainScreen.name,
                            Modifier.padding(paddingValues)
                        ) {
                            composable(AvailableScreens.MainScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 1
                                dropDownState.value = false
                                drawerGestureEnabled.value = true
                                HomeScreen()
                            }
                            composable(AvailableScreens.LocalDatabaseScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 2
                                dropDownState.value = true
                                drawerGestureEnabled.value = true
                                LocalDataViewScreen(showSelectionOnCard,action,selectedProductId,navController)
                            }
                            composable(AvailableScreens.RemoteDatabaseScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 3
                                dropDownState.value = true
                                drawerGestureEnabled.value = true
                                RemoteDataViewScreen()
                            }
                            composable(AvailableScreens.InsertProducScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                Log.d("MyLog","Insert")
                                InsertLocal(productId = null)

                            }
                            composable(AvailableScreens.EditProductScreen.name){ //TODO
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                Log.d("MyLog","Edit with id ${it.arguments?.getInt("productId")}")
                                InsertLocal(productId = selectedProductId.value)

                            }
                            composable(AvailableScreens.InsertTransactionScreen.name) {
                                topBarState.value = false
                                drawerGestureEnabled.value = false
                                InsertRemoteScreen()

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
                navController.navigate(AvailableScreens.MainScreen.name) {
                    popUpTo(AvailableScreens.MainScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Local") },
            selected = selectedItem.value == 2,
            onClick = {
                navController.navigate(AvailableScreens.LocalDatabaseScreen.name) {
                    popUpTo(AvailableScreens.MainScreen.name)
                    launchSingleTop = true
                }
            })
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Drawer Item") },
            selected = selectedItem.value == 3,
            onClick = {
                navController.navigate(AvailableScreens.RemoteDatabaseScreen.name) {
                    popUpTo(AvailableScreens.MainScreen.name)
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
                    if(navController.currentDestination?.route== AvailableScreens.LocalDatabaseScreen.name){
                        navController.navigate(AvailableScreens.InsertProducScreen.name)
                    }else{
                        navController.navigate(AvailableScreens.InsertTransactionScreen.name)
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



