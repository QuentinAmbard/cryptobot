package bot.simulation

import java.time.{LocalDateTime, ZoneOffset}

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator

import scala.collection.Iterable
import scala.collection.mutable.ListBuffer
import scala.util.Random


case class Order(amount: Double, value: Double, creationValue: Double, creationData: Data, id: String = Random.alphanumeric.take(10).mkString(""), creationDate: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC))

trait OrderExecutor {
  def placeSellOrder(order: Order)

  def placeBuyOrder(order: Order)

  def getSellOrder(): Iterable[Order]

  def getBuyOrder(): Iterable[Order]

  def deleteAllBuyOrder(): Unit

  def deleteAllSellOrder(): Unit

  def deleteBuyOrder(order: Order): Unit

  def deleteSellOrder(order: Order): Unit

  def getHigerMarketSellBid(): Double

  def getLowerMarketBuyBid(): Double

}
