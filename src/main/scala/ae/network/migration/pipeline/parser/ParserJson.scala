package ae.network.migration.pipeline.parser

import ae.network.migration.pipeline.models.{Job, Pipeline, Stage}
import io.circe.{Decoder, ParsingFailure}
import io.circe.generic.semiauto.deriveDecoder
import io.circe.jawn.decode

import scala.io.Source
import scala.util.{Try, Using}

class ParserJson {
  implicit val jobDecoder: Decoder[Job] = deriveDecoder[Job]
  implicit val stageDecoder: Decoder[Stage] = deriveDecoder[Stage]
  implicit val pipelineDecoder: Decoder[Pipeline] = deriveDecoder[Pipeline]

  def parsePipeline(filePath: String): Pipeline = {

    val resourcePath = filePath

    val resource = getClass.getClassLoader.getResource(resourcePath)
    if (resource == null) {
      throw ParsingFailure(s"Resource $resourcePath not found", new Exception(s"Resource $resourcePath not found"))
    } else {
      val result = Using(Source.fromURL(resource)) { source =>
        val json = source.getLines().mkString
        decode[Pipeline](json)
      }.recover {
        case ex: Exception =>
          println(s"Exception occurred: ${ex.getMessage}")
          throw ParsingFailure(ex.getMessage, ex)
      }.get

      result.getOrElse(throw new Exception(s"Something wrong ${result.left}"))
    }
  }
}
