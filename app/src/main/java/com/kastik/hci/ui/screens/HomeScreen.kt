package com.kastik.hci.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.kastik.hci.data.AppDao


@Composable
fun HomeScreen(dao: AppDao,firestoreDb: FirebaseFirestore) {
    val scope = rememberCoroutineScope()
    Column {

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
                style = MaterialTheme.typography.bodyMedium,
                text = "Products in warehouse"
            )
        }
        Row(Modifier.padding(10.dp)) {

            Text(modifier = Modifier
                .padding(10.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Suppliers ")
        }
        Row(Modifier.padding(10.dp)) {
            Text(modifier = Modifier
                .padding(10.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Total Customers Sold")

        }
    }

}