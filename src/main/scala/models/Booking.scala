package models

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

import java.time.{LocalDate, LocalDateTime}


sealed trait BookingStatus

case object Pending extends BookingStatus

case object Confirmed extends BookingStatus

case object Cancelled extends BookingStatus

// Custom Encoder/Decoder for BookingStatus to treat it as a string in JSON
object BookingStatus {

  def fromString(str: String): BookingStatus =
    str match {
      case "Confirmed" => Confirmed
      case "Pending" => Pending
      case "Cancelled" => Cancelled
      case _ => throw new Exception(s"Unknown status: $str")
    }

  implicit val bookingStatusEncoder: Encoder[BookingStatus] =
    Encoder.encodeString.contramap {
      case Confirmed => "Confirmed"
      case Pending => "Pending"
      case Cancelled => "Cancelled"
    }

  implicit val bookingStatusDecoder: Decoder[BookingStatus] =
    Decoder.decodeString.emap {
      case "Confirmed" => Right(Confirmed)
      case "Pending" => Right(Pending)
      case "Cancelled" => Right(Cancelled)
      case other => Left(s"Invalid booking status: $other")
    }

  //  implicit val encodeEvent: Encoder[BookingStatus] =
  //    Encoder.instance {
  //      case foo@ Confirmed => foo.asJson
  //      case bar@ Pending => bar.asJson
  //      case baz@ Cancelled => baz.asJson
  //    }
  //
  //  implicit val decodeEvent: Decoder[BookingStatus] =
  //    List[Decoder[BookingStatus]](
  //      Decoder[Confirmed].widen,
  //      Decoder[Pending].widen,
  //      Decoder[Cancelled].widen,
  //    )
  //      .reduceLeft(_ or _)
}

case class Booking(
                    id: Option[Int],
                    booking_id: String,
                    booking_name: String,
                    user_id: Int,
                    workspace_id: Int,
                    booking_date: LocalDate,
                    start_time: LocalDateTime,
                    end_time: LocalDateTime,
                    status: BookingStatus,
                    created_at: LocalDateTime
                  )

object Booking {
  // Manually derive Encoder and Decoder for Booking
  implicit val bookingEncoder: Encoder[Booking] = deriveEncoder[Booking]
  implicit val bookingDecoder: Decoder[Booking] = deriveDecoder[Booking]
}

