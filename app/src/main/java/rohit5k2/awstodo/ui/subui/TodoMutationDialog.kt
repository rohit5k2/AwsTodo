package rohit5k2.awstodo.ui.subui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.add_todo_dialog.*
import rohit5k2.awstodo.R
import type.UpdateTodoInput

/**
 * Created by Rohit on 7/30/2019:2:52 PM
 */
class TodoMutationDialog(context: Context, mutationDialogListener:TodoDataMutationDialogListener, updateData:UpdateTodoInput?):Dialog(context) {
    private var m = DialogMode.Add
    private val l = mutationDialogListener
    private val u = updateData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.add_todo_dialog)

        initUI()
    }

    private fun initUI(){
        if(u != null){
            todo_name.setText(u.name())
            todo_desc.setText(u.description())
        }

        todo_entry_done.setOnClickListener{
            l.entryDone(todo_name.text.toString(), todo_desc.text.toString())
            dismiss()
        }
    }

    interface TodoDataMutationDialogListener{
        fun entryDone(name:String, desc:String)
    }

    enum class DialogMode{
        Add,
        Update
    }
}