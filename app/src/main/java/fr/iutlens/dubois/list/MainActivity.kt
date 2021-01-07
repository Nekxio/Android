package fr.iutlens.dubois.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener {
    val list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ///////////////// configuration du recyclerView
        // On configure comment les éléments de la liste sont organisés : LinearLayout => liste
        recyclerView.layoutManager = GridLayoutManager(this,3)

        // On configure l'adapter, qui prendra les éléments de list, et les affichera en utilisant
        // le layout R.layout.text_row_item
        // On précise aussi les fonctions à appeler lors d'un clic (court / long) sur un élément
        // (ici : appui long pour retirer de la liste)
        recyclerView.adapter = CustomAdapter(list, R.layout.text_row_item, null, this::removeAt)

        ///////////////// Configuration du EditText
        // On écoute ici (dans MainActivity) les évènements (c'est la fin de la saisie qui nous intéresse)
        editText.setOnEditorActionListener(this)
    }

    private fun removeAt(pos: Int): Boolean {
        list.removeAt(pos)
        recyclerView.adapter?.notifyDataSetChanged()
        return true
    }

    override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
      if(actionId == EditorInfo.IME_ACTION_DONE){ // Si on a validé le texte saisi
          list.add(editText.text.toString()) // On ajoute le texte à la liste
          recyclerView.adapter?.notifyDataSetChanged() // On prévient que la liste a changé et doit être réaffichée
          editText.text.clear(); // On efface le texte, pour faire de la place pour le prochain élément
          return true;
      }
      return false;
    }
}