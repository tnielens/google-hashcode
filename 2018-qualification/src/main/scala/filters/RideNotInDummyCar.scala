package filters

import model.{CarFreeUp, Ride, RidePlan}
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter
import org.optaplanner.core.impl.score.director.ScoreDirector

class RideNotInDummyCar extends SelectionFilter[RidePlan, CarFreeUp] {
  override def accept(scoreDirector: ScoreDirector[RidePlan], selection: CarFreeUp): Boolean = selection match {
    case r: Ride if r.car == null || r.car.id != -1 => true
    case _ => false
  }
}
