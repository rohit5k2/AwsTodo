package rohit5k2.awsamplify.ui.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.amplify.generated.graphql.ListTodosQuery
import kotlinx.android.synthetic.main.todo_item.view.*
import rohit5k2.awsamplify.R
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.graphics.Canvas
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import rohit5k2.awsamplify.ui.BaseActivity
import kotlin.math.roundToInt


class ToDoAdapter(context: Context, items:MutableList<ListTodosQuery.Item>?):RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    private val toDos = items
    private val context = context
    private var mRecentlyDeletedItem:ListTodosQuery.Item? = null
    private var mRecentlyDeletedItemPosition:Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false))
    }

    override fun getItemCount():Int{
        if(toDos == null)
            return 0
        else
            return toDos?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.name?.text = toDos?.get(position)?.name()
        holder?.description.text = toDos?.get(position)?.description()
    }

    fun deleteItem(position: Int) {
        mRecentlyDeletedItem = toDos?.get(position)
        mRecentlyDeletedItemPosition = position
        toDos?.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    private fun showUndoSnackbar() {
        val view = (context as BaseActivity).main_layout
        val snackbar = Snackbar.make(view, R.string.undo_delete, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.undo_delete) { undoDelete() }
        snackbar.show()
    }

    private fun undoDelete() {
        toDos?.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem!!)
        notifyItemInserted(mRecentlyDeletedItemPosition)
    }

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val name:TextView = view.todo_item_name
        val description:TextView = view.todo_item_description
    }
}