package rohit5k2.awsamplify.ui

import android.os.Bundle
import android.widget.Toast
import rohit5k2.awsamplify.R
import rohit5k2.awsamplify.backend.handler.DataMutation
import rohit5k2.awsamplify.backend.helper.NotifyUI
import rohit5k2.awsamplify.utils.L
import type.CreateTodoInput

class MainActivity : BaseActivity(), NotifyUI {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addData()
    }

    private fun addData(){
        var createToDoInput = CreateTodoInput.builder()
            .name("First TODO")
            .description("This is the first todo item")
            .build()
        DataMutation(this, this).add(createToDoInput)
    }

    override fun onData() {

    }

    override fun onError(error:String?) {
        L.e(error)
    }

    override fun onComplete() {
        Toast.makeText(this@MainActivity, "Successful", Toast.LENGTH_LONG).show()
    }

    override fun onLog() {

    }
}
