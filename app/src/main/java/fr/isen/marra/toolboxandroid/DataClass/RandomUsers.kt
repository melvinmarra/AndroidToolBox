package fr.isen.marra.toolboxandroid.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Suppress("PLUGIN_WARNING")
@Parcelize
class RandomUsers() : Parcelable {
    @IgnoredOnParcel
    val results: ArrayList<Results> = ArrayList()
}