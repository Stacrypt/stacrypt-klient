package io.stacrypt.stadroid.data

private enum class ContentStatus {
    LOADING,
    LOADED,
    COMPLETED,
    FAILED;
}

private enum class ReliabilityStatus {
    LIVE,
    CACHE,
    NONE;
}

private enum class NetworkStatus {
    REALTIME,
    ONLINE,
    OFFLINE,
    ERROR;
}


@Suppress("DataClassPrivateConstructor")
data class LoadingState private constructor(
    val content: ContentStatus,
    val reliability: ReliabilityStatus,
    val network: NetworkStatus,
    val msg: String? = null
) {
//    companion object {
//        val LOADED = NetworkState(NetworkStatus.SUCCESS)
//        val LOADING = NetworkState(NetworkStatus.RUNNING)
//        fun error(msg: String?) = NetworkState(NetworkStatus.FAILED, msg)
//    }
//
//    override fun toString(): String =
//        when (status) {
//            NetworkStatus.RUNNING -> "Loading ..."
//            NetworkStatus.SUCCESS -> "Loading Completed!"
//            NetworkStatus.FAILED -> "Error Loading! Try Again!"
//        }
}
