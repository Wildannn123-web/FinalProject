import java.io.Serializable

data class PizzaItem(
    val name: String,
    val price: Int,
    var quantity: Int
) : Serializable
