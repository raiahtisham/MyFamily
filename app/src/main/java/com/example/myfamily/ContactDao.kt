package com.example.myfamily

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Data Access Object (DAO) interface for ContactModel
@Dao
interface ContactDao {

    // Insert a single contact into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactModel: ContactModel)

    // Insert a list of contacts into the database, replacing existing ones on conflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contactModelList: List<ContactModel>)

    // Retrieve all contacts from the database and observe changes using LiveData
    @Query("SELECT * FROM contactmodel")
    fun getAllContacts(): LiveData<List<ContactModel>>
}

// Explanation:
// This file is crucial for defining the operations that can be performed on the ContactModel table in the Room database.
// - The `@Dao` annotation identifies this interface as a DAO class.
// - The `insert` functions allow inserting single or multiple ContactModel objects into the database. The `onConflict` parameter specifies the conflict resolution strategy in case of duplicate entries.
// - The `getAllContacts` function retrieves all contacts from the database using a SQL query. The return type is LiveData, which allows observing changes to the database and updating the UI accordingly.
// DAOs help abstract database operations, making it easier to interact with the database without directly dealing with SQL queries. This separation of concerns improves code maintainability and readability.
