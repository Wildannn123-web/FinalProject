import java.io.Serializable

data class DrinkItem(
    val name: String,
    val price: Int,
    var quantity: Int
) : Serializable
