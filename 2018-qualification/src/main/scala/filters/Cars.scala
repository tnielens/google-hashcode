package filters

import model.{Car, CarFreeUp, Ride, RidePlan}
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter
import org.optaplanner.core.impl.score.director.ScoreDirector

class Cars extends SelectionFilter[RidePlan, CarFreeUp] {
  override def accept(scoreDirector: ScoreDirector[RidePlan], selection: CarFreeUp): Boolean = {
    selection.isInstanceOf[Car]
  }
}
