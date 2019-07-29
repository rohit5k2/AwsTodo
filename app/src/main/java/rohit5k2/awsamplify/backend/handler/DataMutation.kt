package rohit5k2.awsamplify.backend.handler

import android.content.Context
import com.amazonaws.amplify.generated.graphql.CreateTodoMutation
import rohit5k2.awsamplify.backend.helper.NotifyUI
import type.CreateTodoInput
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response
import rohit5k2.awsamplify.utils.L


class DataMutation<T>(context: Context, notifyUI: NotifyUI<T>):DataHandlerBase<T>(context, notifyUI) {
    fun add(createToDoInput:CreateTodoInput){
        AWSCommHandler.mAwsAppSyncClient
            .mutate(CreateTodoMutation.builder().input(createToDoInput).build())
            .enqueue(object : GraphQLCall.Callback<CreateTodoMutation.Data>() {
                        override fun onResponse(response: Response<CreateTodoMutation.Data>) {
                            // Don't notify UI. let the updation come from subscription
                            //_notifyUI.onData()
                        }

                        override fun onFailure(e: ApolloException) {
                            //_notifyUI.onError(e.message)
                        }}
                    )
    }

    fun delete(){

    }

    fun edit(){

    }
}