package ae.network.migration.pipeline.processExecute

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import ae.network.migration.pipeline.models.{Stage, Job}
import scala.concurrent.{Future, ExecutionContext}


/**
 * Mockito was used to create mock objects and control the behavior of dependencies in isolation. This allows us to focus on testing
 * the logic within the StageProcessor class without relying on the actual implementation of external classes like JobExecution.
 * By mocking these dependencies, we can simulate different scenarios and outcomes, such as successful job execution or failures,
 * and verify that the StageProcessor handles them correctly.
 *
 *   val mockJobExecution = mock(classOf[JobExecution])
 */

/**
 * Unit tests for the StageProcessor class.
 * This class verifies the behavior of stage processing in both parallel and sequential stages,
 * as well as handling of job execution failures.
 */
class StageProcessorTest extends AnyWordSpec with Matchers with ScalaFutures {

  // Implicit execution context for handling futures
  implicit val ec: ExecutionContext = ExecutionContext.global

  /**
   * Test case for processing a parallel stage.
   *
   * This test simulates processing a parallel stage where each job execution is mocked to return
   * a specific result. It verifies that the processStage method correctly returns results in parallel.
   */
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

    /**
     * Test case for processing a sequential stage.
     *
     * This test simulates processing a sequential stage where each job execution is mocked to return
     * a specific result. It verifies that the processStage method correctly returns results sequentially.
     */
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

    /**
     * Test case for handling failures in parallel stage processing.
     *
     * This test simulates a scenario where one job in a parallel stage fails. It verifies that
     * the processStage method correctly handles the failure by returning a failed Future.
     */
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

    /**
     * Test case for handling failures in sequential stage processing.
     *
     * This test simulates a scenario where one job in a sequential stage fails. It verifies that
     * the processStage method correctly handles the failure by returning a failed Future.
     */
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
