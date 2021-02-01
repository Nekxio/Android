package fr.iutlens.hilaireghesquiere.minews

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Article::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): articleDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context? = null): AppDatabase? {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                context?.let {
                    val instance = Room.databaseBuilder(
                            it.applicationContext,
                            AppDatabase::class.java,
                            "database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
            }
        }
    }
}