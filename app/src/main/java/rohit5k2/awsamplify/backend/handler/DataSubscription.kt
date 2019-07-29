package rohit5k2.awsamplify.backend.handler

import android.content.Context
import com.amazonaws.amplify.generated.graphql.OnCreateTodoSubscription
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import rohit5k2.awsamplify.backend.helper.NotifyUI
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import rohit5k2.awsamplify.utils.L

class DataSubscription<T>(context: Context, notifyUI: NotifyUI<T>):DataHandlerBase<T>(context, notifyUI) {
    fun subscribe(){
        onCreateSubscription()
        //TODO: add other type of subscriptions
    }

    private fun onCreateSubscription(){
        val subscription = OnCreateTodoSubscription.builder().build()
        var subscriptionWatcher:AppSyncSubscriptionCall<OnCreateTodoSubscription.Data> = AWSCommHandler.mAwsAppSyncClient.subscribe(subscription)
        subscriptionWatcher.execute(object : AppSyncSubscriptionCall.Callback<OnCreateTodoSubscription.Data> {
            override fun onFailure(e: ApolloException) {
                _notifyUI.onError(e.message)
            }

            override fun onResponse(response: Response<OnCreateTodoSubscription.Data>) {
                var a = response.data().toString()
                L.e("subscription data is : $a")
                _notifyUI.onData(response as Response<T>, NotifyUI.ResponseOfType.SUBS_CREATE)
            }

            override fun onCompleted() {
                _notifyUI.onComplete()
            }

        })
    }

    private fun onDeleteSubscription(){

    }
}