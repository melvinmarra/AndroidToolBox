package fr.isen.marra.toolboxandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_formulaire.*
import kotlinx.android.synthetic.main.activity_webservice.*


class WebserviceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webservice)


        GenerateButton.setOnClickListener {
            displayRandomUsers()
        }

        buttonRetour4.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        Log.d("STATUS", "onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.d("STATUS", "onResume")
    }


    private fun displayRandomUsers() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://randomuser.me/api/?inc=name,location,email,picture&results=15&noinfo&nat=fr&format=pretty"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val randomUsersObj =
                    Gson().fromJson(response.toString(), WebserviceAdapter.RandomUsers::class.java)

                Log.d("RESPONSE API", response)
                recyclerViewRandomUsers.adapter = WebserviceAdapter(randomUsersObj)
                recyclerViewRandomUsers.layoutManager = LinearLayoutManager(this)

            },
            Response.ErrorListener { titleWebActivity.text = "Not working" })

        queue.add(stringRequest)
    }

}

