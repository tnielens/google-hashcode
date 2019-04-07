package model

import java.util

import org.optaplanner.core.api.domain.solution.drools.{ProblemFactCollectionProperty, ProblemFactProperty}
import org.optaplanner.core.api.domain.solution.{PlanningEntityCollectionProperty, PlanningScore, PlanningSolution}
import org.optaplanner.core.api.domain.valuerange.{CountableValueRange, ValueRangeFactory, ValueRangeProvider}
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/**
  * Copyright (C) 16.02.19 - REstore NV
  */
@PlanningSolution //(lookUpStrategyType = LookUpStrategyType.EQUALITY)
class RidePlan {

  def this(rides: util.List[Ride], facts: ProblemFacts) {
    this()
    this.rides = rides
    this.facts = facts
    val problemCars =  Car.DummyCar +: (for (i <- 0 until facts.nbOfCars) yield {
      val car = new Car()
      car.id = i
      car
    })
      this.cars = problemCars.asJava
  }

  @BeanProperty
  @PlanningEntityCollectionProperty
  @ValueRangeProvider(id = "rides")
  var rides: util.List[Ride] = null

  @BeanProperty
  @PlanningEntityCollectionProperty
  @ValueRangeProvider(id = "cars")
  var cars: util.List[Car] = null

  @ProblemFactProperty
  var facts: ProblemFacts = null

  @PlanningScore
  @BeanProperty
  var score: HardSoftLongScore = null

  override def toString: String = s"Rides involved in plan: ${rides}"
}
