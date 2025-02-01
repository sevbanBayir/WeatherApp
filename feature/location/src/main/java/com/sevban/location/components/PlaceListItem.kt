package com.sevban.location.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.sevban.model.Place
import com.sevban.model.fullText

@Composable
fun PlaceListItem(
    place: Place,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = { onPlaceClick(place) },
        shape = RectangleShape
    ) {
        Text(
            text = place.fullText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}