package utils

import java.nio.file.{Files, Path}

import model.{Position, ProblemFacts, Ride}

import scala.collection.JavaConverters._
import scala.collection.mutable

class FileReader(filePath: Path) {

  val lines: mutable.Buffer[String] = Files.readAllLines(filePath).asScala

  val headerLine = lines(0)

  private val splitLine: Array[String] = headerLine.split(" ").toArray
  val gridRows = splitLine(0).toInt
  val gridColumns = splitLine(1).toInt
  val nbrOfVehicles = splitLine(2).toInt
  val nbOfRides = splitLine(3).toInt
  val bonus = splitLine(4).toInt
  val steps = splitLine(5).toInt

  val facts = new ProblemFacts(
    gridLines = gridRows,
    gridColumns = gridColumns,
    nbOfCars = nbrOfVehicles,
    nbOfRides = nbOfRides,
    bonus = bonus,
    timeSteps = steps
  )

  val rides: mutable.Buffer[Ride] = lines.drop(1).zipWithIndex.map {
    case (l, idx) =>
      val splitLine = l.split(" ")
      new Ride(
        idx,
        Position(splitLine(0).toInt, splitLine(1).toInt),
        Position(splitLine(2).toInt, splitLine(3).toInt),
        splitLine(4).toInt,
        splitLine(5).toInt)
  }
  rides
}
