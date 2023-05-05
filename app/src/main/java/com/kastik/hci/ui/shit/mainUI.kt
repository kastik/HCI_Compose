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
import androidx.compose.material3.DrawerValue
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.kastik.hci.LocalActivity
import com.kastik.hci.MainActivity
import com.kastik.hci.RemoteActivity
import com.kastik.hci.ui.theme.HCI_ComposeTheme
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
        }
    )
}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawer(drawerState:DrawerState,scope:CoroutineScope,navController: NavController) {

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerSheet(navController) },
        content = {
            Row {
                MyTopBar(scope = scope, drawerState = drawerState)
            }
            Column {
            }
        }
    )
}

 */

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
