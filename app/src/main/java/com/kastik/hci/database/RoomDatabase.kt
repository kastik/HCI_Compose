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
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Database(entities = [Product::class,Stock::class,Supplier::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun AppDao(): AppDao

    companion object{
        private var INSTANCE:AppDatabase? = null

        fun getDatabase(context: Context):AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "Database.db"
                    )
                        //.fallbackToDestructiveMigration()
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
    val SupplierId: Int = 0,
    val Name: String,
    val Location: String
)

@Entity(foreignKeys =
[ForeignKey(entity = Supplier::class, childColumns = ["SupplierId"], parentColumns = ["SupplierId"],onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Stock::class, childColumns = ["StockId"], parentColumns = ["StockId"],onDelete = ForeignKey.CASCADE)])
data class Product(
    @PrimaryKey(autoGenerate = true)
    val ProductId: Int = 0,
    val SupplierId: Int = 0,
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

/* TODO Add susped to Dao Funs */
//TODO Add @Update fun

@Dao
interface AppDao {
    @Insert
    fun insertSupplier(supplier: Supplier)
    @Insert
    fun insertStock(stock: Stock) : Long
    @Insert
    fun insertProduct(product: Product) : Long

    @Query("SELECT * FROM PRODUCT")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM Supplier")
    fun getAllSuppliers(): Flow<List<Supplier>>
    @Query("SELECT * FROM Stock")
    fun getAllStocks(): Flow<List<Stock>>

   @Query("SELECT Supplier.Name FROM Supplier")
   fun getSupplierNames(): Flow<List<String>>

   @Query("SELECT * FROM Supplier WHERE :supplierId==Supplier.SupplierId")
   fun getSupplierInfo(supplierId: Int): Supplier

   @Query("SELECT * FROM Stock WHERE :stockId==Stock.Stockid")
   fun getStockOfProduct(stockId: Int): Stock


   @Query("SELECT * FROM Product WHERE :productId==Product.ProductId")
   fun getProductWithId(productId: Int?) :Product?

    //@Query("SELECT * FROM Supplier LEFT JOIN Product ON Supplier.SupplierId==Product.SupplierId AND Product.ProductId==:productId")
    //fun getSupplierOfProduct(productId: Int): Flow<Supplier>

    //@Query("SELECT * FROM Stock LEFT JOIN PRODUCT ON Stock.StockId==Product.StockId AND Product.ProductId==:productId")
    //fun getStockOfProduct(productId: Int): Flow<Stock>

    /*@Query("SELECT * FROM SUPPLIER WHERE ProductId==:ProductId")
    fun getSupplierOfProduct(ProductId: Int): Flow<List<Supplier>>

     */

    @Delete
    fun deleteProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    //TODO Add @Update fun
    //TODO ADD suspend fun on UPDATE DELETE AND MODIFY
}