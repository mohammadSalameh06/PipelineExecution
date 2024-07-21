package ae.network.migration.pipeline

import ae.network.migration.pipeline.models.{Pipeline, Stage, Job}
import ae.network.migration.pipeline.parser.ParserJson
import ae.network.migration.pipeline.processExecute.{JobExecution, StageProcessor, PipelineProcessor}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

/**
 * The Main object contains the entry point for the pipeline processing application.
 */
object Main {

  /**
   * The main method is the entry point of the application. It takes a single argument,
   * which is the filename of the JSON configuration file.
   *
   * @param args Command line arguments where the first argument is the filename.
   */
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("Usage: Main <filename>")
      System.exit(1)
    }

    val fileName = args(0)
    println(s"Parsing file: $fileName")

    // Instantiate the JSON parser
    val parser = new ParserJson

    // Parse the pipeline configuration
    val pipeline = parser.parsePipeline(fileName)

    // Instantiate the job execution and processing components
    val jobExecution = new JobExecution
    val stageProcessor = new StageProcessor(jobExecution)
    val pipelineProcessor = new PipelineProcessor(stageProcessor)

    // Print the stages for debugging purposes
    pipeline.stages.foreach(println)

    println("\n")

    // Process the pipeline and handle the result
    pipelineProcessor.processPipeline(pipeline).onComplete {
      case Success(results) =>
        println(s"Pipeline executed successfully with results: $results")
      case Failure(exception) =>
        println(s"Pipeline execution failed with exception: ${exception.getMessage}")
    }

    // Prevent the application from exiting immediately to allow async processing to complete
    Thread.sleep(5000)
  }
}
