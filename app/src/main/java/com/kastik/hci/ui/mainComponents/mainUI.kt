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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
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
import com.kastik.hci.database.Product
import com.kastik.hci.database.Stock
import com.kastik.hci.database.Supplier
import com.kastik.hci.ui.theme.HCI_ComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random


enum class Screens {
    Main,
    Local,
    Remote,
    InsertProduct,
    InsertSupplier
}


//@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview
fun MainUI() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    HCI_ComposeTheme {
        Surface {
            ModalNavigationDrawer(drawerState = drawerState,
                drawerContent = { DrawerSheet(navController) },
                content = {
                    Scaffold(topBar = {
                        MyTopBar(
                            scope = scope, drawerState = drawerState, navController = navController,topBarState,
                        )
                    }) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Screens.Main.name,
                            Modifier.padding(paddingValues)
                        ) {
                            composable(Screens.Main.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                MainActivityUI()
                            }
                            composable(Screens.Local.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                LocalActivityUI()
                            }
                            composable(Screens.Remote.name) {
                                LaunchedEffect(drawerState) {
                                    drawerState.close()
                                }
                                topBarState.value = true
                                RemoteActivityUI()
                            }
                            composable(Screens.InsertProduct.name) {
                                topBarState.value = false
                                InsertProductUI()
                            }
                        }
                    }
                })

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun InsertProductUI() {
    var productName by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var supplierName by remember { mutableStateOf("") }
    var supplierLocation by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val database = AppDatabase.getDatabase(LocalContext.current).AppDao()
    val supplierNames = database.getSupplierNames().collectAsState(initial = emptyList())
    var selectedText by remember { mutableStateOf("Insert A Supplier First") }


    HCI_ComposeTheme() {
        Surface() {
            LazyColumn() {
                item() {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                //.fillMaxHeight()
                                //.verticalScroll(rememberScrollState())

                        ) {

                            Text(
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .padding(10.dp),
                                style = MaterialTheme.typography.headlineSmall,
                                text = "Insert Supplier"
                            )
                            OutlinedTextField(
                                shape= RoundedCornerShape(15.dp),
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                value = supplierName,
                                onValueChange = { supplierName = it },
                                label = { Text("Supplier Name")}
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            OutlinedTextField(
                                shape= RoundedCornerShape(15.dp),
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                value = supplierLocation,
                                onValueChange = { supplierLocation = it },
                                label = { Text("Supplier Location") }
                            )
                            FilledTonalButton(
                                modifier= Modifier
                                    .align(Alignment.End)
                                    .padding(10.dp),
                                onClick = {
                                    database.insertSupplier(Supplier(
                                        Random.nextInt(),
                                        supplierName,supplierLocation
                                    ))
                                    supplierName = ""
                                    supplierLocation = ""
                                }) { Text("Insert") }

                            Text(
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .padding(10.dp),
                                style = MaterialTheme.typography.headlineSmall,
                                text = "Create Product"
                            )


                            OutlinedTextField(
                                shape= RoundedCornerShape(15.dp),
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                value = productName,
                                onValueChange = { productName = it },
                                label = { Text("Product") }
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            OutlinedTextField(
                                shape= RoundedCornerShape(15.dp),
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                value = manufacturer,
                                onValueChange = { manufacturer = it },
                                label = { Text("Manufacturer") }
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            OutlinedTextField(
                                shape= RoundedCornerShape(15.dp),
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                value = price,
                                onValueChange = { price = it },
                                label = { Text("Price") }
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            OutlinedTextField(
                                shape= RoundedCornerShape(15.dp),
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Product Description") }
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            ExposedDropdownMenuBox(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                expanded = expanded,
                                onExpandedChange = {
                                    expanded = !expanded
                                }
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .menuAnchor()
                                        //.align(Alignment.End)
                                        .padding(10.dp),
                                    value = selectedText,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded
                                        )
                                    }
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = {
                                        expanded = false
                                    }
                                ) {
                                    supplierNames.value.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item) },
                                            onClick = {
                                                selectedText = item
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.padding(10.dp))
                            OutlinedTextField(

                                shape= RoundedCornerShape(15.dp),
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                value = stock,
                                onValueChange = { stock = it },
                                label = { Text("Quantity") }
                            )
                            FilledTonalButton(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(10.dp),
                                onClick = {
                                    database.insertStock(Stock(12,12))
                                    database.insertProduct(Product(
                                        Random.nextInt(),productName, manufacturer,price.toInt(),description,697620083,12
                                    ))
                                }) { Text("Insert") }
                        }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerSheet(navController: NavController) {
    var selected by remember { mutableStateOf(1) }
    ModalDrawerSheet {
        Modifier.size(50.dp)
        Text("Drawer title", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(label = { Text(text = "Main") }, selected = selected == 1, onClick = {
            navController.navigate(Screens.Main.name)
            selected = 1
        })
        Divider()
        NavigationDrawerItem(label = { Text(text = "Local") }, selected = selected == 2, onClick = {
            navController.navigate(Screens.Local.name)
            selected = 2
        })
        Divider()
        NavigationDrawerItem(label = { Text(text = "Drawer Item") },
            selected = selected == 3,
            onClick = {
                navController.navigate(Screens.Remote.name)
                selected = 3
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(scope: CoroutineScope, drawerState: DrawerState, navController: NavController,topBarState: MutableState<Boolean>) {
    AnimatedVisibility(visible = topBarState.value) {
        CenterAlignedTopAppBar(title = {
            Text(
                "Centered TopAppBar", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu, contentDescription = "Localized description"
                )
            }
        }, actions = {
            DropDownMenu(navController)
        })
    }

}

@Composable
//TODO
fun BottomNav() {

}


@Composable
fun DropDownMenu(navController: NavController) {
    val db = AppDatabase.getDatabase(LocalContext.current)
    var expanded by remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded = true }) {
        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.wrapContentSize()) {
        DropdownMenuItem(text = { Text("Insert") }, onClick = {
            expanded = false
            //db.AppDao().insertProduct(Product(Random.nextInt(), "asdasdad", "asdasd", 1, "kaka"))
            navController.navigate(Screens.InsertProduct.name)
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

@Composable
fun MainActivityUI() {
    Text(text = "Hi")
}


@Composable
fun LocalActivityUI() {
    val lazyListState = rememberLazyListState()
    val db = AppDatabase.getDatabase(LocalContext.current)
    val mdata = db.AppDao().getAllProducts().collectAsState(initial = emptyList())
    LazyColumn(Modifier.fillMaxSize()) {
        itemsIndexed(mdata.value) { index, item ->
            MyCard(product = item)
        }
    }
}


@Composable
fun RemoteActivityUI() {
    val db = Firebase.firestore
    Text(text = "Remote Activity stuff")

}


@Composable
fun MyCard(
    product: Product

    //,onClick: () -> Unit
) {
    var expand = remember { false }
    var text = remember { "kostas" }
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
                if (!expand) {
                    text = "kastik"
                } else {
                    text = "kostas"
                }
                Text(text = text)
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