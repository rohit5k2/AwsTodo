package rohit5k2.awsamplify.ui.subui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.add_todo_dialog.*
import rohit5k2.awsamplify.R

/**
 * Created by Rohit on 7/30/2019:2:52 PM
 */
class AddTodoDialog(context: Context, listener:AddTodoDataListener):Dialog(context) {
    private val addTodoDataListener = listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.add_todo_dialog)

        initUI()
    }

    private fun initUI(){
        todo_entry_done.setOnClickListener{
            addTodoDataListener.entryDone(todo_name.text.toString(), todo_desc.text.toString())
            dismiss()
        }
    }

    interface AddTodoDataListener{
        fun entryDone(name:String, desc:String)
    }
}