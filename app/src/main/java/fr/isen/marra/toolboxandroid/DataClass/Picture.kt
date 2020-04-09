package fr.isen.marra.toolboxandroid.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture(val large: String,
                   val medium: String,
                   val thumbnail: String
): Parcelable