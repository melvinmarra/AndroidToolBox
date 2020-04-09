package fr.isen.marra.toolboxandroid.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Results(val email: String,
                   val location: Location,
                   val name: Name,
                   val picture: Picture
): Parcelable