package app.khodko.catfood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.khodko.catfood.db.dao.FavoritesDao
import app.khodko.catfood.db.entity.Favorites


@Database(entities = [Favorites::class], version = 1, exportSchema = false)
abstract class CatfoodRoomDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {

        @Volatile
        private var INSTANCE: CatfoodRoomDatabase? = null

        fun getDatabase(context: Context): CatfoodRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        CatfoodRoomDatabase::class.java,
                        "catfood_database"
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
