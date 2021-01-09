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

    fun clear() =  viewModelScope.launch {
        AppDatabase.getDatabase()?.articleDao()?.clear()
    }

    fun allElements(): LiveData<List<Article>>? {
        return AppDatabase.getDatabase()?.articleDao()?.getAll()?.asLiveData()
    }

    fun getChannel(context: Context) {
//        clear()
        val parser = Parser.Builder()
            .context(context)
            .charset(Charset.forName("UTF8"))
            .cacheExpirationMillis(10 * 60L * 100L) // 10 minutes
            .build()

        val url = arrayOf(
            "https://www.francetvinfo.fr/titres.rss",
            "https://www.lemonde.fr/rss/en_continu.xml",
            "https://www.france24.com/fr/rss",
            "http://feeds.bbci.co.uk/news/world/rss.xml")

        url.forEach {
            viewModelScope.launch {
                try {
                    val channel = parser.getChannel(it)

                    AppDatabase.getDatabase()?.articleDao()?.insertAll(
                        *channel.articles.map { Article(it,channel.image?.url) }.toTypedArray()
                    )
                    // Do something with your data
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Handle the exception
                }
            }
        }
    }
}