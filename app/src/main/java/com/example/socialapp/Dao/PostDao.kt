package com.example.socialapp.Dao

import android.security.identity.AccessControlProfileId
import com.example.socialapp.models.Post
import com.example.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = Firebase.auth

    fun addPost(text:String){
        val currentUserId = auth.currentUser!!.uid
        GlobalScope.launch {
            val userDao=UserDao()
            val user =userDao.getUserById(currentUserId).await().toObject(User::class.java)!!//to get the doc we have to pass the doc
            //to get the current time post created
            val currentTime =System.currentTimeMillis()
            val post = Post(text, user,currentTime)
            postCollection.document().set(post)

         }

    }
    //to get post which was liked
    fun getPostById(postId: String):Task<DocumentSnapshot>{
        return postCollection.document(postId).get()
    }
    fun updateLikes(postId: String){
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post=getPostById(postId).await().toObject(Post::class.java)!!

            //to check whether the person liked the post or not
            val isLiked= post.likedBy.contains(currentUserId)
            if (isLiked) {
                post.likedBy.remove(currentUserId)
            }else{
                post.likedBy.add(currentUserId)
            }
            //to save the like
            postCollection.document(postId).set(post)
        }

    }

}