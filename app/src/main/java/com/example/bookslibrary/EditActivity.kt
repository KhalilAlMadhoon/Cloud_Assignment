package com.example.bookslibrary

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditActivity : AppCompatActivity() {
    val TAG = "Test"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val btn_upload = findViewById<Button>(R.id.btn_upload)
        //val btn_download = findViewById<Button>(R.id.btn_download)
      //  val btn_delete = findViewById<Button>(R.id.btn_delete)

        // Instance of FirebaseStorage
        val storage = Firebase.storage

        // Points to the root reference [Root]
        var storageRef = storage.reference

        // Points to "images"
        var imagesRef = storageRef.child("images")

        // Points to "images/space.jpg"
        // Note that you can use variables to create child values
        val fileName = "space.jpg"
        var spaceRef = imagesRef.child(fileName) // images/space.jpg
        var spaceRef2 = storageRef.child("images/${fileName}") // images/space.jpg

        // File path is "images/space.jpg"
        val path = spaceRef.path

        // File name is "space.jpg"
        val name = spaceRef.name


        // Upload Files
        var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val intent: Intent? = result.data
                    val uri = intent?.data  //The uri with the location of the file
                    Log.d(TAG, "${uri.toString()}")
                    val file = getFile(applicationContext, uri!!)
                    var new_uri = Uri.fromFile(file)
                    Log.d(TAG, "${new_uri.toString()}")


                    // 1- Upload from a local file [Local file]

                    // Create file metadata including the content type [Optional]
                    var metadata = storageMetadata {
                        contentType = "image/jpg"
                    }

                    Toast.makeText(this, "${new_uri.lastPathSegment}", Toast.LENGTH_SHORT).show()
                    val selected_file_Ref = storageRef.child("images/${new_uri.lastPathSegment}")
                    var uploadTask = selected_file_Ref.putFile(new_uri)
                    //var uploadTask2 = riversRef.putFile(file,metadata)

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener { e ->
                        // Handle unsuccessful uploads
                        Log.d(TAG, "Fail ! ${e.message}")
                    }.addOnSuccessListener { taskSnapshot ->
                        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                        Log.d(TAG, "Done ! ${taskSnapshot.metadata?.sizeBytes}")
                    }
                }
            }

        btn_upload.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            resultLauncher.launch(Intent.createChooser(intent, "Select a file"))
        }


        // Download Files


        // Delete Files



        //------------------------------------------------------------------------------------------
        // Extra

        // Upload Files
        // 2- Upload from a stream [Stream]
        val uri = intent?.data
//            val file = getFile(applicationContext, uri!!)
        // val stream = FileInputStream(file)
        // var uploadTask = spaceRef.putStream(stream)
        //  uploadTask.addOnFailureListener { e ->
        // Handle unsuccessful uploads


        // Download Files
        // 2- Download to a local file [Local file]
        var islandRef = storageRef.child("images/social.jpeg")
        val resolver: ContentResolver = getContentResolver()
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "social2")
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Storage")

        val localFile = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        islandRef.getFile(localFile!!).addOnSuccessListener { imageData ->
            Toast.makeText(this, "Done !", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener { it ->
            // Handle any errors
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getFile(context: Context, uri: Uri): File? {
        val destinationFilename: File =
            File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri))
        try {
            context.getContentResolver().openInputStream(uri).use { ins ->
                createFileFromStream(
                    ins!!,
                    destinationFilename
                )
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        return destinationFilename
    }

    private fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor: Cursor = context.getContentResolver().query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }
}