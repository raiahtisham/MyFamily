package com.example.myfamily

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myfamily.ContactDao

// Annotates the class to be a Room database with the specified entities and version
@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
public abstract class MyFamilyDatabase : RoomDatabase() {

    // Abstract method to get the DAO for the ContactModel
    abstract fun contactDao(): ContactDao

    companion object {

        // Marks the JVM backing field of the annotated property as volatile, meaning writes to this field are immediately made visible to other threads
        @Volatile
        private var INSTANCE: MyFamilyDatabase? = null

        // Function to get the database instance
        fun getDatabase(context: Context): MyFamilyDatabase {

            // Return the existing instance if it exists
            INSTANCE?.let {
                return it
            }

            // Synchronize to ensure only one instance of the database is created
            return synchronized(MyFamilyDatabase::class.java) {
                // Build the database instance
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyFamilyDatabase::class.java,
                    "my_family_db"
                ).build()

                // Assign the instance to the INSTANCE variable
                INSTANCE = instance

                // Return the newly created instance
                instance
            }
        }
    }
}

// Explanation:
// This file defines a Room database for the application. Room is a persistence library that provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
// - The `@Database` annotation defines the database configuration and associates it with the Data Access Object (DAO) classes that manage the interaction with the database.
// - The `companion object` contains a singleton pattern to ensure that only one instance of the database is created during the app's lifecycle. This is important to prevent multiple instances from being created, which could lead to database corruption or inconsistent data.
