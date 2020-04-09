package fr.isen.marra.toolboxandroid

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.marra.toolboxandroid.DataClass.RandomUsers
import fr.isen.marra.toolboxandroid.DataClass.Results
import kotlinx.android.synthetic.main.activity_webservice_cell.view.*


class WebserviceAdapter(private val randomUser: RandomUsers, val context: Context, private val peopleClickListener: (Results) -> Unit
) : RecyclerView.Adapter<WebserviceAdapter.WebserviceAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebserviceAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.activity_webservice_cell, parent, false)
        return WebserviceAdapterViewHolder(view, randomUser, context, peopleClickListener)
    }


    override fun onBindViewHolder(holder: WebserviceAdapterViewHolder, position: Int) {
        holder.getData(position)
    }


    override fun getItemCount(): Int = randomUser.results.size

    class WebserviceAdapterViewHolder(View: View, private val randomUser: RandomUsers, val context: Context, private val peopleClickListener: (Results) -> Unit) :
        RecyclerView.ViewHolder(View) {

        private val randomUserName: TextView = View.randomName
        private val randomUserAddress: TextView = View.randomAddress
        private val randomUserEmail: TextView = View.randomMail
        private val randomUserPicture: ImageView = View.randomPicture
        private val randomUserDisplay = View.randomUserDisplay


        fun getData(position: Int) {
            val userName =
                randomUser.results[position].name.first + "" + randomUser.results[position].name.last
            val userAdress =
                randomUser.results[position].location.street.number.toString() + " " + randomUser.results[position].location.street.name + "" + randomUser.results[position].location.city + "" + randomUser.results[position].location.state
            val userEmail = randomUser.results[position].email

            Picasso.get()
                .load(randomUser.results[position].picture.medium)
                .into(randomUserPicture)

            randomUserName.text = userName
            randomUserAddress.text = userAdress
            randomUserEmail.text = userEmail

            randomUserDisplay.setOnClickListener {
                peopleClickListener.invoke(randomUser.results[position])
            }
        }
    }
}
