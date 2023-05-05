package com.kastik.hci.database

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
import kotlin.random.Random

@Database(entities = [Product::class,Stock::class,Supplier::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    //abstract fun ProductDao(): ProductDao
    //abstract fun SupplierDao(): SupplierDao
    //abstract fun StockDao(): StockDao

    abstract fun myDao(): myDao
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
interface myDao {
    @Insert
    fun insertAll(vararg products: Product)

    @Delete
    fun delete(products: Product)

    @Query("SELECT * FROM product")
    fun getAllProducts(): List<Product>


    @Insert
    fun insertAllStocks(vararg stocks: Stock)

    @Delete
    fun delete(stock: Stock)

    @Query("SELECT * FROM stock")
    fun getAllStocks(): List<Stock>



    @Insert
    fun insertAllSuppliers(vararg supplier: Supplier)

    @Delete
    fun delete(supplier: Supplier)

    @Query("SELECT * FROM supplier")
    fun getAllSuppliers(): List<Supplier>



    //TODO
    @Query("SELECT * FROM stock")
    fun getAll(): List<Supplier>


    //TODO Add @Update fun
}



fun createSampleData(context: Context){

    val myDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "Database"
    ).allowMainThreadQueries().build()


    //val productDao = db.ProductDao()
    for(i:Int in 1..100){
        myDatabase.myDao().insertAll(
            Product(i,java.util.UUID.randomUUID().toString(), java.util.UUID.randomUUID().toString(),Random.nextInt(),java.util.UUID.randomUUID().toString()))

        myDatabase.myDao().insertAllStocks(
            Stock(i,Random.nextInt(),Random.nextInt()))

        myDatabase.myDao().insertAllSuppliers(
            Supplier(i,i,java.util.UUID.randomUUID().toString(),java.util.UUID.randomUUID().toString(),java.util.UUID.randomUUID().toString()))

    }
}
