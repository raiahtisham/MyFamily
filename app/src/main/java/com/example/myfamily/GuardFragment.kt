package com.example.myfamily

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Fragment responsible for managing invites and actions related to them
class GuardFragment : Fragment(), InviteMailAdapter.OnActionClick {

    // Context variable
    lateinit var mContext: Context
    // RecyclerView for displaying invites
    private lateinit var inviteRecycler: RecyclerView
    // Button for sending invites
    private lateinit var sendInviteButton: Button
    // EditText for entering email addresses for sending invites
    private lateinit var inviteMailEditText: EditText

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Called to create the view hierarchy associated with the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_guard, container, false)

        // Initialize views
        inviteRecycler = view.findViewById(R.id.invite_recycler)
        sendInviteButton = view.findViewById(R.id.send_invite)
        inviteMailEditText = view.findViewById(R.id.invite_mail)

        return view
    }

    // Called when the fragment is attached to its context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    // Called after the fragment's view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener for sending invites
        sendInviteButton.setOnClickListener {
            sendInvite()
        }
        // Fetch invites
        getInvites()
    }

    // Function to fetch invites from Firestore
    private fun getInvites() {
        val firestore = Firebase.firestore
        // Query Firestore for invites
        firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .collection("invites").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val list: ArrayList<String> = ArrayList()
                    // Iterate through results and filter out pending invites
                    for (item in it.result) {
                        if (item.get("invite_status") == 0L) {
                            list.add(item.id)
                        }
                    }

                    Log.d("invite89", "getInvites: $list")

                    // Initialize and set up RecyclerView adapter
                    val adapter = InviteMailAdapter(list, this)
                    inviteRecycler.adapter = adapter
                }
            }
    }

    // Function to send an invite
    private fun sendInvite() {
        val mail = inviteMailEditText.text.toString()
        Log.d("Mail89", "sendInvite: $mail")

        val firestore = Firebase.firestore
        val data = hashMapOf(
            "invite_status" to 0
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        // Send invite to the specified email address
        firestore.collection("users")
            .document(mail)
            .collection("invites")
            .document(senderMail).set(data)
            .addOnSuccessListener {
                // Handle success
            }.addOnFailureListener {
                // Handle failure
            }
    }

    // Companion object to provide a factory method for creating instances of GuardFragment
    companion object {
        @JvmStatic
        fun newInstance() = GuardFragment()
    }

    // Function called when the invite is accepted
    override fun onAcceptClick(mail: String) {
        Log.d("invite89", "onAcceptClick: $mail")

        val firestore = Firebase.firestore
        val data = hashMapOf(
            "invite_status" to 1
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        // Update invite status to accepted
        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {
                // Handle success
            }.addOnFailureListener {
                // Handle failure
            }
    }

    // Function called when the invite is denied
    override fun onDenyClick(mail: String) {
        Log.d("invite89", "onDenyClick: $mail")

        val firestore = Firebase.firestore
        val data = hashMapOf(
            "invite_status" to -1
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        // Update invite status to denied
        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {
                // Handle success
            }.addOnFailureListener {
                // Handle failure
            }
    }
}
