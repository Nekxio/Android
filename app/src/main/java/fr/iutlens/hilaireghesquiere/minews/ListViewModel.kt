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

    fun getChannel(context: Context) {
//        clear()
        val parser = Parser.Builder()
            .context(context)
            .charset(Charset.forName("UTF8"))
            .cacheExpirationMillis(10 * 60L * 100L) // 10 minutes
            .build()

        val url = arrayOf(
            "https://www.francetvinfo.fr/titres.rss" to "France Info",
            "https://www.lemonde.fr/rss/en_continu.xml" to "Le Monde",
            "https://www.france24.com/fr/rss" to "France 24",
            "http://feeds.bbci.co.uk/news/world/rss.xml" to "BBC",
            "https://www.bfmtv.com/rss/info/flux-rss/flux-toutes-les-actualites/" to "BFMtv",
            "http://www.economiematin.fr/flux/alaune.xml" to "Economie matin",
            "https://www.europe1.fr/rss/actualites.xml" to "Europe1",
            "https://www.sciencesetavenir.fr/rss.xml" to "Science et avenir",
            "https://www.techniques-ingenieur.fr/actualite/articles/feed/" to "Technique Ingénieur",
            "https://www.turbo.fr/global.xml" to "Turbo",
            "https://www.01net.com/rss/info/flux-rss/flux-toutes-les-actualites/" to "01net",
            "https://www.clubic.com/articles.rss" to "Clubic",
            "https://www.cnetfrance.fr/feeds/rss/" to "Cnet France",
            "https://hitek.fr/rss" to "Hitek",
            "https://www.journaldugeek.com/feed/" to "Journal du geek",
            "https://www.gamekult.com/feed.xml" to "Gamekult",
            "https://www.nouvelobs.com/high-tech/rss.xml" to "Nouvelobs",
            "http://www.gameblog.fr/rss.php" to "Gameblog",
            "http://www.jeuxvideo.com/rss/rss.xml" to "jeuxvideo.com",
            "https://cdn-elle.ladmedia.fr/var/plain_site/storage/flux_rss/fluxToutELLEfr.xml" to "toutEllefr",
            "https://www.journaldesfemmes.fr/rss/" to "jounaldesfemmes",
            "https://www.nouvelobs.com/voyage/rss.xml" to "nouvelobs voyage",
            "https://jobs-stages.letudiant.fr/jobs-etudiants/rss.xml " to "l'Etudiant")

        url.forEach {
            viewModelScope.launch {
                try {
                    val channel = parser.getChannel(it.first)

                    AppDatabase.getDatabase()?.articleDao()?.insertAll(
                        *channel.articles.map { article ->  Article(article,channel.image?.url,it.second) }.toTypedArray()
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