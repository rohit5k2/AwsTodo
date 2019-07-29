package rohit5k2.awsamplify.backend.helper

import com.apollographql.apollo.api.Response

interface NotifyUI<T> {
    fun onData(data: Response<T>, type:ResponseOfType)
    fun onError(error:String?)
    fun onComplete()
    fun onLog()

    enum class ResponseOfType{
        ADD,
        DELETE,
        UPDATE,
        QUERY_ALL,
        SUBS_CREATE,
        SUBS_DELETE,
        SUBS_UPDATE
    }
}