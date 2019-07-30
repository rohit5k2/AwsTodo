package rohit5k2.awsamplify.backend.handler

import android.content.Context
import com.amazonaws.amplify.generated.graphql.CreateTodoMutation
import com.amazonaws.amplify.generated.graphql.DeleteTodoMutation
import com.amazonaws.amplify.generated.graphql.UpdateTodoMutation
import rohit5k2.awsamplify.backend.helper.NotifyUI
import type.CreateTodoInput
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response
import type.DeleteTodoInput
import type.UpdateTodoInput


class DataMutation<T>(context: Context, notifyUI: NotifyUI<T>):DataHandlerBase<T>(context, notifyUI) {
    fun add(c:CreateTodoInput){
        AWSCommHandler.mAwsAppSyncClient
            .mutate(CreateTodoMutation.builder().input(c).build())
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

    fun delete(d: DeleteTodoInput){
        AWSCommHandler.mAwsAppSyncClient
            .mutate(DeleteTodoMutation.builder().input(d).build())
            .enqueue(object : GraphQLCall.Callback<DeleteTodoMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    _notifyUI.onError(e.message)
                }

                override fun onResponse(response: Response<DeleteTodoMutation.Data>) {
                    _notifyUI.onComplete()
                }
            })
    }

    fun update(u:UpdateTodoInput){
        AWSCommHandler.mAwsAppSyncClient
            .mutate(UpdateTodoMutation.builder().input(u).build())
            .enqueue(object : GraphQLCall.Callback<UpdateTodoMutation.Data>(){
                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<UpdateTodoMutation.Data>) {

                }
            })
    }
}