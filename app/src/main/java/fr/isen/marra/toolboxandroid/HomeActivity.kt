package fr.isen.marra.toolboxandroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val USER_PREFS = "user_prefs"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        cycleImg.setOnClickListener {
            val intent = Intent(this, CycleActivity::class.java)
            startActivity(intent)
        }

        saveImg.setOnClickListener {
            val intent = Intent(this, FormulaireActivity::class.java)
            startActivity(intent)

        }

        permImg.setOnClickListener {
            val intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)
        }

        webImg.setOnClickListener{
            val intent = Intent(this, WebserviceActivity::class.java)
            startActivity(intent)
        }

        imageBle.setOnClickListener{
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            Toast.makeText(applicationContext, "Déconnexion réussi !", Toast.LENGTH_SHORT).show()
            this.finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }
    }
}

