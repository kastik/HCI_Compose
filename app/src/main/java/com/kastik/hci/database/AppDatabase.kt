package com.kastik.hci.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Database(entities = [Product::class,Stock::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ProductDao(): ProductDao
    abstract fun StockDao(): StockDao
}


@Entity
data class Product(
    @PrimaryKey val id: Int,

    val firstName: String?,
    val lastName: String?
)

@Entity
data class Stock(
    @PrimaryKey val id: Int,

    val firstName: String?,
    val lastName: String?
)




@Dao
interface ProductDao {
    @Insert
    fun insertAll(vararg products: Product)

    @Delete
    fun delete(products: Product)

    @Query("SELECT * FROM product")
    fun getAll(): List<Product>
}

@Dao
interface StockDao {
    @Insert
    fun insertAll(vararg stocks: Stock)

    @Delete
    fun delete(stock: Stock)

    @Query("SELECT * FROM stock")
    fun getAll(): List<Stock>
}