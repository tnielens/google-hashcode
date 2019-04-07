import java.nio.file.Paths

import model.RidePlan
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory
import utils.FileReader
import collection.JavaConverters._

/**
  * Copyright (C) 2/13/19 - REstore NV)
  */
object BenchmarkSolverRunner extends App {


  val solverFactory = PlannerBenchmarkFactory.createFromXmlResource("BenchmarkSolverConfig.xml")
  val fr = new FileReader(Paths.get("input/a_example.in"))
  val p1 = new RidePlan(fr.rides.asJava, fr.facts)

  val solver = solverFactory.buildPlannerBenchmark(p1)
  solver.benchmark()
}
