package com.kastik.hci.ui.mainComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kastik.hci.database.Product
import com.kastik.hci.ui.theme.HCI_ComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainUI() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val topBarState = remember { (mutableStateOf(true)) }
    val selectedItem = remember { mutableStateOf(1) }
    val dropDownState = remember { mutableStateOf(false) }
    HCI_ComposeTheme {
        Surface {
            ModalNavigationDrawer(drawerState = drawerState,
                drawerContent = { DrawerSheet(navController,selectedItem) },
                content = {
                    Scaffold(
                        topBar = {
                        MyTopBar(scope = scope, drawerState = drawerState, navController = navController,topBarState= topBarState,dropDownState)
                        }
                    ){ paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Screens.MainScreen.name,
                            Modifier.padding(paddingValues)
                        ){
                            composable(Screens.MainScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 1
                                dropDownState.value = false
                                MainScreen()
                            }
                            composable(Screens.LocalDatabaseScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 2
                                dropDownState.value = true
                                LocalDatabaseScreen()
                            }
                            composable(Screens.RemoteDatabaseScreen.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                selectedItem.value = 3
                                dropDownState.value = true
                                RemoteDatabaseScreen()
                            }
                            composable(Screens.InsertProducScreen.name) {
                                topBarState.value = false
                                InsertProducScreen()
                            }
                        }
                    }
                })
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerSheet(navController: NavController, selectedItem: MutableState<Int>){
    ModalDrawerSheet{
        Modifier.size(50.dp)
        Text("Drawer title", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(label = { Text(text = "Main") }, selected = selectedItem.value == 1, onClick = {
            navController.navigate(Screens.MainScreen.name) {
                popUpTo(Screens.MainScreen.name)
                launchSingleTop = true
            }
        })
        Divider()
        NavigationDrawerItem(label = { Text(text = "Local") }, selected = selectedItem.value == 2, onClick = {
            navController.navigate(Screens.LocalDatabaseScreen.name){
                popUpTo(Screens.MainScreen.name)
                launchSingleTop = true
            }
        })
        Divider()
        NavigationDrawerItem(label = { Text(text = "Drawer Item") }, selected = selectedItem.value == 3, onClick = {
                navController.navigate(Screens.RemoteDatabaseScreen.name){
                    popUpTo(Screens.MainScreen.name)
                    launchSingleTop = true
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(scope: CoroutineScope, drawerState: DrawerState, navController: NavController,topBarState: MutableState<Boolean>,dropDownState: MutableState<Boolean>) {
    AnimatedVisibility(visible = topBarState.value){
        CenterAlignedTopAppBar(
            title = {
                Text("Centered TopAppBar", maxLines = 1, overflow = TextOverflow.Ellipsis
            )},
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Filled.Menu, contentDescription = "Localized description")
                } },
            actions = {
            DropDownMenu(navController,dropDownState)
            })
    }
}


@Composable
fun DropDownMenu(navController: NavController,dropDownState: MutableState<Boolean>) {
    var expanded by remember { mutableStateOf(false) }

    AnimatedVisibility(visible = dropDownState.value) {
        IconButton(
            onClick = {
                expanded = true
            }){
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()) {
            DropdownMenuItem(
                text = { Text("Insert") }, onClick = {
                    expanded = false
                    //db.AppDao().insertProduct(Product(Random.nextInt(), "asdasdad", "asdasd", 1, "kaka"))
                    navController.navigate(Screens.InsertProducScreen.name)
                }, leadingIcon = {
                    Icon(
                        Icons.Outlined.Add, contentDescription = null
                    )
                })
            Divider()
            DropdownMenuItem(text = { Text("Edit") },
                onClick = {
                    expanded = false
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
fun MyCard(
    product: Product

    //,onClick: () -> Unit
    ){
    var expand = remember { false }
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(bottom = 2.dp, top = 2.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(onClick = {
                expand = !expand
            })
        //shape = MaterialTheme.shapes.large,
        //border = BorderStroke(2.dp,MaterialTheme.colorScheme.inversePrimary),
        //elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row {
            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = product.productName)
                Text(text = product.productName)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = product.productManufacturer.toString())
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = product.productDescription.toString())
            }

            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "supplier.firstName.toString()")
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "supplier.Location.toString()")
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "supplier.lastName.toString()")
            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Image(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = null,
                    Modifier
                        .weight(2f)
                        .fillMaxSize()
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text = "Line2Col3", Modifier.weight(1f))
                //Spacer(modifier = Modifier.padding(5.dp))
            }
        }

    }
}