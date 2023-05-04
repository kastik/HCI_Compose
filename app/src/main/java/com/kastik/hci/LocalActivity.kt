package com.kastik.hci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.kastik.hci.database.AppDatabase
import com.kastik.hci.database.Product
import com.kastik.hci.ui.shit.DrawerSheet
import com.kastik.hci.ui.shit.MyTopBar


class LocalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalActivityUI()
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Product"
        ).allowMainThreadQueries().build()

        //
        //val productDao = db.ProductDao()
        //val products: List<Product> = ProductDao.getAll()
        db.ProductDao().insertAll(Product(1,"asdas","asdasd"),Product(2,"asdas","asdasd"),Product(3,"asdas","asdasd"))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LocalActivityUI() {
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
                    Text(text = "Local Activity stuff")
                }
            }
        }
    )

}