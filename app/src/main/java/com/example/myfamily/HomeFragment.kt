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
import com.google.android.material.shadow.ShadowRenderer
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var inviteAdapter: InviteAdapter
    lateinit var mContext: Context

    private val listContacts: ArrayList<ContactModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listMembers = listOf<MemberModel>(
            MemberModel(
                "Ahtisham Ali",
                "9th buildind, 2nd floor, maldiv road, manali 9th buildind, 2nd floor",
                "90%",
                "220"
            ),
            MemberModel(
                "Abdullah Saqib",
                "10th buildind, 3rd floor, maldiv road, manali 10th buildind, 3rd floor",
                "80%",
                "210"
            ),
            MemberModel(
                "zoraiz Saeed",
                "11th buildind, 4th floor, maldiv road, manali 11th buildind, 4th floor",
                "70%",
                "200"
            ),
            MemberModel(
                "Haroon Sidque",
                "12th buildind, 5th floor, maldiv road, manali 12th buildind, 5th floor",
                "60%",
                "190"
            ),
        )

        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        recycler.adapter = adapter


        val inviteAdapter = InviteAdapter(listContacts)

        fetchDatabaseContacts()

        CoroutineScope(Dispatchers.IO).launch {  // Implementing Threads for fetch contacts faster

            insertDatabaseContacts(fetchContacts())


        }

        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)

        inviteRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        inviteRecycler.adapter = inviteAdapter

        val threeDots = requireView().findViewById<ImageView>(R.id.family_img2)

        threeDots.setOnClickListener{

            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN, false)

            FirebaseAuth.getInstance().signOut()
        }

    }

    private fun fetchDatabaseContacts() {
        val database = MyFamilyDatabase.getDatabase(mContext)

        database.contactDao().getAllContacts().observe(viewLifecycleOwner) {

            Log.d("FetchContact89", "fetchDatabaseContacts: ")

            listContacts.clear()
            listContacts.addAll(it)

            inviteAdapter.notifyDataSetChanged()

        }
    }

    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {

        val database = MyFamilyDatabase.getDatabase(mContext)

        database.contactDao().insertAll(listContacts)

    }

    private fun fetchContacts(): ArrayList<ContactModel> {
        val cr = requireActivity().contentResolver

        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        val listContacts: ArrayList<ContactModel> = ArrayList()

        cursor?.use {
            while (it.moveToNext()) {      // Tells whether the next thing is present or not
                val id = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))  // Will fetch colums and ids
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
