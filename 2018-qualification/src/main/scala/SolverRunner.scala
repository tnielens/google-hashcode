import java.nio.file.{Files, Path, Paths}

import model.{Car, RidePlan}
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore
import org.optaplanner.core.api.solver.SolverFactory
import score.ScoreCalculator
import utils.FileReader

import scala.collection.JavaConverters._
import scala.collection.parallel.immutable.ParSeq

/**
  * Copyright (C) 2/13/19 - REstore NV)
  */
object SolverRunner extends App {

  val inputFiles: List[Path] = Files
    .newDirectoryStream(Paths.get("input"))
    .asScala
    .toList
    .filterNot(_.toString.contains("example"))
    .filterNot(_.toString.contains("easy"))

  private val ridePlans = inputFiles.par.map {file =>
    val fr = new FileReader(file)
    println(s"Processing file $file")
    val unsolvedProblem = new RidePlan(fr.rides.asJava, fr.facts)

    val solverFactory: SolverFactory[RidePlan] =
      SolverFactory.createFromXmlResource("SolverConfig.xml")
    val solver = solverFactory.buildSolver


    val solvedProblem = solver.solve(unsolvedProblem)
    println(s"#vehicules in car null: ${solvedProblem.rides.asScala.count(r => r.car == null)}")
    println(s"#vehicules in car -1: ${solvedProblem.rides.asScala.count(r => r.car == Car.DummyCar)}")
    //println(s"scorer V1: ${ScoreCalculator.scoreV1(solvedProblem)}")
    println(s"scorer V2: ${ScoreCalculator.scoreV2(solvedProblem)}")
    //println(s"scorer V3: ${ScoreCalculator.scoreV3(solvedProblem)}")
    solvedProblem
  }

  val scores = ridePlans.map(_.score)
  private val maxSoftScore = ridePlans.map(r => {
    r.rides.size() * r.facts.bonus + r.rides.asScala.map(_.distance.toInt).sum
  }).sum
  println(s"maxt soft score sum:${maxSoftScore}")
  println(s"soft score sum:${maxSoftScore + scores.map(_.getSoftScore).sum}")
}

