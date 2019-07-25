package rohit5k2.awsamplify.backend.handler

import android.content.Context
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient

class AWSCommHandler(context: Context) {
    companion object {
        lateinit var mAwsAppSyncClient: AWSAppSyncClient
    }

    init{
        mAwsAppSyncClient = AWSAppSyncClient.builder()
            .context(context.applicationContext)
            .awsConfiguration(AWSConfiguration(context.applicationContext))
            .build()
    }
}