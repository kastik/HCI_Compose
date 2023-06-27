package com.kastik.hci.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.kastik.hci.data.AppDao
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun HomeScreen(dao: AppDao,firestoreDb: FirebaseFirestore) {
    val scope = rememberCoroutineScope()
    val count = remember { mutableStateOf("") }
    firestoreDb.collection("Customers").count().get(AggregateSource.SERVER).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            count.value = task.result.count.toString() }
        else { count.value = "Error" } }
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
                text = "Products in warehouse ${dao.getProductCount().collectAsState(emptyFlow<Int>()).value}"
            )
        }
        Row(Modifier.padding(10.dp)) {

            Text(modifier = Modifier
                .padding(10.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Our Suppliers are ${dao.getSupplierCount().collectAsState(emptyFlow<Int>()).value}")
        }
        Row(Modifier.padding(10.dp)) {
            Text(modifier = Modifier
                .padding(10.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Total Registered Customers ${count.value}" )
        }
    }

}