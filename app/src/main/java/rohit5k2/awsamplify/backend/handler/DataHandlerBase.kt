package rohit5k2.awsamplify.backend.handler

import android.content.Context

abstract class DataHandlerBase(context: Context) {
    init {
        AWSCommHandler(context)
    }
}