package mohit.android.assignment.kotlin.data

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message:String?){
    companion object{

        fun <T> success(data:T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg:String, data:T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }
    }

    enum class Status {
        SUCCESS,
        ERROR,
    }
}