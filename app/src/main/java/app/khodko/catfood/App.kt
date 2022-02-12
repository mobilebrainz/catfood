package app.khodko.catfood

import android.app.Application
import app.khodko.catfood.db.CatfoodRoomDatabase

class App : Application() {

    val database by lazy { CatfoodRoomDatabase.getDatabase(this) }

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}