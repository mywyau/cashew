package repositories

import cats.effect.Sync
import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.util.meta.Meta
import models._

import java.sql.Timestamp
import java.time.LocalDateTime

class BookingRepository[F[_] : Sync](transactor: Transactor[F]) {

  // Meta instance to map between LocalDateTime and Timestamp
  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta[Timestamp].imap(_.toLocalDateTime)(Timestamp.valueOf)

  def findBookingById(bookingId: String): F[Option[Bookings]] = {
    sql"SELECT * FROM bookings WHERE id = $bookingId"
      .query[Bookings]
      .option
      .transact(transactor)
  }

  def getAllBookings: F[List[Bookings]] = {
    sql"SELECT * FROM bookings"
      .query[Bookings]
      .to[List]
      .transact(transactor)
  }

  def setBookings(booking: Bookings): F[Int] = {

    // Correct logging to show the actual values (without "Some" in the log)
    //    println(s"VALUES (${booking.user_id}, ${booking.workspace_id}, ${booking.booking_date}, ${booking.start_time}, ${booking.end_time}, ${booking.status}, ${booking.created_at})")

    // Pass deskId and roomId as Option values, so Doobie will handle them
    sql"""
      INSERT INTO bookings (user_id, workspace_id, booking_date, start_time, end_time, status, created_at)
      VALUES (${booking.user_id}, ${booking.workspace_id}, ${booking.booking_date}, ${booking.start_time}, ${booking.end_time}, ${booking.status}, ${booking.created_at})
    """.update
      .run
      .transact(transactor)
  }
}
