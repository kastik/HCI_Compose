package com.kastik.hci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class LocalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        }


/*
        val db1 = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Product"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        val db2 = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Stock"
        ).allowMainThreadQueries() .fallbackToDestructiveMigration().build()

        val db3 = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Supplier"
        ) .fallbackToDestructiveMigration().allowMainThreadQueries().build()


        //
        //val productDao = db.ProductDao()
        //val products: List<Product> = ProductDao.getAll()
        //db.ProductDao().insertAll(Product(1,"asdas","asdasd"),Product(2,"asdas","asdasd"),Product(3,"asdas","asdasd"))

 */
    }
}