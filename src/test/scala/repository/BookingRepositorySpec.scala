package repository

import models._
import weaver.SimpleIOSuite

import java.time.{LocalDate, LocalDateTime}

object BookingRepositorySpec extends SimpleIOSuite {

  def freshRepository = new MockBookingRepository

  // Sample booking data
  val sampleBooking_1: Booking =
    Booking(
      id = Some(1),
      booking_id = "booking_1",
      booking_name = "Sample Booking 1",
      user_id = 1,
      workspace_id = 1,
      booking_date = LocalDate.of(2024, 10, 10),
      start_time = LocalDateTime.of(2024, 10, 10, 9, 0),
      end_time = LocalDateTime.of(2024, 10, 10, 12, 0),
      status = Confirmed,
      created_at = LocalDateTime.of(2024, 10, 5, 15, 0)
    )

  val sampleBooking_2: Booking =
    Booking(
      id = Some(2),
      booking_id = "booking_2",
      booking_name = "Sample Booking 2",
      user_id = 2,
      workspace_id = 1,
      booking_date = LocalDate.of(2024, 10, 10),
      start_time = LocalDateTime.of(2024, 10, 10, 13, 0),
      end_time = LocalDateTime.of(2024, 10, 10, 15, 0),
      status = Confirmed,
      created_at = LocalDateTime.of(2024, 10, 5, 16, 0)
    )

  // Test case for creating a booking
  test(".findBookingById() - find a booking by it's booking_id") {
    val mockRepository: MockBookingRepository = freshRepository
    for {
      _ <- mockRepository.setBooking(sampleBooking_1)
      _ <- mockRepository.setBooking(sampleBooking_2)
      result <- mockRepository.findBookingById("booking_1")
    } yield expect(result == Some(sampleBooking_1))
  }

  // Test case for finding a booking that doesn't exist
  test(".findBookingById() - return an error if booking ID does not exist") {
    val mockRepository = freshRepository
    for {
      _ <- mockRepository.setBooking(sampleBooking_1)
      _ <- mockRepository.setBooking(sampleBooking_2)
      result <- mockRepository.findBookingById("booking_3")
    } yield expect(result == None)
  }

  //  // Test case for updating a booking
  //  test("update a booking") {
  //    val mockRepository = freshRepository
  //    val updatedBooking = sampleBooking.copy(booking_name = "Updated Booking")
  //    for {
  //      _ <- mockRepository.setBooking(sampleBooking)
  //      result <- bookingService.updateBooking("booking_1", updatedBooking)
  //    } yield expect(result == Right(1))
  //  }
  //
  //  // Test case for deleting a booking
  //  test("delete a booking") {
  //    val mockRepository = freshRepository
  //    for {
  //      _ <- mockRepository.setBooking(sampleBooking)
  //      result <- bookingService.deleteBooking("booking_1")
  //    } yield expect(result == Right(1))
  //  }
}
