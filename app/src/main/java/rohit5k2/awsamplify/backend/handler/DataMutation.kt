package rohit5k2.awsamplify.backend.handler

import android.content.Context
import com.amazonaws.amplify.generated.graphql.CreateTodoMutation
import rohit5k2.awsamplify.backend.helper.NotifyUI
import type.CreateTodoInput
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response


class DataMutation(context: Context, notifyUI: NotifyUI):DataHandlerBase(context) {
    private var _notifyUI = notifyUI

    fun add(createToDoInput:CreateTodoInput){
        AWSCommHandler.mAwsAppSyncClient
            .mutate(CreateTodoMutation.builder().input(createToDoInput).build())
            .enqueue(object : GraphQLCall.Callback<CreateTodoMutation.Data>() {
                        override fun onResponse(response: Response<CreateTodoMutation.Data>) {
                            _notifyUI.onComplete()
                        }

                        override fun onFailure(e: ApolloException) {
                            _notifyUI.onError(e.message)
                        }}
                    )
    }
}