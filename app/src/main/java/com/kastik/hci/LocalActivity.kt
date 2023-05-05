package com.kastik.hci

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.kastik.hci.ui.shit.DrawerSheet
import com.kastik.hci.ui.shit.MyDrawer
import com.kastik.hci.ui.shit.MyTopBar
import com.kastik.hci.ui.theme.HCI_ComposeTheme
import kotlinx.coroutines.launch


class LocalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myDatabase = Room
            .databaseBuilder(this,AppDatabase::class.java,"Database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        setContent {
            LocalActivityUI()
        }


/*
        val db1 = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Product"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        val db2 = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Stock"
        ).allowMainThreadQueries() .fallbackToDestructiveMigration().build()

        val db3 = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Supplier"
        ) .fallbackToDestructiveMigration().allowMainThreadQueries().build()


        //
        //val productDao = db.ProductDao()
        //val products: List<Product> = ProductDao.getAll()
        //db.ProductDao().insertAll(Product(1,"asdas","asdasd"),Product(2,"asdas","asdasd"),Product(3,"asdas","asdasd"))

 */
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun LocalActivityUI(myDatabase: AppDatabase) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerSheet() },
        content = {
            Scaffold(
                topBar = { MyTopBar(scope = scope, drawerState = drawerState) }
            ){ paddingValues ->
                Box(modifier =Modifier.padding(top = paddingValues.calculateTopPadding())){
                    Text(text = "Some RandomText")
                    LazyRow(){
                       // items{ myDatabase.myDao().getAllProducts() }

                    }

                }
            }
        }
    )

}