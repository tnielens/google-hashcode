package model
import org.optaplanner.core.impl.domain.variable.listener.VariableListener
import org.optaplanner.core.impl.score.director.ScoreDirector
import scala.math._

import scala.annotation.tailrec

class PickUpTimeUpdatingVariableListener extends VariableListener[Ride] {
  override def beforeEntityAdded(scoreDirector: ScoreDirector[_], entity: Ride): Unit = {}
  override def afterEntityAdded(scoreDirector: ScoreDirector[_], entity: Ride): Unit =
    updatePickUpTime(scoreDirector, entity)
  override def beforeVariableChanged(scoreDirector: ScoreDirector[_], entity: Ride): Unit = {}
  override def afterVariableChanged(scoreDirector: ScoreDirector[_], entity: Ride): Unit =
    updatePickUpTime(scoreDirector, entity)
  override def beforeEntityRemoved(scoreDirector: ScoreDirector[_], entity: Ride): Unit = {}
  override def afterEntityRemoved(scoreDirector: ScoreDirector[_], entity: Ride): Unit =
    updatePickUpTime(scoreDirector, entity)

  import org.optaplanner.core.impl.score.director.ScoreDirector

  @tailrec
  final protected def updatePickUpTime(scoreDirector: ScoreDirector[_], ride: Ride): Unit = {
    val previousCarAvailability = ride.previousRideCarAvailability
    if (previousCarAvailability != null) {
      if (ride.car != Car.DummyCar) {
        val newPickUp: Integer = max(
          previousCarAvailability.carAvailabilityTime + Position
            .distance(previousCarAvailability.carAvailabilityPosition, ride.start),
          ride.earliest)
        if (newPickUp != ride.pickUpTime) {
          scoreDirector.beforeVariableChanged(ride, "pickUpTime")
          ride.setPickUpTime(newPickUp)
          scoreDirector.afterVariableChanged(ride, "pickUpTime")
          if (ride.nextRide != null) {
            updatePickUpTime(scoreDirector, ride.nextRide)
          }
        }
      }
    }
  }
}
