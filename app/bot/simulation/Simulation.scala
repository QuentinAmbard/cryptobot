package bot.simulation

import java.time.LocalDateTime

import bot.simulation.Simulation.closingPrice
import bot.simulation.strategy.VolatilityStrategy


object Simulation extends App {
  val data = DataLoader.loadData(DataLoader.XRP, DataLoader.USD, DataLoader.plot5Min)
  val strategy = new VolatilityStrategy(100000D, 0D, 0.0025)
  val backTest = new BackTest(data, strategy, startDate = Some(LocalDateTime.of(2017,5,16,0,0,0)))
  backTest.simulate()
  strategy.orderExecutor.getSellOrder().foreach(println(_))
  println(s"C1=${strategy.currency1}")
  println(s"C2=${strategy.currency2} (pending for sell)")
//  val closingPrice = 0.22736
  val closingPrice = data(data.length-1).middleOpenClose
  println(s"closingPrice=$closingPrice")
  val pendingValue = strategy.orderExecutor.getSellOrder().foldLeft(0D)((sum, order) => sum + order.amount*closingPrice)
  println(s"TOTAL C1 if selling all: ${strategy.currency1 + pendingValue}")
}