package com.hodamohammadi.conveyor.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hodamohammadi.conveyor.models.DefaultDialog
import com.hodamohammadi.conveyor.models.DefaultMessage
import com.hodamohammadi.conveyor.models.DefaultUser
import com.stfalcon.chatkit.commons.models.IMessage
import java.util.Date

/**
 * Helper class for Firebase services.
 */
class FirebaseHelper private constructor() {
    companion object {
        private val TAG = FirebaseHelper::class.qualifiedName
        private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        fun isUserAuthenticated(): Boolean {
            return firebaseAuth.currentUser != null
        }

        fun getCurrentUser(): DefaultUser {
            val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
            return DefaultUser(firebaseUser!!.uid, firebaseUser.displayName,
                    firebaseUser.photoUrl.toString(), getUserThreads())
        }

        fun sendMessage(messageInput: String, threadId: String): IMessage {
            val messageReference: DatabaseReference =
                    getThreadsDatabase().child(threadId)
            val messageKey: String? = messageReference.push().key
            val message = DefaultMessage(messageKey!!, messageInput, Date(), getCurrentUser())
            messageReference.child(messageKey).setValue(message)
            return message
        }

        private fun getThreadsDatabase(): DatabaseReference {
            val databaseReference: DatabaseReference =
                    firebaseDatabase.getReference(FirebaseConstants.THREADS_DATABASE)
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d(TAG, "successful database reference")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, databaseError.message)
                }
            })
            return databaseReference
        }

        private fun getUsersDatabase(): DatabaseReference {
            val databaseReference: DatabaseReference =
                    firebaseDatabase.getReference(FirebaseConstants.USERS_DATABASE)
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d(TAG, "successful database reference")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, databaseError.message)
                }
            })
            return databaseReference
        }

        private fun getUserThreads(): List<DefaultDialog> {
            val threads: MutableList<DefaultDialog> = mutableListOf()
            val messageReference: DatabaseReference =
                    getUsersDatabase().child(firebaseAuth.currentUser!!.uid)
                            .child(FirebaseConstants.USER_THREADS)

            // TODO: get user's threads list
            messageReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (childDataSnapshot: DataSnapshot in dataSnapshot.children) {
                                childDataSnapshot.getValue(DefaultDialog::class.java)!!
                                threads.add(childDataSnapshot.getValue(DefaultDialog::class.java)!!)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                        }
                    })

            return threads
        }
    }
}