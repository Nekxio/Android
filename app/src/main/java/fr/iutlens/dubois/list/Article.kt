package fr.iutlens.dubois.list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prof.rssparser.Article

@Entity
data class Article(
    @PrimaryKey val guid: String,
                val title: String? = null,
                val description: String? = null,
                val image: String? = null,
                val pubDate: String? = null){

    constructor(article: com.prof.rssparser.Article) :
            this(article.guid!!,article.title,article.description,article.image, article.pubDate)
}