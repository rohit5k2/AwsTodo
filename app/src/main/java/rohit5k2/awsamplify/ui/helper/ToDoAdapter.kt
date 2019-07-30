package rohit5k2.awsamplify.ui.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.amplify.generated.graphql.ListTodosQuery
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.todo_item.view.*
import rohit5k2.awsamplify.R
import rohit5k2.awsamplify.ui.BaseActivity
import android.os.Handler
import type.DeleteTodoInput
import type.UpdateTodoInput


class ToDoAdapter(context: Context, items:MutableList<ListTodosQuery.Item>?, notifyRecycler:NotifyRecyclerView):RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    private val toDos = items
    private val context = context
    private var mRecentlyDeletedItem:ListTodosQuery.Item? = null
    private var mRecentlyDeletedItemPosition:Int = -1
    private var deleteHandler:Handler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false))
    }

    override fun getItemCount():Int{
        return if(toDos == null)
            0
        else
            toDos?.size
    }

    fun add(a:ListTodosQuery.Item){
        toDos?.add(a)
        notifyItemInserted(toDos?.size!!.minus(1))
        //notifyDataSetChanged()
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
        enqueDelete()
    }

    private fun enqueDelete(){
        deleteHandler = Handler()
        deleteHandler?.postDelayed(deleteRunnable, 5500)
    }

    private var deleteRunnable:Runnable = Runnable {
        var d:DeleteTodoInput = DeleteTodoInput.builder().id(mRecentlyDeletedItem?.id()).build()
        notifyRecycler.deleteIt(d)
    }

    private fun showUndoSnackbar() {
        val view = (context as BaseActivity).main_layout
        val snackbar = Snackbar.make(view, R.string.undo_delete, 5000)
        snackbar.setAction(R.string.undo_delete) { undoDelete() }
        snackbar.show()
    }

    fun undoDelete() {
        deleteHandler?.removeCallbacks(deleteRunnable)
        deleteHandler = null

        toDos?.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem!!)
        notifyItemInserted(mRecentlyDeletedItemPosition)
    }

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val name:TextView = view.todo_item_name
        val description:TextView = view.todo_item_description
    }

    interface NotifyRecyclerView{
        fun deleteIt(d:DeleteTodoInput)
        fun updateIt(d:UpdateTodoInput)
    }
}