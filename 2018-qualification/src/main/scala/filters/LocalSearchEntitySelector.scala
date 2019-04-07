package filters

import model.{Car, CarFreeUp, Ride, RidePlan}
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter
import org.optaplanner.core.impl.score.director.ScoreDirector

class LocalSearchEntitySelector extends SelectionFilter[RidePlan, CarFreeUp] {
  override def accept(scoreDirector: ScoreDirector[RidePlan], selection: CarFreeUp): Boolean = selection match {
    case r: Ride if r.previousRideCarAvailability == Car.DummyCar => true
    case r: Ride if r.car != -1 => true
    case _ => false
  }
}
