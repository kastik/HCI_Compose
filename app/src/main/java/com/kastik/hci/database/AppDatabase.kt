package com.kastik.hci.database

import android.content.Context
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
import kotlinx.coroutines.flow.Flow

@Database(entities = [Product::class,Stock::class,Supplier::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun AppDao(): AppDao

    companion object{
        private var INSTANCE:AppDatabase? = null

        fun getDatabase(context:Context):AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "Database.db"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries() //TODO Remove at some point
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}

//TableName = ClassName of entities if not specified
//ColumnInfo = var name if not specified
//TODO onDelete = ForeignKey.CASCADE



@Entity
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    val SupplierId: Int,
    val Name: String?,
    val Location: String?
)

@Entity(foreignKeys = [ForeignKey(entity = Supplier::class, childColumns = ["SupplierId"], parentColumns = ["SupplierId"]),
    ForeignKey(entity = Stock::class, childColumns = ["StockId"], parentColumns = ["StockId"])])
data class Product(
    @PrimaryKey(autoGenerate = true) val ProductId: Int,
    val productName: String,
    val productManufacturer: String?,
    val productPrice: Int,
    val productDescription: String?,
    val SupplierId: Int,
    val StockId: Int
)

@Entity
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val StockId: Int,
    val Stock: Int
)

/* TODO Add susped to Dao Funs */
//TODO Add @Update fun

@Dao
interface AppDao {
    @Insert
    fun insertSupplier(supplier: Supplier)
    @Insert
    fun insertStock(stock: Stock)
    @Insert
    fun insertProduct(product: Product)

    @Query("SELECT * FROM PRODUCT")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM Supplier.Name")
    fun getSupplierNames(): Flow<List<String>>

    @Query("SELECT * FROM Supplier LEFT JOIN Product ON Supplier.SupplierId==Product.SupplierId AND Product.ProductId==:productId")
    fun getSupplierOfProduct(productId: Int): Flow<Supplier>

    @Query("SELECT * FROM Stock LEFT JOIN PRODUCT ON Stock.StockId==Product.StockId AND Product.ProductId==:productId")
    fun getStockOfProduct(productId: Int): Flow<Stock>

    /*@Query("SELECT * FROM SUPPLIER WHERE ProductId==:ProductId")
    fun getSupplierOfProduct(ProductId: Int): Flow<List<Supplier>>




     */




    @Delete
    fun deleteProduct(product: Product)

    //TODO Add @Update fun
    //TODO ADD suspend fun on UPDATE DELETE AND MODIFY
}