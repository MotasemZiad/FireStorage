package com.ziad.motasem.firestorage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ziad.motasem.firestorage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "mzn"
    private val pickImageRequest = 6000
    private lateinit var binding: ActivityMainBinding
    private lateinit var filePath: Uri
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPick.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)
        storageReference = FirebaseStorage.getInstance().reference


    }

    override fun onClick(view: View?) {
        if (view == binding.btnPick) {
            pickImage()
        } else if (view == binding.btnUpload) {
            uploadImage()
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), pickImageRequest)
    }

    private fun uploadImage() {
        val imgReference = storageReference.child("images")
        val childReference = imgReference.child(System.currentTimeMillis().toString())
        imgReference.putFile(filePath).addOnSuccessListener {
            Log.e(TAG, "Success")
        }.addOnFailureListener {
            Log.e(TAG, "Fail ${it.message.toString()}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequest && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data!!
            binding.imageStorage.setImageURI(filePath)
        }
    }
}