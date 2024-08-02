package ae.network.migration.pipeline.processExecute

import ae.network.migration.pipeline.models.Job
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.*

import scala.concurrent.{ExecutionContext, Future}

class JobExecutionTest extends AnyWordSpec with Matchers with ScalaFutures {

  implicit val ec: ExecutionContext = ExecutionContext.global

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
