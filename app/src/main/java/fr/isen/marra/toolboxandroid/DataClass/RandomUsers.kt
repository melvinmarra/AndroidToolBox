package fr.isen.marra.toolboxandroid.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RandomUsers() : Parcelable {
    val results: ArrayList<Results> = ArrayList()
}