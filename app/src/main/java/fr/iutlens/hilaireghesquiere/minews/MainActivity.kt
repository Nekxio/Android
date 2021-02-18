package fr.iutlens.hilaireghesquiere.minews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import fr.iutlens.dubois.list.R
import kotlinx.android.synthetic.*
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
            recyclerView.scrollToPosition(0)
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

    fun onCategoryClickActu() {
        model.allCategories("Actualité")?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            recyclerView.scrollToPosition(0)
            swipeLayout.isRefreshing = false;
        }
    }

    fun onCategoryClickCar() {
        model.allCategories("Voiture")?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            recyclerView.scrollToPosition(0)
            swipeLayout.isRefreshing = false;
        }
    }

    fun onCategoryClickComputer() {
        model.allCategories("Informatique")?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            recyclerView.scrollToPosition(0)
            swipeLayout.isRefreshing = false;
        }
    }

    fun onCategoryClickEconomy() {
        model.allCategories("Economie")?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            recyclerView.scrollToPosition(0)
            swipeLayout.isRefreshing = false;
        }
    }

    fun onCategoryClickScience() {
        model.allCategories("Science")?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            recyclerView.scrollToPosition(0)
            swipeLayout.isRefreshing = false;
        }
    }

    fun onCategoryClickTravel() {
        model.allCategories("Voyage")?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            recyclerView.scrollToPosition(0)
            swipeLayout.isRefreshing = false;
        }
    }

    fun onCategoryClickWoman() {
        model.allCategories("Femme")?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            recyclerView.scrollToPosition(0)
            swipeLayout.isRefreshing = false;
        }
    }

    fun onCategoryClickGames() {
        model.allCategories("Jeux-vidéo")?.observe(this) {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(it)
            recyclerView.scrollToPosition(0)
            swipeLayout.isRefreshing = false;
        }
    }

    fun menuUne(item: MenuItem) {

    }
    fun menuLastNews(item: MenuItem) {
        model.getChannel(this@MainActivity)
    }
    fun menuSources(item: MenuItem){
        val intent = Intent(this, Sources::class.java);
        startActivity(intent)
    }

}