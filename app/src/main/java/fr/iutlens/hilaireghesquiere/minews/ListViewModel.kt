package fr.iutlens.hilaireghesquiere.minews

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

    fun allCategories(category: String): LiveData<List<Article>>? {
        return AppDatabase.getDatabase()?.articleDao()?.getCategory(category)?.asLiveData()
    }

    val arrayOfFluxs: Array<Flux>
        get() {
            val url = arrayOf<Flux>(
                Flux("https://www.francetvinfo.fr/titres.rss", "France Info", "Actualité", "https://www.francetvinfo.fr/"),
                Flux("https://www.lemonde.fr/rss/en_continu.xml", "Le Monde", "Actualité", "https://www.lemonde.fr/"),
                Flux("https://www.france24.com/fr/rss", "France 24", "Actualité", "https://www.france24.com/"),
                Flux("http://feeds.bbci.co.uk/news/world/rss.xml", "BBC", "Actualité", "http://feeds.bbci.co.uk/"),
                Flux("https://www.bfmtv.com/rss/info/flux-rss/flux-toutes-les-actualites/", "BFMTV", "Actualité", "https://www.bfmtv.com/"),
                Flux("http://www.economiematin.fr/flux/alaune.xml", "Economie matin", "Economie", "http://www.economiematin.fr/"),
                Flux("https://www.economie.gouv.fr/daj/rss", "Economie.gouv", "Economie", "https://www.economie.gouv.fr/"),
                Flux("https://www.europe1.fr/rss/actualites.xml", "Europe1", "Actualité", "https://www.europe1.fr/"),
                Flux("https://www.sciencesetavenir.fr/rss.xml", "Science et avenir", "Science", "https://www.sciencesetavenir.fr/"),
                Flux("https://www.turbo.fr/global.xml", "Turbo", "Voiture", "https://www.turbo.fr/"),
                Flux("https://www.01net.com/rss/info/flux-rss/flux-toutes-les-actualites/", "01net", "Informatique", "https://www.01net.com/"),
                Flux("https://www.clubic.com/articles.rss", "Clubic", "Informatique","https://www.clubic.com/"),
                Flux("https://www.cnetfrance.fr/feeds/rss/", "Cnet France", "Informatique","https://www.cnetfrance.fr/"),
                Flux("https://hitek.fr/rss", "Hitek", "Jeux-vidéo","https://hitek.fr"),
                Flux("https://www.journaldugeek.com/feed/", "Journal du geek", "Jeux-vidéo", "https://www.journaldugeek.com/"),
                Flux("https://www.gamekult.com/feed.xml", "Gamekult", "Jeux-vidéo", "https://www.gamekult.com/"),
                Flux("https://www.nouvelobs.com/high-tech/rss.xml", "Nouvelobs", "Informatique", "https://www.nouvelobs.com/"),
                Flux("http://www.gameblog.fr/rss.php", "Gameblog", "Jeux-vidéo", "http://www.gameblog.fr/"),
                Flux("http://www.jeuxvideo.com/rss/rss.xml", "jeuxvideo.com", "Jeux-vidéo", "http://www.jeuxvideo.com/"),
                Flux("https://cdn-elle.ladmedia.fr/var/plain_site/storage/flux_rss/fluxToutELLEfr.xml", "Elle", "Femme", "https://www.elle.fr"),
                Flux("https://www.journaldesfemmes.fr/rss/", "jounaldesfemmes", "Femme", "https://www.journaldesfemmes.fr/"),
                Flux("https://www.nouvelobs.com/voyage/rss.xml", "nouvelobs voyage", "Voyage", "https://www.nouvelobs.com/")
            )
            return url
        }

    fun getChannel(context: Context) {
//        clear()
        val parser = Parser.Builder()
            .context(context)
            .charset(Charset.forName("UTF8"))
            .cacheExpirationMillis(10 * 60L * 100L) // 10 minutes
            .build()

        val url = arrayOfFluxs

        url.forEach {
            viewModelScope.launch {
                try {
                    val channel = parser.getChannel(it.url)

                    AppDatabase.getDatabase()?.articleDao()?.insertAll(
                        *channel.articles.map { article ->  Article(article,channel.image?.url,it.name, it.category) }.toTypedArray()
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