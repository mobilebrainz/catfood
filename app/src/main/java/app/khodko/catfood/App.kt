package app.khodko.catfood

import android.app.Application
import app.khodko.catfood.data.FavoritesRepository
import app.khodko.catfood.db.CatfoodRoomDatabase

class App : Application() {

    val database by lazy { CatfoodRoomDatabase.getDatabase(this) }
    val favoritesRepository by lazy { FavoritesRepository(database.favoritesDao()) }

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}