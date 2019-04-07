package model
import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable

import scala.beans.BeanProperty

@PlanningEntity
trait CarFreeUp {
  def carAvailabilityPosition: Position
  def getCarAvailabilityPosition: Position = carAvailabilityPosition
  def carAvailabilityTime: Integer
  def getCarAvailabilityTime: Integer = carAvailabilityTime

  @InverseRelationShadowVariable(sourceVariableName = "previousRideCarAvailability")
  def getNextRide: Ride
  def setNextRide(ride: Ride): Unit
}
