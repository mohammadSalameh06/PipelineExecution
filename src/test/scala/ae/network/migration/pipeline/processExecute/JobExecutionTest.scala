package ae.network.migration.pipeline.processExecute

import ae.network.migration.pipeline.models.Job
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.mockito.Mockito.*

import scala.concurrent.{ExecutionContext, Future}

/**
 * Unit tests for the JobExecution class.
 * This class verifies the behavior of job execution
 */
class JobExecutionTest extends AnyWordSpec with Matchers with ScalaFutures {

  // Implicit execution context for handling futures
  implicit val ec: ExecutionContext = ExecutionContext.global

  /**
   * Test case for successful job execution.
   *
   * This test simulates a successful job execution by mocking the Process class.
   * It verifies that the executeJob method returns an exit value of 0.
   */
  "JobExecution" should {

    "return exit value 0 for a successful job execution" in {
      // Mock the process to simulate a successful job execution
      val mockProcess = mock(classOf[Process])
      when(mockProcess.exitValue()).thenReturn(0)
      val jobExecution = new JobExecution

      val job = Job("testJob", "JarFile.jar", Array("-i", "input", "-o", "output"), "LOG/path")
      val resultFuture = jobExecution.executeJob(job)

      whenReady(resultFuture) { exitValue =>
        exitValue shouldBe 0
      }
    }

    /**
     * Test case for failed job execution.
     *
     * This test simulates a failed job execution by mocking the Process class.
     * It verifies that the executeJob method throws a RuntimeException with an appropriate message.
     */
    "throw an exception for a failed job execution" in {
      // Mock the process to simulate a failed job execution
      val mockProcess = mock(classOf[Process])
      when(mockProcess.exitValue()).thenReturn(1)
      val jobExecution = new JobExecution

      val job = Job("testJob", "JarFile.jar", Array("-i", "input", "-o", "output"), "LOG/path")
      val resultFuture = jobExecution.executeJob(job)

      whenReady(resultFuture.failed) { ex =>
        ex shouldBe a[RuntimeException]
        ex.getMessage should include ("Job failed with exit code 1")
      }
    }
  }
}
