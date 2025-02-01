package com.sevban.location.helper

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.sevban.location.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PlaceAutocompleteService @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val apiKey = BuildConfig.MAPS_API_KEY

    fun getAutocomplete(query: String) = callbackFlow {
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(context, apiKey)
        }
        val client = Places.createClient(context)

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()
        client.findAutocompletePredictions(request).addOnSuccessListener { response ->
            trySend(response.autocompletePredictions.map { it.toPlace() })
        }.addOnFailureListener { exception ->
            close(exception)
        }
        awaitClose()
    }
}