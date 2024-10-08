package ae.network.migration.pipeline.parser

import ae.network.migration.pipeline.models.{Job, Pipeline, Stage}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.circe.ParsingFailure

/**
 * Unit tests for the ParserJson class.
 * This class verifies the correctness of JSON parsing into a Pipeline object.
 */



class ParserJsonSpec extends AnyFlatSpec with Matchers {

  "ParserJson" should "correctly parse a valid JSON file into a Pipeline object" in {
    val parser = new ParserJson
    val filePath = "src/main/scala/Resources/P.Json" // Replace with actual test file path

    val pipeline = parser.parsePipeline(filePath)

    // Add assertions to verify the contents of the parsed Pipeline object
    println("Valid JSON parsed successfully.")
    pipeline.stages should not be empty
    pipeline.stages.head.name should be ("stage 1")
  }

  it should "throw a ParsingFailure exception for an invalid JSON file" in {
    val parser = new ParserJson
    val filePath = "src/main/scala/Resources/error.Json" // Replace with actual test file path

    val thrown = intercept[Exception] {
      parser.parsePipeline(filePath)
    }

    println("Invalid JSON parsing resulted in an exception.")
    thrown shouldBe a[ParsingFailure]
    thrown.getMessage should include ("Exception occurred while parsing JSON")
  }

  it should "throw a ParsingFailure exception for a non-existent JSON file" in {
    val parser = new ParserJson
    val filePath = "src/main/scala/Resources/non_existent_file.Json" // Non-existent file path

    val thrown = intercept[Exception] {
      parser.parsePipeline(filePath)
    }

    println("Non-existent file parsing resulted in an exception.")
    thrown shouldBe a[ParsingFailure]
    thrown.getMessage should include ("Exception occurred while parsing JSON")
  }

  it should "throw a ParsingFailure exception for a JSON file with invalid format" in {
    val parser = new ParserJson
    val filePath = "src/main/scala/Resources/invalid_format.Json" // Replace with a file that has invalid JSON format

    val thrown = intercept[Exception] {
      parser.parsePipeline(filePath)
    }

    println("Invalid JSON format parsing resulted in an exception.")
    thrown shouldBe a[ParsingFailure]
    thrown.getMessage should include ("Exception occurred while parsing JSON")
  }
}
