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

    fun getChannel(context: Context) {
//        clear()
        val parser = Parser.Builder()
            .context(context)
            .charset(Charset.forName("UTF8"))
            .cacheExpirationMillis(10 * 60L * 100L) // 10 minutes
            .build()

        val url = arrayOf<Flux>(
            Flux("https://www.francetvinfo.fr/titres.rss", "France Info", "Actualité"),
                Flux("https://www.lemonde.fr/rss/en_continu.xml","Le Monde", "Actualité"),
                Flux("https://www.france24.com/fr/rss","France 24", "Actualité"),
                Flux("http://feeds.bbci.co.uk/news/world/rss.xml","BBC", "Actualité"),
                Flux("https://www.bfmtv.com/rss/info/flux-rss/flux-toutes-les-actualites/", "BFMTV","Actualité"),
                Flux("http://www.economiematin.fr/flux/alaune.xml", "Economie matin","Économie"),
                Flux("https://www.europe1.fr/rss/actualites.xml", "Europe1","Actualité"),
                Flux("https://www.sciencesetavenir.fr/rss.xml", "Science et avenir","Science"),
                Flux("https://www.turbo.fr/global.xml", "Turbo","Voiture"),
                Flux("https://www.01net.com/rss/info/flux-rss/flux-toutes-les-actualites/", "01net","Informatique"),
                Flux("https://www.clubic.com/articles.rss","Clubic","Informatique"),
                Flux("https://www.cnetfrance.fr/feeds/rss/","Cnet France","Informatique"),
                Flux("https://hitek.fr/rss","Hitek","Jeux-vidéo"),
                Flux("https://www.journaldugeek.com/feed/","Journal du geek","Jeux-vidéo"),
                Flux( "https://www.gamekult.com/feed.xml","Gamekult","Jeux-vidéo"),
                Flux("https://www.nouvelobs.com/high-tech/rss.xml","Nouvelobs","Informatique"),
                Flux("http://www.gameblog.fr/rss.php", "Gameblog","Jeux-vidéo"),
                Flux("http://www.jeuxvideo.com/rss/rss.xml","jeuxvideo.com","Jeux-vidéo"),
                Flux("https://cdn-elle.ladmedia.fr/var/plain_site/storage/flux_rss/fluxToutELLEfr.xml","Elle","Femme"),
                Flux("https://www.journaldesfemmes.fr/rss/","jounaldesfemmes","Femme"),
                Flux("https://www.nouvelobs.com/voyage/rss.xml", "nouvelobs voyage","Voyage"))

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