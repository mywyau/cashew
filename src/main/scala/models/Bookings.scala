package models

import java.time.{LocalDate, LocalDateTime}


sealed trait BookingStatus

case object Pending extends BookingStatus

case object Confirmed extends BookingStatus

case object Cancelled extends BookingStatus


case class Bookings(
                     id: String,
                     user_id: String,
                     workspace_id: String,
                     booking_date: LocalDate,
                     start_time: LocalDateTime,
                     end_time: LocalDateTime,
                     status: BookingStatus,
                     created_at: LocalDateTime
                   )
