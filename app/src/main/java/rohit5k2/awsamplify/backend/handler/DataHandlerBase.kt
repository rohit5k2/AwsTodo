package rohit5k2.awsamplify.backend.handler

import android.content.Context
import rohit5k2.awsamplify.backend.helper.NotifyUI

abstract class DataHandlerBase<T>(context: Context, notifyUI: NotifyUI<T>) {
    protected var _notifyUI = notifyUI
    init {
        AWSCommHandler(context)
    }
}