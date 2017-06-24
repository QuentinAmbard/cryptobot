package bot.simulation.strategy

import java.time.{LocalDateTime, ZoneOffset}

import bot.simulation.{Data, Order}

import scala.util.Random

/**
  * Created by quentin on 10/06/17.
  */
class VolatilityStrategy(c1: Double, c2: Double, f: Double) extends Strategy(c1, c2, f) {
  override def process(data: Data, previousData: List[Data]): Unit = {
    val lastData = previousData.last
    orderExecutor.deleteAllBuyOrder()
    //Higher bid is the, if we buy at this price we close the order. Doesn't make sense to buy higher than this price.
    val higherBid = orderExecutor.getHigerMarketSellBid()
    if (currency1 > 500*7) {
      println(s"C1=$currency1")
      val limit = 1
      placeOrder(limit - 0.005)
      placeOrder(limit - 0.001)
      placeOrder(limit - 0.015)
      placeOrder(limit - 0.02)
      placeOrder(limit - 0.025)
      placeOrder(limit - 0.03)
      placeOrder(limit - 0.04)
    } else {
      println("insufisent fund")
    }

    /**
      * ---------------- Sell (higherBid)
      *
      * ---------------- Buy (lowerBid)
      * -----------------Our Buy value. Must be < higherBid
      */
    def placeOrder(percent: Double) = {
      val value = percent*lastData.low
      //Only execute the order if the current price is > than the new target (might happen if the market crashed)
      if (value < higherBid) {
        orderExecutor.placeBuyOrder(Order(500, value, creationValue = lastData.middleOpenClose, creationData = lastData))
      } else {
        println(s"current value $higherBid : ${lastData} $percent * ${lastData.low} is smaller than the order ${value}. Won't place the order, market is probably going down")
      }
    }

    //STOP LOSS?
    val sellOrders = orderExecutor.getSellOrder()
    val sellPrice = lastData.high
    sellOrders.foreach(order => {
      if (order.creationValue > lastData.high * 1.15) {
        println("SIZE")
        orderExecutor.deleteSellOrder(order)
        val newOrder = order.copy(value = sellPrice, creationDate = LocalDateTime.now(ZoneOffset.UTC), id = Random.alphanumeric.take(10).mkString(""))
        println(s"STOP LOSS - bought at ${order.creationValue} - dropping initial target ${order.value} to ${sellPrice} - $lastData")
      }
//      if (order.creationDate.plusDays(5).isAfter(LocalDateTime.now(ZoneOffset.UTC))) {
//        orderExecutor.deleteSellOrder(order)
//        val newOrder = order.copy(value = order.value * 0.99, creationDate = order.creationDate.plusDays(1))
//        orderExecutor.placeSellOrder(newOrder)
//        println(s"Order expired. Decreasing its value of 1% from ${order.value} to ${newOrder.value}")
//      }
    })
  }

  override def onBuyOrderClose(order: Order): Unit = {
    super.onBuyOrderClose(order)
    //orderExecutor.placeSellOrder(Order(order.amount*0.3, order.creationValue*1.005, order.value, order.creationData))
    //orderExecutor.placeSellOrder(Order(order.amount*1, order.value*1.008, order.value, order.creationData))
    //orderExecutor.placeSellOrder(Order(order.amount*0.4, order.creationValue, order.value, order.creationData))
    //orderExecutor.placeSellOrder(Order(order.amount*0.2, order.value*1.008, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount * 0.2, order.value * 1.01, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount * 0.2, order.value * 1.015, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount * 0.2, order.value * 1.02, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount * 0.2, order.value * 1.025, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount * 0.2, order.value * 1.03, order.value, order.creationData))
    //    orderExecutor.order.amountplaceSellOrder(Order(order.amount*0.1, order.value*1.035, order.value, order.creationData))
    //    orderExecutor.placeSellOrder(Order(order.amount*0.1, order.value*1.04, order.value, order.creationData))
    //    orderExecutor.placeSellOrder(Order(order.amount*0.1, order.value*1.045, order.value, order.creationData))
  }

  override def onSellOrderClose(order: Order): Unit = {
    super.onSellOrderClose(order)
  }
}
