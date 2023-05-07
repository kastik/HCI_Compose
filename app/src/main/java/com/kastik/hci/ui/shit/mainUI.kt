package com.kastik.hci.ui.shit

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.room.Room
import com.kastik.hci.database.AppDatabase
import com.kastik.hci.database.Product
import com.kastik.hci.ui.theme.HCI_ComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random


enum class Screens{
    Main,
    Local,
    Remote
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(scope:CoroutineScope,drawerState: DrawerState){
    var expanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Centered TopAppBar",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions =   {
            Box() {
                IconButton(onClick = { expanded=true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded=false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Insert") },
                        onClick = { /* Handle edit! */ },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Add,
                                contentDescription = null
                            )
                        })
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = { /* Handle settings! */ },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = null
                            )
                        })
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = { /* Handle send feedback! */ },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        },
                        trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
                }
            }


        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerSheet(navController: NavController){
    var selected by remember {  mutableStateOf(1) }
    ModalDrawerSheet {
        Modifier.size(50.dp)
        Text("Drawer title", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Main") },
            selected = selected==1,
            onClick = {
                navController.navigate(Screens.Main.name)
                selected=1
            }
        )
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Local") },
            selected = selected==2,
            onClick = {
                navController.navigate(Screens.Local.name)
                selected=2
            }
        )
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Drawer Item") },
            selected = selected==3,
            onClick = {
                navController.navigate(Screens.Remote.name)
                selected=3
                }
        )
    }
}


    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainUI() {
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val navController = rememberNavController()
        val db = Room
            .databaseBuilder(LocalContext.current, AppDatabase::class.java, "Database.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    HCI_ComposeTheme{
        Surface{
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = { DrawerSheet(navController) },
                content = {
                    Scaffold(
                        topBar = {MyTopBar(scope = scope, drawerState = drawerState)},

                    ){ paddingValues ->
                        NavHost(navController =navController ,startDestination=Screens.Main.name ,Modifier.padding(paddingValues)){
                            composable(Screens.Main.name){
                                scope.launch { drawerState.close() }
                                MainActivityUI()
                            }
                            composable(Screens.Local.name){
                                scope.launch { drawerState.close() }
                                LocalActivityUI()
                            }
                            composable(Screens.Remote.name){
                                scope.launch { drawerState.close() }
                                RemoteActivityUI()
                            }
                        }
                    }
                }
            )

        }

    }
}

@Composable
fun MyCard(product: Product
           //, onClick: () -> Unit
) {
            Card(
                shape= MaterialTheme.shapes.large,
                modifier= Modifier
                    .padding(bottom = 2.dp, top = 2.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                //shape = MaterialTheme.shapes.large,
                //border = BorderStroke(2.dp,MaterialTheme.colorScheme.inversePrimary),
                //elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Row{
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(5.dp)) {
                        Text(text = product.productName)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = product.productManufacturer.toString())
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = product.productDescription.toString())
                    }

                    Column(
                        Modifier
                            .weight(1f)
                            .padding(5.dp)) {
                        Text(text = "supplier.firstName.toString()")
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "supplier.Location.toString()")
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "supplier.lastName.toString()")
                    }
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(5.dp)) {
                        Image(imageVector = Icons.Filled.AccountBox,contentDescription = null,
                            Modifier
                                .weight(2f)
                                .fillMaxSize())
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(text = "Line2Col3",Modifier.weight(1f))
                        //Spacer(modifier = Modifier.padding(5.dp))
                    }
                }

            }
}





@Composable
//TODO
fun BottomNav(){
    HCI_ComposeTheme{
        Surface{
            //MyCard(data =) {

            }

        }

    }

@Composable
fun MainActivityUI() {




}



@Composable
fun LocalActivityUI() {

    val lazyListState = rememberLazyListState()


    //val datalist = myDatabase.AppDao().getAllProducts()
    val datalist = listOf<Product>(
        Product(Random.nextInt(),java.util.UUID.randomUUID().toString(),java.util.UUID.randomUUID().toString(), Random.nextInt(),java.util.UUID.randomUUID().toString()),
        Product(Random.nextInt(),java.util.UUID.randomUUID().toString(),java.util.UUID.randomUUID().toString(), Random.nextInt(),java.util.UUID.randomUUID().toString()),
        Product(Random.nextInt(),java.util.UUID.randomUUID().toString(),java.util.UUID.randomUUID().toString(), Random.nextInt(),java.util.UUID.randomUUID().toString()),
        Product(Random.nextInt(),java.util.UUID.randomUUID().toString(),java.util.UUID.randomUUID().toString(), Random.nextInt(),java.util.UUID.randomUUID().toString())
    )


/*
    val datalist = AppDatabase.getDatabase(LocalContext.current).AppDao().getAllProducts()

 */
    LazyColumn (Modifier.fillMaxSize()){
        items(datalist){data ->
            MyCard(product = data)
        }
        }
    }


@Composable
fun RemoteActivityUI() {

    Text(text = "Remote Activity stuff")

}
