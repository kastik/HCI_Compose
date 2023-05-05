package com.kastik.hci.ui.shit

import android.content.Intent
import android.view.View.OnLongClickListener
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.hci.LocalActivity
import com.kastik.hci.MainActivity
import com.kastik.hci.RemoteActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.kastik.hci.database.Product
import com.kastik.hci.database.createSampleData
import com.kastik.hci.ui.theme.HCI_ComposeTheme


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