package mohit.android.assignment.kotlin.model

import com.google.gson.annotations.SerializedName
import java.util.*
import java.util.Arrays.sort
import java.util.Collections.sort


data class Country(
    @SerializedName("capital") var capital: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("currency") var currency: Currency? = Currency(),
    @SerializedName("flag") var flag: String? = null,
    @SerializedName("language") var language: Language? = Language(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("region") var region: String? = null
)

data class Currency(
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("symbol") var symbol: String? = null
)

data class Language(
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null
)

fun main() {

//    bubbleSort(intArrayOf(10,2,4,2,3,5,3))
    fibonacci(8)
}
fun bubbleSort(numbers: IntArray) {
//    for (pass in 0 until (numbers.size - 1)) {
        for (currentPosition in 0 until (numbers.size - 1)) {
            // This is a single step
            if (numbers[currentPosition] > numbers[currentPosition + 1]) {
                val tmp = numbers[currentPosition]
                numbers[currentPosition] = numbers[currentPosition+1 ]
                numbers[currentPosition +1] = tmp
            }
        }
//    }
    numbers.forEach {
        println("$it")
    }
}
fun fibonacci(n: Int, a: Int = 0, b: Int = 1): Int =
    when (n) {
        0 -> a
        1 -> b
        else -> fibonacci(n - 1, b, a + b)
    }