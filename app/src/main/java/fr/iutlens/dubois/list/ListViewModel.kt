package fr.iutlens.dubois.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.prof.rssparser.Parser
import kotlinx.coroutines.launch
import java.nio.charset.Charset

class ListViewModel() : ViewModel() {

    fun insert(element: Article) =  viewModelScope.launch {
        AppDatabase.getDatabase()?.articleDao()?.insertAll(element)
    }

    fun delete(element: Article) = viewModelScope.launch {
        AppDatabase.getDatabase()?.articleDao()?.delete(element)
    }

    fun allElements(): LiveData<List<Article>>? {
        return AppDatabase.getDatabase()?.articleDao()?.getAll()?.asLiveData()
    }

    fun getChannel(context: Context) {
        val parser = Parser.Builder()
            .context(context)
            .charset(Charset.forName("UTF8"))
            .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
            .build()

        val url = "https://news.google.com/rss/search?q=source:AFP&um=1&ie=UTF-8&num=100&hl=fr&gl=FR&ceid=FR:fr"

        viewModelScope.launch {
            try {
                val channel = parser.getChannel(url)

                AppDatabase.getDatabase()?.articleDao()?.insertAll(
                    *channel.articles.map { Article(it) }.toTypedArray()
                )
                // Do something with your data
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the exception
            }
        }
    }
}