package fr.isen.marra.toolboxandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import fr.isen.marra.toolboxandroid.DataClass.Results
import kotlinx.android.synthetic.main.activity_webservice_user_details.*

class WebserviceUserDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webservice_user_details)

        buttonRetourDetail.setOnClickListener {
            val intent = Intent(this, WebserviceActivity::class.java)
            startActivity(intent)
        }

        val userRandom: Results = intent.getParcelableExtra("UserRandom")

        titreDetail.text = userRandom.name.title
        firstnameDetail.text = userRandom.name.first
        lastnameDetail.text = userRandom.name.last
        Picasso.get()
            .load(userRandom.picture.large)
            .into(pictureImageView)
        adresseDetail.text = userRandom.location.street.number.toString() + "" +userRandom.location.street.name
        cityDetail.text = userRandom.location.city
        postcodeDetail.text = userRandom.location.postcode.toString()
        paysDetail.text = userRandom.location.state
        emailDetail.text = userRandom.email

    }
}