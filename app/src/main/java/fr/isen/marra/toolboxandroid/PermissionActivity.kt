package fr.isen.marra.toolboxandroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {

    private val permManager = PermissionManager(this)
    private val picturePermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val contactPermission = arrayOf(
        Manifest.permission.READ_CONTACTS
    )

    val contacts = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        buttonRetour.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


        imageView.setOnClickListener {
            showPictureDialog()
        }
        //textPermissionAllow.visibility = View.GONE
        getContacts()



        contactRecycler.adapter = ContactAdapter(contacts.sorted())
        contactRecycler.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            imageView.setImageURI(data?.data)
        }else if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            val bmp = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bmp)
        }
    }

    private fun showPictureDialog() {

        if (permManager.arePermissionsOk(picturePermissions)) {
            val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems = arrayOf(
                "Select photo from gallery",
                "Capture photo from camera"
            )

            pictureDialog.setItems(pictureDialogItems,
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        0 -> pickImageFromGallery()
                        1 -> takePictureIntent()
                    }
                })
            pictureDialog.show()
        }else{
            permManager.requestMultiplePermissions(this, picturePermissions, 20)
        }
    }

    private fun pickImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, 1001)
            }
        }
    }



    private fun getContacts() {

       if (permManager.arePermissionsOk(contactPermission)) {
            val resolver: ContentResolver = contentResolver;
            val cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null
            )

            if (cursor!!.count > 0) {
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    contacts.add("  Nom : $name")

                }
            } else {
                Toast.makeText(applicationContext, "No contacts found!", Toast.LENGTH_SHORT)
                    .show()
            }
            cursor.close()
        }else{
           permManager.requestMultiplePermissions(this, contactPermission, 30)
           textPermissionAllow.visibility = View.VISIBLE
        }
    }




    open class PermissionManager(val context: Context){


        fun isPermissionOk(perm: String): Boolean {
            val result = ContextCompat.checkSelfPermission(context, perm)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun requestMultiplePermissions(activity: Activity, perms: Array<String>, code: Int) {
            ActivityCompat.requestPermissions(activity, perms, code)
        }

        fun arePermissionsOk(perms: Array<String>): Boolean {
            for (p in perms) {
                if (isPermissionOk(p))
                    continue
                else
                    return false
            }
            return true
        }

    }
}









