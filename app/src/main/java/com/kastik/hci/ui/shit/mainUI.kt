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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawer(drawerState:DrawerState,scope:CoroutineScope) {

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerSheet() },
        content = {
            Row {
                MyTopBar(scope = scope, drawerState = drawerState)
            }
            Column {
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerSheet(){
    ModalDrawerSheet {
        Modifier.size(50.dp)
        val context = LocalContext.current
        Text("Drawer title", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Main") },
            selected = false,
            onClick = { context.startActivity(Intent(context, MainActivity::class.java)) }
        )
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Local") },
            selected = false,
            onClick = {
                context.startActivity(Intent(context, LocalActivity::class.java))
            }
        )
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Drawer Item") },
            selected = false,
            onClick = { context.startActivity(Intent(context, RemoteActivity::class.java)) }
        )
    }
}