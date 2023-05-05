package com.kastik.hci.ui.shit

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kastik.hci.LocalActivity
import com.kastik.hci.MainActivity
import com.kastik.hci.RemoteActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



enum class Screens(){
    Main,
    Local,
    Remote
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(scope:CoroutineScope,drawerState: DrawerState){
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Centered TopAppBar",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            Column() {
                Switch(
                    checked = false,
                    //modifier = modifier,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            // The switch is checked.
                        } else {
                            // The switch isn't checked.
                        }
                    }
                )

            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerSheet(navController:NavController){
    ModalDrawerSheet {
        Modifier.size(50.dp)
        val context = LocalContext.current
        Text("Drawer title", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Main") },
            selected = false,
            onClick = { navController.navigate(Screens.Main.name)
            }
        )
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Local") },
            selected = false,
            onClick = {
                navController.navigate(Screens.Local.name)
            }
        )
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Drawer Item") },
            selected = false,
            onClick = {
                navController.navigate(Screens.Remote.name)
                }
        )
    }
}


    @OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainUI() {
    //val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()

    HCI_ComposeTheme() {
        Surface() {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = { DrawerSheet(navController) },
                content = {
                    Scaffold(
                        topBar = { MyTopBar(scope = scope, drawerState = drawerState) }
                    ){ paddingValues ->
                        NavHost(navController =navController ,startDestination=Screens.Main.name ,Modifier.padding(paddingValues)){
                            composable(Screens.Main.name){
                                MainActivityUI()
                            }
                            composable(Screens.Local.name){
                                LocalActivityUI()
                            }
                            composable(Screens.Remote.name){
                                RemoteActivityUI()
                            }
                        }
                    }
                }
            )

        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyCard(product: Product,onClick: () -> Unit ) {
            Card(
                shape= MaterialTheme.shapes.large,
                modifier= Modifier
                    .clickable(onClick = onClick)
                    .padding(bottom = 2.dp, top = 2.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                //shape = MaterialTheme.shapes.large,
                //border = BorderStroke(2.dp,MaterialTheme.colorScheme.inversePrimary),
                //elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Row() {
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(5.dp)) {
                        Text(text = "TestLine1")
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "TestLine2")
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "TestLine2")
                    }

                    Column(
                        Modifier
                            .weight(1f)
                            .padding(5.dp)) {
                        Text(text = "Line1Col2")
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "Line2Col2")
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "TestLine2")
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPreview(product: Product) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerSheet() },
        content = {
            Scaffold(
                topBar = { MyTopBar(scope = scope, drawerState = drawerState) }
            ){ paddingValues ->
                Column(modifier =Modifier.padding(top = paddingValues.calculateTopPadding())
                ){
                    LazyColumn(){}
                }
            }
        }
    )

}

@Composable
fun CardPreview(){
    HCI_ComposeTheme() {
        Surface() {
            Column() {
                Spacer(modifier =Modifier.padding(5.dp))
               // MyCard()
                Spacer(modifier =Modifier.padding(5.dp))
               // MyCard()
                Spacer(modifier =Modifier.padding(5.dp))
               // MyCard()
                Spacer(modifier =Modifier.padding(5.dp))
            }

        }

    }
}

@Composable
@Preview
fun bottomNav(){
    HCI_ComposeTheme() {
        Surface() {
            MyCard(data = "asdasd") {

            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityUI() {

    Text(text = "Main Activity stuff")

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalActivityUI() {

    Text(text = "Local Activity stuff")

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteActivityUI() {

    Text(text = "Remote Activity stuff")

}
