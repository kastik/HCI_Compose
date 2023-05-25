package com.kastik.hci.data

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Database(entities = [Product::class, Stock::class, Supplier::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun AppDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Database.db"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Entity
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    val SupplierId: Int = 0,
    val Name: String,
    val Location: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Supplier::class,
            childColumns = ["SupplierId"],
            parentColumns = ["SupplierId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Stock::class,
            childColumns = ["StockId"],
            parentColumns = ["StockId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val ProductId: Int = 0,

    @ColumnInfo(index = true)
    val SupplierId: Int = 0,
    @ColumnInfo(index = true)
    val StockId: Int = 0,
    val ProductName: String,
    val ProductManufacturer: String,
    val ProductPrice: Int,
    val ProductDescription: String
)

@Entity
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val StockId: Int = 0,
    val Stock: Int
)

@Dao
interface AppDao {
    @Insert
    suspend fun insertSupplier(supplier: Supplier): Long

    @Insert
    suspend fun insertStock(stock: Stock): Long

    @Insert
    suspend fun insertProduct(product: Product): Long

    @Query("SELECT * FROM PRODUCT")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM Supplier")
    fun getAllSuppliers(): Flow<List<Supplier>>

    @Query("SELECT * FROM Stock")
    fun getAllStocks(): Flow<List<Stock>>

    @Query("SELECT * FROM Stock WHERE StockId=:stockId")
    //suspend
    fun getStockOfProduct(stockId: Int?): Stock

    @Query("SELECT * FROM Product WHERE ProductId=:productId")
    //suspend
    fun getProductWithId(productId: Int?): Product

    @Query("SELECT * FROM Supplier WHERE SupplierId=:supplierId")
    //suspend
    fun getSupplierWithId(supplierId: Int): Supplier

    @Query("SELECT COUNT(ProductId) FROM PRODUCT")
    fun getProductCount(): Flow<Int>
    @Query("SELECT SUM(STOCK) FROM STOCK")
    fun getStockCount(): Flow<Int>

    @Query("SELECT COUNT(SupplierId) FROM Supplier")
    fun getSupplierCount(): Flow<Int>

    @Delete
    suspend fun deleteProduct(product: Product): Int

    @Delete
    suspend fun deleteSupplier(supplier: Supplier): Int

    @Delete
    suspend fun deleteStock(stock: Stock): Int

    @Update
    //suspend
    fun updateProduct(product: Product): Int

    @Update
    //suspend
    fun updateStock(stock: Stock): Int

    @Update
    //suspend
    fun updateSupplier(supplier: Supplier): Int
}