package main

import cats.effect._
import com.comcast.ip4s._
import controllers._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router
import repositories._
import services._

object Main extends IOApp {

  // Resource-safe way to initialize the transactor
  def transactorResource[F[_] : Async]: Resource[F, HikariTransactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool(32) // Specify thread pool size (32 threads in this case)
      xa <- HikariTransactor.newHikariTransactor[F](
        driverClassName = "org.postgresql.Driver",
        url = "jdbc:postgresql://localhost:5450/cashew_db",
        user = "postgres",
        pass = "cashew",
        connectEC = ce // Connect execution context
      )
    } yield xa

  // Entry point
  override def run(args: List[String]): IO[ExitCode] = {
    // Resource composition
    transactorResource[IO].use { transactor =>
      val bookingRepository = new BookingRepository[IO](transactor)
      val bookingService = new BookingServiceImpl[IO](bookingRepository)
      val bookingController = new BookingController[IO](bookingService) // Create controller instance
      // Combine routes from different controllers if you have multiple

      def createRouter: HttpRoutes[IO] = {
        val bookingRepository = new BookingRepository[IO](transactor) // Create repository instance
        val bookingService = new BookingServiceImpl[IO](bookingRepository) // Create service instance
        val bookingController = new BookingController[IO](bookingService) // Create controller instance

        // Combine the routes from different controllers, if needed
        Router(
          "/api" -> bookingController.routes // Prefix all booking routes with /api
        )
      }

      val server = EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(createRouter.orNotFound)
        .build
        .use(_ => IO.never)
        .as(ExitCode.Success)

      server
    }
  }
}
