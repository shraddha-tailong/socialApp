package com.example.socialapp.Dao

import com.example.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    val db = FirebaseFirestore.getInstance()//to get the data
    val usersCollection = db.collection("users")

    fun addUser(user: User?){
        user?.let {
            GlobalScope.launch {
                usersCollection.document(user.uid).set(it)
            }
        }
    }
    fun getUserById(uId:String):Task<DocumentSnapshot>{
        return  usersCollection.document(uId).get()
    }//get user id and we will pass this function in post dao
}