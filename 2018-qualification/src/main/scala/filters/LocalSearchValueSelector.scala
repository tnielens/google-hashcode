package filters

import model.{CarFreeUp, Ride, RidePlan}
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter
import org.optaplanner.core.impl.score.director.ScoreDirector

class LocalSearchValueSelector extends SelectionFilter[RidePlan, CarFreeUp] {
  override def accept(scoreDirector: ScoreDirector[RidePlan], selection: CarFreeUp): Boolean = selection match {
    case r: Ride if r.car.id != -1 => true
    case _ => false
  }
}
