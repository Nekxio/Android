package fr.iutlens.hilaireghesquiere.minews

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prof.rssparser.Article

@Entity
data class Article(
    @PrimaryKey val guid: String,
                val title: String? = null,
                val description: String? = null,
                val link: String? = null,
                val image: String? = null,
                val pubDate: String? = null,
                val source: String? =null){

    constructor(article: com.prof.rssparser.Article, channelImage : String?= null, channelName : String?=null) :
            this(article.guid!!,article.title,article.description, article.link,article.image ?: channelImage, article.pubDate, channelName)


}