package com.kastik.hci.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kastik.hci.data.AppDao
import com.kastik.hci.data.AppDatabase

@Preview
@Composable
fun HomePreview(){
    HomeScreen(AppDatabase.getDatabase(LocalContext.current).AppDao(), Firebase.firestore)
}



@Composable
fun HomeScreen(dao: AppDao,firestoreDb: FirebaseFirestore) {
    val scope = rememberCoroutineScope()
    Column() {

        Row(Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                style = MaterialTheme.typography.headlineMedium,
                text = "Statistics"
            )
        }

        Row(Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Products in warehouse"
            )
        }
        Row(Modifier.padding(10.dp)) {

            Text(modifier = Modifier
                .padding(10.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Suppliers ")
        }
        Row(Modifier.padding(10.dp)) {
            Text(modifier = Modifier
                .padding(10.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Total Customers Sold")

        }
    }

}