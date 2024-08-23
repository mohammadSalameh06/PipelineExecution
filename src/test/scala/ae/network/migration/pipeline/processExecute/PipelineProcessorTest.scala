package ae.network.migration.pipeline.processExecute

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import ae.network.migration.pipeline.models.{Pipeline, Stage, Job}
import scala.concurrent.{Future, ExecutionContext}

class PipelineProcessorTest extends AnyWordSpec with Matchers with ScalaFutures {

  implicit val ec: ExecutionContext = ExecutionContext.global

  "PipelineProcessor" should {

    "process a pipeline with multiple stages and return the results" in {
      // Mock JobExecution that always returns 1
      val mockJobExecution = new JobExecution {
        override def executeJob(job: Job)(implicit ec: ExecutionContext): Future[Int] = Future.successful(1)
      }

      // StageProcessor with the mocked JobExecution
      val stageProcessor = new StageProcessor(mockJobExecution)

      // PipelineProcessor with the StageProcessor
      val pipelineProcessor = new PipelineProcessor(stageProcessor)

      // Define a pipeline with two stages
      val pipeline = Pipeline(List(
        Stage("stage 1", isParallel = true, List(Job("job1", "JarFile.jar", Array("-jar"), "LOG/path"))),
        Stage("stage 2", isParallel = false, List(
          Job("job2", "JarFile.jar", Array("-i", "./input", "-o", "./output"), "LOG/path"),
          Job("job3+", "JarFile.jar", Array("-i", "./input", "-o", "./output"), "LOG/path")
        ))
      ))

      // Run the pipeline processing
      val resultFuture = pipelineProcessor.processPipeline(pipeline)

      // Assert the results
      whenReady(resultFuture) { result =>
        result shouldBe List(List(1), List(1, 1))
      }
    }
  }
}
