package bot.simulation

import java.time.LocalDateTime

import bot.simulation.strategy.Strategy

import scala.collection.{Iterable, mutable}
import scala.collection.mutable.ListBuffer


abstract class BackTestExecutor extends OrderExecutor {
  val sellOrders = mutable.Set[Order]()
  val buyOrders = mutable.Set[Order]()

  def placeSellOrder(order: Order) = sellOrders += order

  def placeBuyOrder(order: Order) = buyOrders += order

  def getSellOrder(): Iterable[Order] = sellOrders

  def getBuyOrder(): Iterable[Order] = buyOrders

  def deleteAllBuyOrder(): Unit = buyOrders.clear()

  def deleteAllSellOrder(): Unit = sellOrders.clear()

  def deleteBuyOrder(order: Order): Unit = buyOrders.remove(order)

  def deleteSellOrder(order: Order): Unit = sellOrders.remove(order)
}


class BackTest(data: List[Data], strategy: Strategy, startDate: Option[LocalDateTime] = None) {
  //Assume the current higher and lower bids are random, (since it's just a safe guard with the current volatility strategy it doesn't really matter)
  strategy.orderExecutor = new BackTestExecutor() {
    override def getHigerMarketSellBid(): Double = previousData.last.high*1.1

    override def getLowerMarketBuyBid(): Double = previousData.last.low*1.1
  }
  var previousData = ListBuffer[Data]()

  def simulate() = {
    previousData = ListBuffer[Data]()
    data.foreach(d => {
      //First process the old orders with the new incoming data (we simulate what just happened within the last X minutes):
      processOrders(d)
      //Then execute the strategy
      if(previousData.length>50 && (startDate.isEmpty || (startDate.isDefined && startDate.get.isBefore(d.localDateTime) ))) {
        strategy.process(d, previousData.toList)
      }
      previousData += d
    })
  }

  def processOrders(data: Data): Unit = {
    //Sell if the price exceed the target
    val sold = strategy.orderExecutor.getSellOrder().filter(order => data.high > order.value)
    sold.foreach(order => {
      println(s"Sell order closed: $order - C1=${strategy.currency1} - C2=${strategy.currency2}")
      strategy.orderExecutor.deleteSellOrder(order)
      strategy.onSellOrderClose(order)
    })

    //Buy if the price goes below the target
    val bought = strategy.orderExecutor.getBuyOrder().filter(order => data.low < order.value)
    bought.foreach(order => {
      println(s"Buy order closed: $order - C1=${strategy.currency1} - C2=${strategy.currency2}")
      strategy.orderExecutor.deleteBuyOrder(order)
      strategy.onBuyOrderClose(order)
    })
  }
}
