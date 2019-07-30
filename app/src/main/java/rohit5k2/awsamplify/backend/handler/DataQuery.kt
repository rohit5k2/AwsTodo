package rohit5k2.awsamplify.backend.handler

import android.content.Context
import com.amazonaws.amplify.generated.graphql.ListTodosQuery
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import rohit5k2.awsamplify.backend.helper.NotifyUI
import rohit5k2.awsamplify.utils.L

/**
 * Created by Rohit on 7/29/2019:3:57 PM
 */
class DataQuery<T>(context: Context, notifyUI: NotifyUI<T>):DataHandlerBase<T>(context, notifyUI) {
    fun getAll(){
        AWSCommHandler.mAwsAppSyncClient.query(ListTodosQuery.builder().build())
            .responseFetcher(AppSyncResponseFetchers.NETWORK_FIRST)
            .enqueue(object : GraphQLCall.Callback<ListTodosQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    _notifyUI.onError(e.message)
                }

                override fun onResponse(response: Response<ListTodosQuery.Data>) {
                    L.e("onQuery data is : " + response.data()?.listTodos()?.items().toString())
                    _notifyUI.onData(response as Response<T>)
                }

            })
    }
}