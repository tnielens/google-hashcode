package filters

import model.{Car, CarFreeUp, Ride, RidePlan}
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter
import org.optaplanner.core.impl.score.director.ScoreDirector

class DummyCar extends SelectionFilter[RidePlan, CarFreeUp] {
  override def accept(scoreDirector: ScoreDirector[RidePlan], selection: CarFreeUp): Boolean = selection match {
    case c: Car if c.id == -1 => true
    case _ => false
  }
}
