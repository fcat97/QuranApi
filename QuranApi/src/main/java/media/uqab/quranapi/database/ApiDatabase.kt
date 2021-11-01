package media.uqab.quranapi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Content::class], version = 1, exportSchema = false)
abstract class ApiDatabase: RoomDatabase() {
    abstract val apiDao: ApiDao

    companion object {
        @Volatile
        private var INSTANCE: ApiDatabase? = null

        fun getInstance(context: Context): ApiDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ApiDatabase::class.java,
                        "quranApi.room")
                        .createFromAsset("quran_full.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }

                INSTANCE = instance
                return instance;
            }
        }
    }
}