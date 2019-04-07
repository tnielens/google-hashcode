package model
import scala.beans.BeanProperty

/**
  * Copyright (C) 16.02.19 - REstore NV
  */
class ProblemFacts(
    val gridLines: Int,
    val gridColumns: Int,
    val nbOfCars: Int,
    val nbOfRides: Int,
    val timeSteps: Int,
    val bonus: Int
)
