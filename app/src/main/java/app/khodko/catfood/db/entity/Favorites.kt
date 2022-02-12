
package app.khodko.catfood.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorites(
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "product_key") val productKey: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}
