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
import com.kastik.hci.ui.shit.DrawerSheet
import com.kastik.hci.ui.shit.MyTopBar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityUI()
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
                    Text(text = "5")
                    Text(text = "6")
                    Text(text = "paok")
                    Text(text = "aris")
                    Text(text = "kaka")
                    Text(text = "pipes")
                    Text(text = "piperies")
                    Text(text = "asdasd")
                    Text(text = "asdasd")
                    Text(text = "bcbcvb")
                    Text(text = "ff23fwe")
                    Text(text = "asdascvbcvbcvd")
                    Text(text = "asdascvbcvbcbcdfdsfd")
                    Text(text = "asdasxcvxcvxd")
                    Text(text = "asdaxcvxcvsd")
                    Text(text = "asdaxcvxcvsd")
                    Text(text = "asdasxcvxcvd")
                    Text(text = "asdaxcvxcvxcsd")
                    Text(text = "asda234234234sd")
                    Text(text = "asdasd")
                }
            }
        }
    )

}