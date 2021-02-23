package fr.iutlens.hilaireghesquiere.minews

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import fr.iutlens.dubois.list.R
import kotlinx.android.synthetic.main.activity_sources.*

class Sources : AppCompatActivity() {
    private val model: ListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)
        model.arrayOfFluxs.forEach {
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.text_row_sources, null)
            rowView.findViewById<TextView>(R.id.textView).text = it.name
            rowView.findViewById<TextView>(R.id.source).text = it.editeur
            rowView.setOnClickListener {view ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.editeur))
                startActivity(browserIntent)
            }
            linearSources.addView(rowView)
        }

        model.getChannel(this)
    }

    fun menuUne(item: MenuItem) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent)
    }
    fun menuLastNews(item: MenuItem) {
        val intent = Intent(this, MainActivity::class.java);
        model.getChannel(this)
        startActivity(intent)
    }
    fun menuSources(item: MenuItem){
        val intent = Intent(this, Sources::class.java);
        startActivity(intent)
    }

}