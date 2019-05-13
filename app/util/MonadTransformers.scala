package util
import scala.concurrent.{ExecutionContext, Future}

object MonadTransformers {
  implicit class EitherTransformer[T](either: Either[String, Future[T]]) {
    def mapToFuture(implicit ec: ExecutionContext): Future[Either[String, T]] =
      either match {
        case Left(s)  => Future.successful(Left(s))
        case Right(f) => f.map(Right(_))
      }
  }
}
