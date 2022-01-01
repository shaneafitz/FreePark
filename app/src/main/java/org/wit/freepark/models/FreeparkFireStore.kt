package org.wit.freepark.models


import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.wit.freepark.helpers.readImageFromPath
import java.io.ByteArrayOutputStream
import java.io.File

class FreeparkFireStore(val context: Context) : FreeparkStore {
    val freeparks = ArrayList<FreeparkModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override suspend fun findAll(): List<FreeparkModel> {
        return freeparks
    }

    override suspend fun findById(id: Long): FreeparkModel? {
        val foundFreepark: FreeparkModel? = freeparks.find { p -> p.id == id }
        return foundFreepark
    }

    override suspend fun create(freepark: FreeparkModel) {
        val key = db.child("users").child(userId).child("freeparks").push().key
        key?.let {
            freepark.fbId = key
            freeparks.add(freepark)
            db.child("users").child(userId).child("freeparks").child(key).setValue(freepark)
            updateImage(freepark)
        }
    }

    override suspend fun update(freepark: FreeparkModel) {
        var foundFreepark: FreeparkModel? = freeparks.find { p -> p.fbId == freepark.fbId }
        if (foundFreepark != null) {
            foundFreepark.title = freepark.title
            foundFreepark.description = freepark.description
            foundFreepark.image = freepark.image
            foundFreepark.location = freepark.location
        }

        db.child("users").child(userId).child("freeparks").child(freepark.fbId).setValue(freepark)
        if(freepark.image.length > 0){
            updateImage(freepark)
        }

    }

    override suspend fun delete(freepark: FreeparkModel) {
        db.child("users").child(userId).child("freeparks").child(freepark.fbId).removeValue()
        freeparks.remove(freepark)
    }

    override suspend fun clear() {
        freeparks.clear()
    }

    fun fetchFreeparks(freeparksReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(freeparks) {
                    it.getValue<FreeparkModel>(
                        FreeparkModel::class.java
                    )
                }
                freeparksReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        st = FirebaseStorage.getInstance().reference
        db =
            FirebaseDatabase.getInstance("https://freepark-68b8a-default-rtdb.firebaseio.com").reference
        freeparks.clear()
        db.child("users").child(userId).child("freeparks")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImage(freepark: FreeparkModel) {
        if (freepark.image != "") {
            val fileName = File(freepark.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, freepark.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        freepark.image = it.toString()
                        db.child("users").child(userId).child("freeparks").child(freepark.fbId)
                            .setValue(freepark)
                    }
                }
            }
        }
    }
}