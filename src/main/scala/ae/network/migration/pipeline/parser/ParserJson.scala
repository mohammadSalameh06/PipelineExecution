  package ae.network.migration.pipeline.parser

  import ae.network.migration.pipeline.models.{Job, Pipeline, Stage}
  import io.circe.{Decoder, ParsingFailure}
  import io.circe.generic.semiauto.deriveDecoder
  import io.circe.jawn.decode

  import scala.io.Source
  import scala.util.Using

  /**
   * ParserJson is responsible for parsing JSON configuration files into Pipeline objects.
   */
  class ParserJson {

    /**
     * Implicit decoder for Job objects using Circe's semiauto derivation.
     */
    implicit val jobDecoder: Decoder[Job] = deriveDecoder[Job]

    /**
     * Implicit decoder for Stage objects using Circe's semiauto derivation.
     */
    implicit val stageDecoder: Decoder[Stage] = deriveDecoder[Stage]

    /**
     * Implicit decoder for Pipeline objects using Circe's semiauto derivation.
     */
    implicit val pipelineDecoder: Decoder[Pipeline] = deriveDecoder[Pipeline]

    /**
     * Parses a JSON file at the specified file path into a Pipeline object.
     *
     * @param filePath The path to the JSON file to be parsed.
     * @return The parsed Pipeline object.
     * @throws ParsingFailure if the JSON cannot be parsed or an exception occurs during parsing.
     */
    def parsePipeline(filePath: String): Pipeline = {
      val result = Using(Source.fromFile(filePath)) { source =>
        val json = source.getLines().mkString
        decode[Pipeline](json)
      }.recover {
        case ex: Exception =>
          throw ParsingFailure(s"Exception occurred while parsing JSON: ${ex.getMessage}", ex)
      }.get

      result match {
        case Right(pipeline) => pipeline
        case Left(error) => throw ParsingFailure(s"Exception occurred while parsing JSON: $error", new Exception(s"Failed to parse JSON: $error"))
      }
    }
  }
