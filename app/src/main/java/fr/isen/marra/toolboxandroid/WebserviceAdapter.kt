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

class WebserviceAdapter(private val randomContacts : RandomUsers
) : RecyclerView.Adapter<WebserviceAdapter.ViewHolderRandom>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRandom {

        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_webservice_cell, parent, false)

        return ViewHolderRandom(view)
    }

    override fun getItemCount() = randomContacts.results.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderRandom, position: Int) {
        holder.randomUserName.text =
            randomContacts.results[position].name.first + "  " + randomContacts.results[position].name.last
        holder.randomUserAddress.text =
            randomContacts.results[position].location.city + "  " + randomContacts.results[position].location.postcode
        holder.randomUserMail.text = randomContacts.results[position].email

        Picasso.get()
            .load(randomContacts.results[position].picture.large)
            .into(holder.randomUserPicture)

    }


    class ViewHolderRandom(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val randomUserName: TextView = itemView.findViewById(R.id.randomName)
        val randomUserMail: TextView = itemView.findViewById(R.id.randomMail)
        val randomUserAddress: TextView = itemView.findViewById(R.id.randomAddress)
        val randomUserPicture: ImageView = itemView.findViewById((R.id.randomPicture))
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

