
import android.app.Application
import androidx.lifecycle.ViewModel

class LocalViewModel(application: Application) : ViewModel(){

}
    /*
private val itemsList = MutableStateFlow(listOf<Product>())
val items: StateFlow<List<Product>> get() = itemsList

private val itemIdsList = MutableStateFlow(listOf<Int>())
val itemIds: StateFlow<List<Int>> get() = itemIdsList

init {
    getData()
}

private fun getData(application: Application) {
    viewModelScope.launch {
        withContext(Dispatchers.Default) {
            itemsList.emit(AppDatabase.getDatabase(application).AppDao().getAllProducts())
        }
    }
}


fun onItemClicked(itemId: Int) {
    itemIdsList.value = itemIdsList.value.toMutableList().also { list ->
        if (list.contains(itemId)) {
            list.remove(itemId)
        } else {
            list.add(itemId)
        }
    }
}

}


/*
    fun onItemClicked(itemId: Int) {
        itemIdsList.value = itemIdsList.value.toMutableList().also { list ->
            if (list.contains(itemId)) {
                list.remove(itemId)
            } else {
                list.add(itemId)
            }
        }
    }



    fun addItems() {
        for (i in 1..1) {
            db.AppDao().insertProduct(
                Product(
                    Random.nextInt(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    Random.nextInt(),
                    UUID.randomUUID().toString()
                )
            )
        }
    }
}


 */