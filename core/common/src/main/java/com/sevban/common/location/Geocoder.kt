package com.sevban.common.location

import android.content.Context
import android.location.Geocoder
import com.sevban.model.Place
import com.sevban.model.PlaceCoordinates
import com.sevban.model.PlaceText
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Geocoder @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val geocoder = Geocoder(context)

    //TODO : These operations must be moved to appropriate dispatchers since they
    // may be performing network operations see Geocoder documentation..
    fun getPlace(latitude: Double, longitude: Double): Place? {
        require(Geocoder.isPresent()) { "Geocoder is not present" }

        //TODO : Can be optimized by adding limits to area to be searched for the name.
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return addresses?.firstOrNull()?.let { address ->
            Place(
                name = address.locality,
                country = address.countryName,
                latitude = address.latitude,
                longitude = address.longitude
            )
        }
    }

    fun getPlaceCoordinates(placeText: PlaceText): Place? {
        require(Geocoder.isPresent()) { "Geocoder is not present" }

        //TODO : Can be optimized by adding limits to area to be searched for the name.
        val addresses = geocoder.getFromLocationName(placeText.fullText, 1)
        return addresses?.firstOrNull()?.let { address ->
            println("Alindi ${placeText.fullText}")

            println("Cikti ${address.locality} ${address.countryName}")

            Place(
                name = placeText.primaryText,
                country = placeText.secondaryText,
                latitude = address.latitude,
                longitude = address.longitude
            )
        }
    }

}
