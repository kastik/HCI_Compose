package com.kastik.hci.utils

import androidx.compose.foundation.background
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kastik.hci.ui.components.cards.CardActions

fun checkNumberInput(input : String) : Boolean {
    return !(input.isEmpty() || input.matches(Regex("^\\d{1,9}\$")))
}


fun modifierBasedOnAction(action: MutableState<CardActions>) : Modifier {
    return if(action.value== CardActions.Delete){
        Modifier.background(Color.Red)
    }else{
        Modifier.background(Color.Yellow)
    }
}