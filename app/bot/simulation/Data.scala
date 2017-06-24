package bot.simulation

import java.time.{Instant, LocalDateTime, ZoneOffset}

case class Data(localDateTime: LocalDateTime, open: Double, high: Double, low: Double, close: Double) {
  def this(list: List[Double]) = this(LocalDateTime.ofInstant(Instant.ofEpochSecond(list(0).toLong), ZoneOffset.UTC), list(1), list(2), list(3), list(4))
  def middleOpenClose = Math.max(open, close) + Math.abs(open-close)/2
  def middle = low + (high-low)/2


}
