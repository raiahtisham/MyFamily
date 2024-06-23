package com.example.myfamily

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    // Declaring parameters
    private var param1: String? = null
    private var param2: String? = null

    // Declaring the inviteAdapter
    private lateinit var inviteAdapter: InviteAdapter
    private lateinit var mContext: Context

    // Getting the context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    // List to store contacts
    private val listContacts: ArrayList<ContactModel> = ArrayList()

    // Fragment onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Inflate the layout for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // Executed after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creating a list of members
        val listMembers = listOf(
            MemberModel(
                "Ahtisham Ali",
                "9th building, 2nd floor, maldiv road, manali 9th building, 2nd floor",
                "90%",
                "220"
            ),
            MemberModel(
                "Abdullah Saqib",
                "10th building, 3rd floor, maldiv road, manali 10th building, 3rd floor",
                "80%",
                "210"
            ),
            MemberModel(
                "zoraiz Saeed",
                "11th building, 4th floor, maldiv road, manali 11th building, 4th floor",
                "70%",
                "200"
            ),
            MemberModel(
                "Haroon Sidque",
                "12th building, 5th floor, maldiv road, manali 12th building, 5th floor",
                "60%",
                "190"
            ),
        )

        // Setting up the adapter with the list of members
        val adapter = MemberAdapter(listMembers)

        // Getting the recycler view for members and setting its layout manager and adapter
        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // Setting up the inviteAdapter with the list of contacts
        inviteAdapter = InviteAdapter(listContacts)

        // Fetching database contacts
        fetchDatabaseContacts(listContacts)

        // Launching a coroutine to insert contacts into the database
        CoroutineScope(Dispatchers.IO).launch {
            insertDatabaseContacts(fetchContacts())
        }

        // Getting the recycler view for invites and setting its layout manager and adapter
        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inviteRecycler.adapter = inviteAdapter

        // Handling the click event for the three dots image view
        val threeDots = requireView().findViewById<ImageView>(R.id.family_img2)
        threeDots.setOnClickListener {
            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN, false)
            FirebaseAuth.getInstance().signOut()
        }
    }

    // Fetches contacts from the database and updates the UI
    private fun fetchDatabaseContacts(listContacts: ArrayList<ContactModel>) {
        val database = MyFamilyDatabase.getDatabase(mContext)
        database.contactDao().getAllContacts().observe(viewLifecycleOwner) {
            Log.d("FetchContact89", "fetchDatabaseContacts: ")
            this.listContacts.clear()
            this.listContacts.addAll(it)
            inviteAdapter.notifyDataSetChanged()
        }
    }

    // Inserts contacts into the database
    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {
        val database = MyFamilyDatabase.getDatabase(mContext)
        database.contactDao().insertAll(listContacts)
    }

    // Fetches contacts from the device
    private fun fetchContacts(): ArrayList<ContactModel> {
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        val listContacts: ArrayList<ContactModel> = ArrayList()

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = it.getInt(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhoneNumber > 0) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                        arrayOf(id),
                        null
                    )
                    pCur?.use { phoneCursor ->
                        while (phoneCursor.moveToNext()) {
                            val phoneNum = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            listContacts.add(ContactModel(name, phoneNum))
                        }
                    }
                }
            }
        }
        return listContacts
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
