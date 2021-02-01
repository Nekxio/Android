package fr.iutlens.dubois.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    private lateinit var adapter: CustomAdapter

    private val model: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDatabase.getDatabase(this)
        model.clear()
        setContentView(R.layout.activity_main)
        setSupportActionBar(mytoolbar)

        model.getChannel(this)

        ///////////////// configuration du recyclerView
        // On configure comment les éléments de la liste sont organisés : LinearLayout => liste
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // On configure l'adapter, qui prendra les éléments de list, et les affichera en utilisant
        // le layout R.layout.text_row_item
        // On précise aussi les fonctions à appeler lors d'un clic (court / long) sur un élément
        // (ici : appui long pour retirer de la liste)
        adapter = CustomAdapter(R.layout.text_row_item, this::open, this::removeAt)
        model.allElements()?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            swipeLayout.isRefreshing = false;
        }
        recyclerView.adapter = adapter

        swipeLayout.setOnRefreshListener { model.getChannel(this@MainActivity) }

    }

    private fun open(article: Article){
        article.link?.let{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(browserIntent)
        }
    }

    private fun removeAt(article: Article): Boolean {
        model.delete(article)
        return true
    }

}