package ae.network.migration.pipeline.parser

import ae.network.migration.pipeline.models.{Job, Pipeline, Stage}
import io.circe.{Decoder, ParsingFailure}
import io.circe.generic.semiauto.deriveDecoder
import io.circe.jawn.decode

import scala.io.Source
import scala.util.Using

class ParserJson {
  implicit val jobDecoder: Decoder[Job] = deriveDecoder[Job]
  implicit val stageDecoder: Decoder[Stage] = deriveDecoder[Stage]
  implicit val pipelineDecoder: Decoder[Pipeline] = deriveDecoder[Pipeline]

  def parsePipeline(filePath: String): Pipeline = {
    val result = Using(Source.fromFile(filePath)) { source =>
      val json = source.getLines().mkString
      decode[Pipeline](json)
    }.recover {
      case ex: Exception =>
        println(s"Exception occurred while parsing JSON: ${ex.getMessage}")
        throw ParsingFailure(s"Exception occurred while parsing JSON: ${ex.getMessage}", ex)
    }.get

    result match {
      case Right(pipeline) => pipeline
      case Left(error) => throw ParsingFailure(s"Failed to parse JSON: $error", new Exception(s"Failed to parse JSON: $error"))
    }
  }
}
