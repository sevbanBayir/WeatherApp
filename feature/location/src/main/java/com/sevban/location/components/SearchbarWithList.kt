package com.sevban.location.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevban.location.R
import com.sevban.model.Place

@Composable
fun SearchbarWithList(
    searchQuery: String,
    placeList: List<Place>,
    onSearchQueryChanged: (String) -> Unit,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text(text = stringResource(R.string.map_tf_search)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 0.dp
                ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        AnimatedVisibility(
            visible = placeList.isNotEmpty(),
            enter = slideInVertically() + fadeIn(),
            exit = shrinkVertically()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .animateContentSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(placeList) { place ->
                    PlaceListItem(
                        place = place,
                        onPlaceClick = onPlaceClick,
                        modifier = Modifier
                            .animateItem()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
