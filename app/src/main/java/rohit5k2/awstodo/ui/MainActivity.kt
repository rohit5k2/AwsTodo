package rohit5k2.awstodo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.apollographql.apollo.api.Response
import kotlinx.android.synthetic.main.activity_main.*
import rohit5k2.awstodo.R
import rohit5k2.awstodo.backend.handler.DataQuery
import rohit5k2.awstodo.backend.helper.NotifyUI
import rohit5k2.awstodo.ui.helper.ToDoAdapter
import rohit5k2.awstodo.utils.L
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.amplify.generated.graphql.*
import rohit5k2.awstodo.backend.handler.DataMutation
import rohit5k2.awstodo.backend.handler.DataSubscription
import rohit5k2.awstodo.ui.helper.SwipeToDeleteCallback
import rohit5k2.awstodo.ui.subui.TodoMutationDialog
import type.CreateTodoInput
import type.DeleteTodoInput
import type.UpdateTodoInput

class MainActivity : BaseActivity() {

    private var toDoAdapter:ToDoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAll()
        initUI()
    }

    override fun onStart() {
        super.onStart()
        subscribe()
    }

    private fun initUI(){
        add_todo.setOnClickListener{
            showAddDialog()
        }
    }

    private fun showAddDialog(){
        TodoMutationDialog(this@MainActivity, object :TodoMutationDialog.TodoDataMutationDialogListener{
            override fun entryDone(name: String, desc: String) {
                addData(name, desc)
            }
        }, null).show()
    }

    private fun showEditDialog(u:UpdateTodoInput){
        TodoMutationDialog(this@MainActivity, object :TodoMutationDialog.TodoDataMutationDialogListener{
            override fun entryDone(name: String, desc: String) {
                val ui = UpdateTodoInput.builder().id(u.id()).name(name).description(desc).build()
                updateData(ui)
            }
        }, u).show()
    }

    private fun loadToDoItems(data: MutableList<ListTodosQuery.Item>?){
        lv_todo.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        lv_todo.layoutManager = LinearLayoutManager(this)

        toDoAdapter = ToDoAdapter(this@MainActivity, data, object:ToDoAdapter.NotifyRecyclerView{
            override fun deleteIt(d: DeleteTodoInput) {
                DataMutation(this@MainActivity, Notify<DeleteTodoMutation.Data>(NotifyUI.ResponseOfType.DELETE)).delete(d)
            }

            override fun updateIt(u: UpdateTodoInput) {
                showEditDialog(u)
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

    private fun addData(name:String, desc:String){
        var createToDoInput = CreateTodoInput.builder()
            .name(name)
            .description(desc)
            .build()
        DataMutation(this, Notify<CreateTodoMutation>(NotifyUI.ResponseOfType.ADD)).add(createToDoInput)
    }

    private fun updateData(u:UpdateTodoInput){
        DataMutation(this@MainActivity, Notify<UpdateTodoMutation>(NotifyUI.ResponseOfType.UPDATE)).update(u)
    }

    private fun subscribe(){
        //TODO: Need to find a better way to do this. Aim is to reduce it to only one call
        DataSubscription(this, Notify<OnCreateTodoSubscription>(NotifyUI.ResponseOfType.SUBS_CREATE)).onCreateSubscription()
        //DataSubscription(this, Notify<OnDeleteTodoSubscription>(NotifyUI.ResponseOfType.SUBS_DELETE)).onDeleteSubscription()
        DataSubscription(this, Notify<OnUpdateTodoSubscription>(NotifyUI.ResponseOfType.SUBS_UPDATE)).onUpdateSubscription()
    }

    private fun getAll(){
        DataQuery(this, Notify<ListTodosQuery.Data>(NotifyUI.ResponseOfType.QUERY_ALL)).getAll()
    }

    /**
     ******************** Callback *******************
     */

    inner class Notify<T>(t:NotifyUI.ResponseOfType) : NotifyUI<T> {
        override val responseType: NotifyUI.ResponseOfType = t

        override fun onData(data: Response<T>) {
            L.e("Data : " + data.data().toString())
            ThreadUtils.runOnUiThread {
                if (responseType == NotifyUI.ResponseOfType.QUERY_ALL) {
                    val d: List<ListTodosQuery.Item>? = (data as Response<ListTodosQuery.Data>).data()?.listTodos()?.items()
                    loadToDoItems(d?.toMutableList())
                } else if (responseType == NotifyUI.ResponseOfType.SUBS_CREATE) {
                    val x = (data as Response<OnCreateTodoSubscription.Data>).data()?.onCreateTodo()
                    val c: ListTodosQuery.Item = ListTodosQuery.Item(x!!.__typename(), x?.id(), x.name(), x.description())
                    toDoAdapter?.add(c)
                }
                else if(responseType == NotifyUI.ResponseOfType.SUBS_UPDATE){
                    val x = (data as Response<OnUpdateTodoSubscription.Data>).data()?.onUpdateTodo()
                    val c = ListTodosQuery.Item(x!!.__typename(), x?.id(), x.name(), x.description())
                    toDoAdapter?.update(c)
                }
            }
        }

        override fun onError(error: String?) {
            L.e(error)
            ThreadUtils.runOnUiThread{
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                if(responseType == NotifyUI.ResponseOfType.DELETE){
                    toDoAdapter?.undoDelete()
                }
                else if(responseType == NotifyUI.ResponseOfType.UPDATE){
                    toDoAdapter?.undoUpdate()
                }
            }
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
