package fr.isen.marra.toolboxandroid.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(val city: String,
                    val coordinates: Coordinates,
                    val country: String,
                    val postcode: Int,
                    val state: String,
                    val street: Street,
                    val timezone: Timezone
): Parcelable