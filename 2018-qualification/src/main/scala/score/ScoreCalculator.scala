package score

import model.{Car, Position, Ride, RidePlan}
import org.optaplanner.core.api.score.Score
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator

import collection.JavaConverters._
import scala.math.abs

class ScoreCalculator extends EasyScoreCalculator[RidePlan] {
  override def calculateScore(solution: RidePlan): HardSoftLongScore = ???
}

object ScoreCalculator {
  def scoreV3(ridePlan: RidePlan): Int = {
    val rides = ridePlan.rides.asScala.filter(r => r.car != null && r.car.id != -1)
    val rideScores = rides.map { ride =>
      import ride._
      val earliestFeasibleStartTime = previousRideCarAvailability.carAvailabilityTime + Position
        .distance(previousRideCarAvailability.carAvailabilityPosition, start)
      val endTime = pickUpTime + distance
      if (earliestFeasibleStartTime > pickUpTime) {
        -1
      } else if (endTime > latest) {
        -1
      } else if (pickUpTime == earliest) {
        distance + ridePlan.facts.bonus
      } else {
        distance.toInt
      }
    }.toVector
    rideScores.sum
  }


  def scoreV1(ridePlan: RidePlan): HardSoftLongScore = {
    def evaluate(ridePlan: RidePlan): HardSoftLongScore = {
    val scores = ridePlan.rides.asScala
      .filter(r => r.car != null && r.car.id != -1 && r.pickUpTime != null)
      .groupBy(_.car.id)
      .mapValues(_.sortBy(_.pickUpTime))
      .toSeq
      .map(e => vehicleScore(e._1, e._2, ridePlan))
      .toList

    HardSoftLongScore.of(scores.map(_._1).sum, scores.map(_._2).sum)
  }

    def vehicleScore(
      vehicleId: Int,
      vehiculeRides: Seq[Ride],
      ridePlan: RidePlan): Tuple2[Int, Int] = {
    var step = 0
    var currentScore = 0
    var currentConstraintBreak = 0
    var currentPosition = Position(0, 0)

    for (ride <- vehiculeRides) {
      step += Position.distance(currentPosition, ride.start)
      if (step > ride.pickUpTime) currentConstraintBreak += ride.pickUpTime - step
      if (ride.pickUpTime == ride.earliest) currentScore += ridePlan.facts.bonus
      step = ride.pickUpTime
      step += ride.distance
      currentScore += ride.distance
      if (step > ride.latest) currentConstraintBreak += ride.latest - step
      currentPosition = ride.end
    }

    Tuple2(currentConstraintBreak, currentScore)
  }

    evaluate(ridePlan)
  }

  def scoreV2(ridePlan: RidePlan): HardSoftLongScore = {

    val rides = ridePlan.rides.asScala
    val ridePairOverlapSum: Integer = (for {
      ride1 <- rides if ride1.car != null && ride1.car.id != -1
      ride2 <- rides if ride2.car != null && ride2.car.id == ride1.car.id && ride2.id > ride1.id
    } yield Ride.overlap(ride1, ride2).toInt).sum
    println(s"ridePairOverlap $ridePairOverlapSum")

    val rideFromOriginOverlap: Integer = rides
      .filter(r => r.car != null && r.car.id >= 0)
      .map(r => r.distanceToOrigin - r.pickUpTime)
      .filter(_ > 0)
      .sum
    println(s"rideFromOriginOverlap $rideFromOriginOverlap")

    val bonusSum = rides
      .filter(r => r.car != null && r.car.id >= 0)
      .count(r => r.pickUpTime == r.earliest) * ridePlan.facts.bonus
    println(s"bonusSum $bonusSum")


    val distanceSum = rides
      .filter(r => r.car != null && r.car.id >= 0)
      .map(_.distance.toInt).sum
    println(s"distanceSum $distanceSum")

    HardSoftLongScore.of(-ridePairOverlapSum -rideFromOriginOverlap, bonusSum + distanceSum)
  }

}
