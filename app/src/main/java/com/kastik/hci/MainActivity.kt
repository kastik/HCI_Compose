package com.kastik.hci

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.hci.database.createSampleData
import com.kastik.hci.ui.shit.DrawerSheet
import com.kastik.hci.ui.shit.MyTopBar
import com.kastik.hci.ui.theme.HCI_ComposeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HCI_ComposeTheme() { Surface() { MainActivityUI() } } }

        val settings = getSharedPreferences("Shared_Preferences", 0)

        if(settings.getBoolean("first_run", true)) {
            createSampleData(this)
            Log.d("MyLog","First Run")
            settings.edit().putBoolean("first_run", false).apply()
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainActivityUI() {
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
                    Text(text = "1st")
                    Text(text = "2nd")
                    Text(text = "3d")
                    Text(text = "4th")
                }
            }
        }
    )

}