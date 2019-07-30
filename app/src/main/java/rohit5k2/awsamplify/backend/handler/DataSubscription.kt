package rohit5k2.awsamplify.backend.handler

import android.content.Context
import com.amazonaws.amplify.generated.graphql.OnCreateTodoSubscription
import com.amazonaws.amplify.generated.graphql.OnDeleteTodoSubscription
import com.amazonaws.amplify.generated.graphql.OnUpdateTodoSubscription
import rohit5k2.awsamplify.backend.helper.NotifyUI
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import rohit5k2.awsamplify.utils.L

class DataSubscription<T>(context: Context, notifyUI: NotifyUI<T>):DataHandlerBase<T>(context, notifyUI) {
    fun onCreateSubscription(){
        val subscription = OnCreateTodoSubscription.builder().build()
        var subscriptionWatcher:AppSyncSubscriptionCall<OnCreateTodoSubscription.Data> = AWSCommHandler.mAwsAppSyncClient.subscribe(subscription)
        subscriptionWatcher.execute(object : AppSyncSubscriptionCall.Callback<OnCreateTodoSubscription.Data> {
            override fun onFailure(e: ApolloException) {
                _notifyUI.onError(e.message)
            }

            override fun onResponse(response: Response<OnCreateTodoSubscription.Data>) {
                var a = response.data().toString()
                L.e("subscription data is : $a")
                _notifyUI.onData(response as Response<T>)
            }

            override fun onCompleted() {
                _notifyUI.onComplete()
            }
        })
    }

    fun onDeleteSubscription(){
        val subscription = OnDeleteTodoSubscription.builder().build()
        val subscriptionWatcher = AWSCommHandler.mAwsAppSyncClient.subscribe(subscription)
        subscriptionWatcher.execute(object : AppSyncSubscriptionCall.Callback<OnDeleteTodoSubscription.Data>{
            override fun onFailure(e: ApolloException) {
                _notifyUI.onError(e.message)
            }

            override fun onResponse(response: Response<OnDeleteTodoSubscription.Data>) {
                _notifyUI.onData(response as Response<T>)
            }

            override fun onCompleted() {
                _notifyUI.onComplete()
            }
        })
    }

    fun onUpdateSubscription(){
        val subscription = OnUpdateTodoSubscription.builder().build()
        val subscriptionWatcher = AWSCommHandler.mAwsAppSyncClient.subscribe(subscription)
        subscriptionWatcher.execute(object :AppSyncSubscriptionCall.Callback<OnUpdateTodoSubscription.Data>{
            override fun onFailure(e: ApolloException) {
                _notifyUI.onError(e.message)
            }

            override fun onResponse(response: Response<OnUpdateTodoSubscription.Data>) {
                _notifyUI.onData(response as Response<T>)
            }

            override fun onCompleted() {
                _notifyUI.onComplete()
            }
        })
    }
}