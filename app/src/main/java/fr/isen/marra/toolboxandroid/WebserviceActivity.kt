package fr.isen.marra.toolboxandroid

import android.Manifest
import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_webservice.*


class WebserviceActivity : AppCompatActivity() {

    private val url = "https://randomuser.me/api/?results=20&inc=name,location,email,picture"
    private val internetStoragePermissions = arrayOf(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webservice)

    }
}
