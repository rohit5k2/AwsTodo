package rohit5k2.awsamplify.backend.handler

import android.content.Context
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient

class AWSCommHandler(context: Context) {
    companion object {
        lateinit var mAwsAppSyncClient:AWSAppSyncClient
    }

    /**
     * This is just a messy hack. Need to implement "isInitialized" as soon as
     * https://youtrack.jetbrains.com/issue/KT-21862 is fixed
     *
     * @see https://stackoverflow.com/questions/51007975/isinitialized-property-for-lateinit-isnt-working-in-companion-object
     */
     init{
        initSyncClient(context)
     }

    private fun initSyncClient(context: Context){
        synchronized(AWSCommHandler::class) {
            var b = true
            try {
                b = mAwsAppSyncClient == null
            } catch (ex: UninitializedPropertyAccessException) {

            } finally {
                if (b) {
                    mAwsAppSyncClient = AWSAppSyncClient.builder()
                        .context(context.applicationContext)
                        .awsConfiguration(AWSConfiguration(context.applicationContext))
                        .build()
                }
            }
        }
    }
}