package model
import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable

import scala.beans.BeanProperty

class Car extends CarFreeUp {
  override def carAvailabilityPosition = Position(0, 0)
  override def carAvailabilityTime = 0

  @BeanProperty
  var id: Integer = null

  var nextRide: Ride = null
  def getNextRide: Ride = nextRide
  def setNextRide(newRide: Ride): Unit = this.nextRide = newRide

  def canEqual(other: Any): Boolean = other.isInstanceOf[Car]
  override def equals(other: Any): Boolean = other match {
    case that: Car =>
      (that canEqual this) &&
        id == that.id
    case _ => false
  }
}

object Car {
  val DummyCar: Car = {
    val c = new Car()
    c.id = -1
    c
  }
}
