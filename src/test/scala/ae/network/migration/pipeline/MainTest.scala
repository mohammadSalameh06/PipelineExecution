//package ae.network.migration.pipeline
//
//import org.scalatest.wordspec.AnyWordSpec
//import org.scalatest.matchers.should.Matchers
//import org.mockito.Mockito._
//import org.mockito.ArgumentMatchers._
//import org.mockito.Mockito
//import scala.concurrent.Future
//import ae.network.migration.pipeline.parser.ParserJson
//import ae.network.migration.pipeline.processExecute.{PipelineProcessor}
//import ae.network.migration.pipeline.models.Pipeline
//
//class MainTest extends AnyWordSpec with Matchers {
//
//  "Main" should {
//
//    "parse the file and start the pipeline processing successfully" in {
//      // Mock dependencies
//      val mockParser = Mockito.mock(classOf[ParserJson])
//      val mockPipeline = Mockito.mock(classOf[Pipeline])
//      val mockPipelineProcessor = Mockito.mock(classOf[PipelineProcessor])
//
//      // Mock the behavior of the ParserJson
//      when(mockParser.parsePipeline(any[String])).thenReturn(mockPipeline)
//
//      // Mock the behavior of the PipelineProcessor
//      when(mockPipelineProcessor.processPipeline(any[Pipeline])).thenReturn(Future.successful("Success"))
//
//      // Inject the mocks into the Main object
//      Main.parser = mockParser
//      Main.pipelineProcessor = mockPipelineProcessor
//
//      // Simulate command line arguments
//      val args = Array("test-file.json")
//
//      // Call the main method
//      Main.main(args)
//
//      // Verify that the parser and pipeline processor were called with the expected arguments
//      verify(mockParser).parsePipeline("test-file.json")
//      verify(mockPipelineProcessor).processPipeline(mockPipeline)
//    }
//
//    "handle pipeline processing failure" in {
//      // Mock dependencies
//      val mockParser = Mockito.mock(classOf[ParserJson])
//      val mockPipeline = Mockito.mock(classOf[Pipeline])
//      val mockPipelineProcessor = Mockito.mock(classOf[PipelineProcessor])
//
//      // Mock the behavior of the ParserJson
//      when(mockParser.parsePipeline(any[String])).thenReturn(mockPipeline)
//
//      // Mock the behavior of the PipelineProcessor to simulate failure
//      val exception = new RuntimeException("Processing failed")
//      when(mockPipelineProcessor.processPipeline(any[Pipeline])).thenReturn(Future.failed(exception))
//
//      // Inject the mocks into the Main object
//      Main.parser = mockParser
//      Main.pipelineProcessor = mockPipelineProcessor
//
//      // Simulate command line arguments
//      val args = Array("test-file.json")
//
//      // Call the main method
//      Main.main(args)
//
//      // Verify that the parser and pipeline processor were called with the expected arguments
//      verify(mockParser).parsePipeline("test-file.json")
//      verify(mockPipelineProcessor).processPipeline(mockPipeline)
//    }
//
//    "require exactly one argument" in {
//      // Redirect standard error to capture System.exit calls
//      val originalErr = System.err
//      val baos = new java.io.ByteArrayOutputStream()
//      val ps = new java.io.PrintStream(baos)
//      System.setErr(ps)
//
//      // Call the main method with no arguments
//      intercept[SecurityException] {
//        Main.main(Array.empty[String])
//      }
//
//      // Reset standard error
//      System.setErr(originalErr)
//      val output = baos.toString
//
//      // Verify the output contains the expected usage message
//      output should include("Usage: Main <filename>")
//    }
//  }
//}
