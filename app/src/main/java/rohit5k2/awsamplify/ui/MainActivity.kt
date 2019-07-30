package rohit5k2.awsamplify.ui

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.amplify.generated.graphql.ListTodosQuery
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.apollographql.apollo.api.Response
import kotlinx.android.synthetic.main.activity_main.*
import rohit5k2.awsamplify.R
import rohit5k2.awsamplify.backend.handler.DataQuery
import rohit5k2.awsamplify.backend.helper.NotifyUI
import rohit5k2.awsamplify.ui.helper.ToDoAdapter
import rohit5k2.awsamplify.utils.L
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.amplify.generated.graphql.DeleteTodoMutation
import rohit5k2.awsamplify.backend.handler.DataMutation
import rohit5k2.awsamplify.ui.helper.SwipeToDeleteCallback
import type.DeleteTodoInput
import type.UpdateTodoInput

class MainActivity : BaseActivity() {

    private var toDoAdapter:ToDoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAll()
    }

    private fun loadToDoItems(data: MutableList<ListTodosQuery.Item>?){
        lv_todo.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        lv_todo.layoutManager = LinearLayoutManager(this)

        toDoAdapter = ToDoAdapter(this@MainActivity, data, object:ToDoAdapter.NotifyRecyclerView{
            override fun deleteIt(d: DeleteTodoInput) {
                DataMutation(this@MainActivity, Notify<DeleteTodoMutation.Data>()).delete(d)
            }

            override fun updateIt(d: UpdateTodoInput) {

            }

        })

        lv_todo.adapter = toDoAdapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = lv_todo.adapter as ToDoAdapter
                adapter.deleteItem(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(lv_todo)
    }

    private fun addData(){
        /*var createToDoInput = CreateTodoInput.builder()
            .name("3rd TODO")
            .description("This is the 3rd todo item")
            .build()
        DataMutation(this, this<CreateTodoInput>).add(createToDoInput)*/
    }

    private fun subscribe(){

    }

    private fun getAll(){
        DataQuery(this, Notify<ListTodosQuery.Data>()).getAll()
    }

    /**
     ******************** Callback *******************
     */

    inner class Notify<T> : NotifyUI<T> {
        override fun onData(data: Response<T>, responseOfType: NotifyUI.ResponseOfType) {
            L.e("Data : " + data.data().toString())
            if(responseOfType == NotifyUI.ResponseOfType.QUERY_ALL){
                ThreadUtils.runOnUiThread{
                    val d:List<ListTodosQuery.Item>? = (data as Response<ListTodosQuery.Data>).data()?.listTodos()?.items()
                    loadToDoItems(d?.toMutableList())
                }
            }
        }

        override fun onError(error: String?) {
            L.e(error)
        }

        override fun onComplete() {
            ThreadUtils.runOnUiThread{
                lv_todo.adapter?.notifyDataSetChanged()
            }
        }

        override fun onLog() {

        }
    }

    /**
     ******************** Callback *******************
     */
}
