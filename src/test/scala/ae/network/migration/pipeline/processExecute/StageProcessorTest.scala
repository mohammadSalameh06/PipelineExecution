package ae.network.migration.pipeline.processExecute

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import ae.network.migration.pipeline.models.{Stage, Job}
import scala.concurrent.{Future, ExecutionContext}

class StageProcessorTest extends AnyWordSpec with Matchers with ScalaFutures {

  implicit val ec: ExecutionContext = ExecutionContext.global

  "StageProcessor" should {

    "process a parallel stage and return results in parallel" in {
      // Manually mock JobExecution
      val mockJobExecution = mock(classOf[JobExecution])

      // Mock the behavior for each job execution
      when(mockJobExecution.executeJob(any[Job])(any[ExecutionContext]))
        .thenReturn(Future.successful(4))

      val stageProcessor = new StageProcessor(mockJobExecution)

      val stage = Stage("stage 1", isParallel = true, List(
        Job("job1", "JarFile.jar", Array("-jar"), "LOG/path"),
        Job("job2", "JarFile.jar", Array("-jar"), "LOG/path")
      ))

      val resultFuture = stageProcessor.processStage(stage)

      // Assert the results
      whenReady(resultFuture) { result =>
        result shouldBe List(4, 4)
      }

      // Verify that executeJob was called twice
      verify(mockJobExecution, times(2)).executeJob(any[Job])(any[ExecutionContext])
    }

    "process a sequential stage and return results sequentially" in {
      // Manually mock JobExecution
      val mockJobExecution = mock(classOf[JobExecution])

      // Mock the behavior for each job execution
      when(mockJobExecution.executeJob(any[Job])(any[ExecutionContext]))
        .thenReturn(Future.successful(4))

      val stageProcessor = new StageProcessor(mockJobExecution)

      val stage = Stage("stage 2", isParallel = false, List(
        Job("job1", "JarFile.jar", Array("-jar"), "LOG/path"),
        Job("job2", "JarFile.jar", Array("-jar"), "LOG/path")
      ))

      val resultFuture = stageProcessor.processStage(stage)

      // Assert the results
      whenReady(resultFuture) { result =>
        result shouldBe List(4, 4)
      }

      // Verify that executeJob was called twice
      verify(mockJobExecution, times(2)).executeJob(any[Job])(any[ExecutionContext])
    }

    "handle failures in parallel stage processing" in {
      // Manually mock JobExecution
      val mockJobExecution = mock(classOf[JobExecution])

      // Mock the behavior: first job succeeds, second job fails
      when(mockJobExecution.executeJob(any[Job])(any[ExecutionContext]))
        .thenReturn(Future.successful(4))
        .thenReturn(Future.failed(new RuntimeException("Job failed")))

      val stageProcessor = new StageProcessor(mockJobExecution)

      val stage = Stage("stage 3", isParallel = true, List(
        Job("job1", "JarFile.jar", Array("-jar"), "LOG/path"),
        Job("job2", "JarFile.jar", Array("-jar"), "LOG/path")
      ))

      val resultFuture = stageProcessor.processStage(stage)

      // Assert that the future fails
      whenReady(resultFuture.failed) { ex =>
        ex shouldBe a[RuntimeException]
        ex.getMessage should include("Job failed")
      }

      // Verify that executeJob was called twice
      verify(mockJobExecution, times(2)).executeJob(any[Job])(any[ExecutionContext])
    }

    "handle failures in sequential stage processing" in {
      // Manually mock JobExecution
      val mockJobExecution = mock(classOf[JobExecution])

      // Mock the behavior: first job succeeds, second job fails
      when(mockJobExecution.executeJob(any[Job])(any[ExecutionContext]))
        .thenReturn(Future.successful(4))
        .thenReturn(Future.failed(new RuntimeException("Job failed")))

      val stageProcessor = new StageProcessor(mockJobExecution)

      val stage = Stage("stage 4", isParallel = false, List(
        Job("job1", "JarFile.jar", Array("-jar"), "LOG/path"),
        Job("job2", "JarFile.jar", Array("-jar"), "LOG/path")
      ))

      val resultFuture = stageProcessor.processStage(stage)

      // Assert that the future fails
      whenReady(resultFuture.failed) { ex =>
        ex shouldBe a[RuntimeException]
        ex.getMessage should include("Job failed")
      }

      // Verify that executeJob was called twice
      verify(mockJobExecution, times(2)).executeJob(any[Job])(any[ExecutionContext])
    }
  }
}
