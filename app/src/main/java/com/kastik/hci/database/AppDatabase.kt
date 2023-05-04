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

@Database(entities = [Product::class,Stock::class,Supplier::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ProductDao(): ProductDao
    abstract fun StockDao(): StockDao
    abstract fun SupplierDao(): SupplierDao
}


/*
@Entity(foreignKeys = [ForeignKey(entity = Stock::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("Stock")
    //,onDelete = ForeignKey.CASCADE
)])
 */

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_manufacturer ") val productManufacturer: String?,
    @ColumnInfo(name = "product_price") val productPrice: Int

)




@Entity
data class Stock(
    @PrimaryKey val id: Int,
    val localStock: Int,
    val warehouseStock: Int
)

@Entity
data class Supplier(
    @PrimaryKey val id: Int,
    val firstName: String?,
    val lastName: String?,
    val Location: String?
)



/* TODO Add susped to Dao Funs */
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

@Dao
interface SupplierDao {
    @Insert
    fun insertAll(vararg supplier: Supplier)

    @Delete
    fun delete(supplier: Supplier)

    @Query("SELECT * FROM supplier")
    fun getAll(): List<Supplier>
}



fun createSampleData(context: Context){

    val productDb = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "Product"
    ).allowMainThreadQueries().build()

    val stockDb = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "Stock"
    ).allowMainThreadQueries().build()

    val supplierDb = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "Supplier"
    ).allowMainThreadQueries().build()
/*
    //val productDao = db.ProductDao()
    productDb.ProductDao().insertAll(
        Product(4,"Some","Item"),
        Product(5,"Some","Item2"),
        Product(6,"Other","Item3"))

    productDb.ProductDao().insertAll(
        Product(1,"Some","Stock"),
        Product(5,"Some","Stock"),
        Product(6,"Other","Stock"))

    productDb.ProductDao().insertAll(
        Product(1,"Some","Supplier"),
        Product(2,"Some","Supplier2"),
        Product(3,"Other","Supplier"))
 */

}
