package model

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.variable._

import scala.beans.BeanProperty
import scala.math.abs

@PlanningEntity(difficultyComparatorClass = classOf[RideComparator])
class Ride()
    extends CarFreeUp {

  def this(id: Integer, start: Position, end: Position, earliest: Integer, latest: Int) {
    this()
    this.id = id
    this.start = start
    this.end = end
    this.earliest = earliest
    this.latest = latest
  }

  var id: Integer = null
  var start: Position = null
  var end: Position = null
  var earliest: Integer = null
  var latest: Integer = null

  def distance: Integer = Position.distance(start, end)
  def distanceToOrigin: Integer = start.row + start.column

  var nextRide: Ride = null
  override def getNextRide: Ride = nextRide
  override def setNextRide(ride: Ride): Unit = this.nextRide = ride


  var previousRideCarAvailability: CarFreeUp = null
  @PlanningVariable(
    graphType = PlanningVariableGraphType.CHAINED,
    valueRangeProviderRefs = Array("cars", "rides"))
  def getPreviousRideCarAvailability: CarFreeUp = previousRideCarAvailability
  def setPreviousRideCarAvailability(carFreeUp: CarFreeUp): Unit = this.previousRideCarAvailability = carFreeUp

  @BeanProperty
  @CustomShadowVariable(
    variableListenerClass = classOf[PickUpTimeUpdatingVariableListener],
    sources = Array(
      new PlanningVariableReference(variableName = "previousRideCarAvailability"),
      new PlanningVariableReference(variableName = "car"))
  )
  var pickUpTime: Integer = null

  var car: Car = null
  @AnchorShadowVariable(sourceVariableName = "previousRideCarAvailability")
  def getCar: Car = car
  def setCar(car: Car): Unit = this.car = car

  def overrun: Integer = {
    val v = (pickUpTime + distance) - latest
    v
  }

  // CarAvailability impl
  override def carAvailabilityPosition: Position = end
  override def carAvailabilityTime: Integer =
    if (pickUpTime != null) {
      distance + pickUpTime
    } else {
      null
    }
}

object Ride {
  def overlap(a: Ride, b: Ride): Integer = {
    def overlapWithOrderedRides(a: Ride, b: Ride): Integer = {
      val earliestFeasiblePickTimeForB = a.pickUpTime + a.distance + Position.distance(a.end, b.start)
      val overlap = earliestFeasiblePickTimeForB - b.pickUpTime
      if (overlap > 0) {
        overlap
      } else {
        0
      }
    }

    if (a.pickUpTime < b.pickUpTime) {
      overlapWithOrderedRides(a, b)
    } else {
      overlapWithOrderedRides(b, a)
    }
  }
}
