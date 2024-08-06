package ae.network.migration.pipeline
import ae.network.migration.pipeline.models.{Pipeline, Job}
import ae.network.migration.pipeline.parser.ParserJson
import ae.network.migration.pipeline.processExecute.{JobExecution, StageProcessor, PipelineProcessor}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

object Main {

  var parser: ParserJson = new ParserJson
  var pipelineProcessor: PipelineProcessor = new PipelineProcessor(new StageProcessor(new JobExecution))

  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("Usage: Main <filename>")
      System.exit(1)
    }

    val fileName = args(0)
    println(s"Parsing file: $fileName")

    // Parse the pipeline configuration
    val pipeline = parser.parsePipeline(fileName)

    // Print the stages for debugging purposes,
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
