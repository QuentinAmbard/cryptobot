package bot.simulation.strategy

import bot.simulation.{Data, Order, OrderExecutor}


abstract class Strategy(var currency1: Double, var currency2: Double, var fees: Double ) {
  var orderExecutor: OrderExecutor = _
  def process(data: Data, previousData: List[Data])

  def onBuyOrderClose(order: Order): Unit = {
    currency1 -= order.amount * order.value
    currency2 += order.amount
  }

  def onSellOrderClose(order: Order): Unit = {
    currency1 += order.amount * order.value
    currency2 -= order.amount
  }

  def getAvailableFundC1(): Double = {
    val fund = orderExecutor.getBuyOrder().foldLeft(currency1)((sum, order) => sum - order.value * order.amount * (1+fees))
    if(fund <0){
      println(s"FUND < 0, shouldn't happen ($fund)")
    }
    fund
  }

  def getAvailableFundC2() = {
    val fund = orderExecutor.getSellOrder().foldLeft(currency2)((sum, order) => sum - order.value * order.amount * (1+fees))
    if(fund <0){
      println(s"FUND < 0, shouldn't happen ($fund)")
    }
    fund
  }
}