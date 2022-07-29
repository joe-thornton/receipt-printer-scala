import com.github.nscala_time.time.Imports._

class CafeDetails (
                    val shopName: String,
                    val address: String,
                    val phone: String,
                    val prices: Map[String, Double]
                  )

class ReceiptPrinter(val cafe: CafeDetails, var order: Map[String, Int] = Map()) {

  /**
   * This method should return a multiline string
   * representing a ReceiptPrinter receipt that should show
   * - shop name, address, phone number
   * - the date and time the receipt was created
   * - each item in the order, with the price. eg:
   *     2 x Blueberry Muffin       8.10
   *     1 x Cappuccino             3.85
   * - the total price
   * - the VAT (20% of total price)
   */
  val orderList: List[(String, Int)] = order.toList
  val ordersWithItemQuantityCost: List[(String, Int, Double)] = orderList.map(mapItemToCost)

  def receipt(dateAndTime: LocalDateTime = LocalDateTime.now()): String = {
    s"""$printCafeDetails
       |${printDateAndTime(dateAndTime: LocalDateTime)}
       |${ordersWithItemQuantityCost.map(printItem).mkString("\n")}
       |""".stripMargin
  }

  private[this] def printItem(item: (String, Int, Double)): String = {
    f"${item._2}%-2sx ${item._1}%-14s ${item._3}%.2f"
  }

  private[this] def mapItemToCost(item: (String, Int)): (String, Int, Double) = {
    val (name, quantity) = item

    cafe.prices.get(name) match {
      case Some(pricePerUnit) => (name, quantity, pricePerUnit * quantity)
      case None => throw new IllegalArgumentException("Cafe does not stock that item")
    }
  }

  private[this] def printDateAndTime(dateAndTime: LocalDateTime): String = {
    val dateTimeString = dateAndTime.toString("dd/MM/yy hh:mm")
    s"Transaction at: $dateTimeString"
  }

  private[this] def printCafeDetails: String = {
    s"${cafe.shopName} | ${cafe.address} | ${cafe.phone}"
  }

}