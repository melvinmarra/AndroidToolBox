package fr.isen.marra.toolboxandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cycle_de_vie.*

class CycleActivity : AppCompatActivity() {

    private var texte :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycle_de_vie)
        texte += "onCreate()\n"
        lifeCycleTextView.text = texte

        buttonRetour2.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        texte += "onPause()\n"
        lifeCycleTextView.text = texte
    }

    override fun onResume() {
        super.onResume()
        texte += "onResume()\n"
        lifeCycleTextView.text = texte
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext,"onDestroy()", Toast.LENGTH_SHORT).show()
    }
}

