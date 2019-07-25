package rohit5k2.awsamplify.backend.helper

interface NotifyUI {
    fun onData()
    fun onError(error:String?)
    fun onComplete()
    fun onLog()
}