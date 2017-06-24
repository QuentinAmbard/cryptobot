package bot.simulation.strategy

import bot.simulation.{Data, Order}

/**
  * Created by quentin on 10/06/17.
  */
class VolatilityStrategyEma(c1: Double, c2: Double, f: Double) extends Strategy(c1, c2, f) {
  override def process(data: Data, previousData: List[Data]): Unit = {
    val lastData = previousData(previousData.length-1)
    orderExecutor.deleteAllBuyOrder()
    if(currency1>100+300+500+1000+2000) {
      println(s"C1=$currency1")
      orderExecutor.placeBuyOrder(Order(1000, lastData.low * 0.995, creationValue=lastData.middleOpenClose, creationData=lastData))
      orderExecutor.placeBuyOrder(Order(1000, lastData.low * 0.990, creationValue=lastData.middleOpenClose, creationData=lastData))
      orderExecutor.placeBuyOrder(Order(1000, lastData.low * 0.975, creationValue=lastData.middleOpenClose, creationData=lastData))
      orderExecutor.placeBuyOrder(Order(1000, lastData.low * 0.95, creationValue=lastData.middleOpenClose, creationData=lastData))
      orderExecutor.placeBuyOrder(Order(2000, lastData.low * 0.9, creationValue=lastData.middleOpenClose, creationData=lastData))
    } else {
      println("insufisent fund")
    }
  }

  override def onBuyOrderClose(order: Order): Unit = {
    super.onBuyOrderClose(order)
    //orderExecutor.placeSellOrder(Order(order.amount*0.3, order.creationValue*1.005, order.value, order.creationData))
    //orderExecutor.placeSellOrder(Order(order.amount*1, order.value*1.008, order.value, order.creationData))
    //orderExecutor.placeSellOrder(Order(order.amount*0.4, order.creationValue, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount*0.2, order.value*1.008, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount*0.2, order.value*1.01, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount*0.2, order.value*1.015, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount*0.2, order.value*1.02, order.value, order.creationData))
    orderExecutor.placeSellOrder(Order(order.amount*0.2, order.value*1.025, order.value, order.creationData))
  }

  override def onSellOrderClose(order: Order): Unit = {
    super.onSellOrderClose(order)
  }
}
