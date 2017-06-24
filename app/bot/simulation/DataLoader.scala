package bot.simulation

import java.io.InputStreamReader

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._

object DataLoader extends {
  sealed class Currency(val value: String)
  case object XRP extends Currency("XRP")
  case object USD extends Currency("USD")
  case object BTC extends Currency("BTC")

  sealed class Precision(val value: String)
  case object plot5Min extends Precision("5min")

  def loadData(currency1: Currency, currency2: Currency, precision: Precision): List[Data] = {
    val c: Config = ConfigFactory.parseReader(new InputStreamReader(getClass.getResourceAsStream(s"/data/${currency1.value}${currency2.value}-${precision.value}.js"))).withFallback(ConfigFactory.load())
    c.as[List[List[Double]]]("data").map(new Data(_))
  }

}
