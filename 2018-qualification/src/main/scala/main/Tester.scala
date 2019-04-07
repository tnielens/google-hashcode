package main

import model.{Position, ProblemFacts, Ride, RidePlan}

import scala.collection.JavaConverters._

object Tester extends App {

  val ride1 = new Ride(0, Position(0, 0), Position(1, 3), 2, 9)
  ride1.pickUpTime = 2
  val ride2 = new Ride(1, Position(1, 2), Position(1, 0), 0, 9)
  ride2.pickUpTime = 5
  val ride3 = new Ride(2, Position(2, 0), Position(2, 2), 0, 9)
  ride3.pickUpTime = 2

  val rides = List(
    ride1,
    ride2,
    ride3
  )

  //println(new ScoreCalculator().evaluate(new RidePlan(rides.asJava, new ProblemFacts(3, 4, 4, 3, 10, 2))))
}
