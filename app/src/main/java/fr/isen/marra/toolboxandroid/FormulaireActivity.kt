package fr.isen.marra.toolboxandroid

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_formulaire.*
import kotlinx.android.synthetic.main.activity_permission.*
import org.json.JSONObject
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FormulaireActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire)

        buttonShow.setOnClickListener{
            showData()
        }

        dateOfBirth.setOnClickListener {
            this.showDatePicker(dateSetListener)
        }

        buttonSave.setOnClickListener{
            val birthday = dateOfBirth.text.toString()
            val firstName = firstName.text.toString()
            val lastName = LastName.text.toString()
            saveData(firstName,lastName,birthday)
        }

        buttonRetour3.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }



    private fun saveData(firstName: String, lastName: String, date: String){
        val age = calculAge(dateOfBirth.text.toString())
        if(firstName.isNotEmpty() && lastName.isNotEmpty() && date.isNotEmpty()){
            val donnees = "{'lastName': '$lastName', 'firstName': '$firstName', 'date': '$date', 'age': '$age'}"
            File(cacheDir.absolutePath + "Data_user.json").writeText(donnees)
            Toast.makeText(this@FormulaireActivity, "Les données ont bien été sauvegardées", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this@FormulaireActivity, "Champ manquant", Toast.LENGTH_LONG).show()
        }
    }

    private fun showData(){
        val datas: String= File(cacheDir.absolutePath + "Data_user.json").readText()
        if(datas.isNotEmpty()){
            val jsonObject = JSONObject(datas)
            val firstNameRead = jsonObject.optString("firstName")
            val lastNameRead = jsonObject.optString("lastName")
            val birthday = jsonObject.optString("date")
            val age = jsonObject.optString("age")

            AlertDialog.Builder(this@FormulaireActivity)
                .setTitle("Données : ")
                .setMessage(" Nom : $lastNameRead \n Prenom : $firstNameRead \n Date : $birthday \n Age : $age")
                .create()
                .show()
        }else{
            Toast.makeText(this@FormulaireActivity, "Pas de données", Toast.LENGTH_LONG).show()
        }
    }


    val cal = Calendar.getInstance()
    val dateSetListener =
        DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            dateOfBirth.text = sdf.format(cal.time)
        }


    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener){
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this@FormulaireActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()


    }

    private fun calculAge(date: String): Int {

        var age = 0

        try {
            val dates = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(date)
            val today = Calendar.getInstance();
            val birth = Calendar.getInstance();

            birth.time = dates

            val thisYear = today.get(Calendar.YEAR)
            val yearBirth = birth.get(Calendar.YEAR)

            age = thisYear - yearBirth

            val thisMonth = today.get(Calendar.MONTH)
            val birthMonth = birth.get(Calendar.MONTH)

            if(birthMonth > thisMonth){
                age--
            }else if (birthMonth == thisMonth){
                val thisDay = today.get(Calendar.DAY_OF_MONTH)
                val birthDay = birth.get(Calendar.DAY_OF_MONTH)

                    if(birthDay > thisDay){
                        age--
                    }
            }
        }catch (e: ParseException){
            e.printStackTrace()
        }
        return age
    }




}






