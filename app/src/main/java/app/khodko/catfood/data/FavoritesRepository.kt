package app.khodko.catfood.data

import androidx.annotation.WorkerThread
import app.khodko.catfood.db.dao.FavoritesDao
import app.khodko.catfood.db.entity.Favorites

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    @WorkerThread
    suspend fun insert(favorites: Favorites) {
        favoritesDao.insert(favorites)
    }

    @WorkerThread
    suspend fun delete(favorites: Favorites) {
        favoritesDao.delete(favorites)
    }

    @WorkerThread
    suspend fun getAllFavorites(userId: String) =
        favoritesDao.getAllFavorites(userId)

    @WorkerThread
    suspend fun existFavorites(userId: String, productKey: String) =
        favoritesDao.existFavorites(userId, productKey).isNotEmpty()

}