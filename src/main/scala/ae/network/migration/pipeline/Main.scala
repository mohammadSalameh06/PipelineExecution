package ae.network.migration.pipeline

import ae.network.migration.pipeline.models.{Pipeline, Stage, Job}
import ae.network.migration.pipeline.parser.ParserJson
import ae.network.migration.pipeline.processExecute.{JobExecution, StageProcessor, PipelineProcessor}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("Usage: Main <filename>")
      System.exit(1)
    }
    val fileName = args(0)
    println(s"Parsing file: $fileName")

    val parser = new ParserJson
    val pipeline = parser.parsePipeline(fileName)

    val jobExecution = new JobExecution
    val stageProcessor = new StageProcessor(jobExecution)
    val pipelineProcessor = new PipelineProcessor(stageProcessor)

    pipeline.stages.foreach(println)

    println("\n")
    pipelineProcessor.processPipeline(pipeline).onComplete {
      case Success(results) =>
        println(s"Pipeline executed successfully with results: $results")
      case Failure(exception) =>
        println(s"Pipeline execution failed with exception: ${exception.getMessage}")
    }

    Thread.sleep(5000)
  }
}
