package com.kastik.hci.database

import android.content.Context
import androidx.lifecycle.LiveData
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
data class Product(
    @PrimaryKey(autoGenerate = true) val ProductId: Int,
    val productName: String,
    val productManufacturer: String?,
    val productPrice: Int,
    val productDescription: String?

)

@Entity(foreignKeys = [ForeignKey(entity = Product::class, childColumns = ["ProductId"], parentColumns = ["ProductId"])])
data class Stock(
    @PrimaryKey
    val ProductId: Int,
    val localStock: Int,
    val warehouseStock: Int,
    val totalStock: Int = warehouseStock+localStock
)

@Entity(foreignKeys = [ForeignKey(entity = Product::class, childColumns = ["ProductId"], parentColumns = ["ProductId"])])
data class Supplier(
    @PrimaryKey
    val SupplierId: Int,
    val ProductId: Int,
    val firstName: String?,
    val lastName: String?,
    val Location: String?
)
/* TODO Add susped to Dao Funs */
//TODO Add @Update fun

@Dao
interface AppDao {
    @Query("SELECT * FROM Stock WHERE ProductId==:ProductId")
    fun getStockOfProduct(ProductId: Int): Flow<Stock>

    @Query("SELECT * FROM SUPPLIER WHERE ProductId==:ProductId")
    fun getSupplierOfProduct(ProductId: Int): Flow<Supplier>

    @Query("SELECT * FROM PRODUCT")
    fun getAllProducts(): Flow<List<Product>>

    @Insert
    fun insertProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)

    @Query("SELECT COUNT(ProductId) FROM Product")
    fun getDataCount(): LiveData<Int?>

    //TODO Add @Update fun
    //TODO ADD suspend fun on UPDATE DELETE AND MODIFY
}