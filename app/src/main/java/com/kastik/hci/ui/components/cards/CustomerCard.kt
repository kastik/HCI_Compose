package com.kastik.hci.ui.components.cards

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kastik.hci.data.CustomerData
import com.kastik.hci.utils.modifierBasedOnAction


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun CustomerCardPreview(){
    CustomerCard(
        CustomerData(),
        mutableStateOf(false),
        mutableStateOf(CardActions.Empty),
        rememberNavController(),
        SnackbarHostState()
    )

}



@Composable
fun CustomerCard(
    customer: CustomerData,
    actionsEnabled: MutableState<Boolean>,
    action: MutableState<CardActions>,
    navController: NavController,
    snackbarHostState: SnackbarHostState
){
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(IntrinsicSize.Min),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.inversePrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row {
            AnimatedVisibility(visible = actionsEnabled.value) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .then(modifierBasedOnAction(action))
                        .padding(5.dp)
                        .clickable(
                            // enabled = actionsEnabled.value,
                            onClick = {

                                if (action.value == CardActions.Delete && actionsEnabled.value) {
                                    //      if(0<database.deleteProduct(product)){ scope.launch { snackbarHostState.showSnackbar("Success!") } }
                                    //     else{scope.launch { snackbarHostState.showSnackbar("Something Happened Try Again")}}

                                    //    } else{
                                    if (action.value == CardActions.Modify && actionsEnabled.value) {
                                        //   selectedProductId.value = product.ProductId
                                        //  navController.navigate(AvailableScreens.EditProductScreen.name)
                                    }
                                }
                            }
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    if(action.value== CardActions.Delete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete the item")
                    }else{
                        Icon(Icons.Filled.Edit, contentDescription = "Delete the item")
                    }
                }
                //Checkbox(checked = checked.value, onCheckedChange = { checked.value = !checked.value })
            }

            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Customer Name", style = (MaterialTheme.typography.labelSmall))
                Text(text = customer.customerName)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Customer Lastname", style = (MaterialTheme.typography.labelSmall))
                Text(text = customer.customerLastName)
                Spacer(modifier = Modifier.padding(5.dp))
            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {

            }
        }

    }
}
