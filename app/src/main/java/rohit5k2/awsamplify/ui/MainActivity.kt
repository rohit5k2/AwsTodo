package rohit5k2.awsamplify.ui

import android.os.Bundle
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

class MainActivity : BaseActivity() {

    private var toDoAdapter:ToDoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAll()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initUi(){

    }

    private fun loadToDoItems(data: MutableList<ListTodosQuery.Item>?){
        toDoAdapter = ToDoAdapter(this@MainActivity, data)
        lv_todo.layoutManager = LinearLayoutManager(this)
        lv_todo.adapter = toDoAdapter
        val itemTouchHelper = ItemTouchHelper(ToDoAdapter.SwipeToDeleteCallback(toDoAdapter as ToDoAdapter))
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
        //DataSubscription<OnCreateTodoSubscription>(this, notify<OnCreateTodoSubscription>()).subscribe()
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

        }

        override fun onLog() {

        }
    }

    /**
     ******************** Callback *******************
     */
}
