package fr.isen.marra.toolboxandroid

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class WebserviceAdapter(val randomContacts : RandomUsers
) : RecyclerView.Adapter<WebserviceAdapter.ViewHolderRandom>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRandom {

        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_webservice_cell, parent, false)
            Log.d("Tag", "onCreateViewHolderRandomUser")
        return ViewHolderRandom(view)
    }

    override fun getItemCount() = randomContacts.results.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderRandom, position: Int) {
        holder.randomName.text =
            randomContacts.results[position].name.first + "  " + randomContacts.results[position].name.last
        holder.randomAddress.text =
            randomContacts.results[position].location.city + "  " + randomContacts.results[position].location.postcode
        holder.randomMail.text = randomContacts.results[position].email

        Picasso.get()
            .load(randomContacts.results[position].picture.large)
            .into(holder.randomPicture)

        Log.d("Tag", "onBindViewHolderRandomUser")
    }


    class ViewHolderRandom(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val randomName: TextView = itemView.findViewById(R.id.randomName)
        val randomMail: TextView = itemView.findViewById(R.id.randomMail)
        val randomAddress: TextView = itemView.findViewById(R.id.randomAddress)
        val randomPicture: ImageView = itemView.findViewById((R.id.randomPicture))
    }



    data class RandomUsers(val results: List<Result>)

    data class Result(val email: String,
                      val location: Location,
                      val name: Name,
                      val picture: Picture
    )

    data class Location(val city: String,
                        val coordinates: Coordinates,
                        val country: String,
                        val postcode: Int,
                        val state: String,
                        val street: Street,
                        val timezone: Timezone
    )

    data class Name(val first: String,
                    val last: String,
                    val title: String
    )

    data class Picture(val large: String,
                        val medium: String,
                        val thumbnail: String
    )

    data class Street(val name: String,
                      val number: Int
    )

    data class Timezone(val description: String,
                        val offset: String
    )

    data class Coordinates(val latitude: String,
                           val longitude: String
    )

}

