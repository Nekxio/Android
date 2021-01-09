package fr.iutlens.dubois.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CustomAdapter(private val row_item_layout: Int,
                    private val onItemClickListener: ((Article) -> Unit)?,
                    private val onItemLongClickListener: ((Article) -> Boolean)?):
    ListAdapter<Article, CustomAdapter.ViewHolder>(ElementComparator()) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(row_item_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val article = getItem(position)
        viewHolder.textView.text = article.title
        Log.d("Adapter","image :"+ article.image)
        article.image?.let { Picasso.get().load(it).into(viewHolder.imageView); }


        // Set listeners on item (?.let content is executed only when listener is not null)
        onItemClickListener?.let {
            viewHolder.itemView.setOnClickListener { it(article) }
        }

        onItemLongClickListener?.let {
            viewHolder.itemView.setOnLongClickListener { it(article) }
        }
    }


    class ElementComparator : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.guid == newItem.guid
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}
