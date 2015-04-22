import com.lucidchart.piezo.admin.RequestStatCollector
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.Play.current

import scala.concurrent.Future

object Global extends WithFilters(RequestStatCollector)
{
  val logger = Logger("com.lucidchart.piezo.Global")

  override def onHandlerNotFound(request: RequestHeader): Future[Result] = {
    logger.error("Request handler not found for URL: " + request.uri)
    Future.successful(NotFound(com.lucidchart.piezo.admin.views.html.errors.notfound(None)(request)))
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[Result] = {
    logger.error("Error handling request for URL: " + request.uri, ex)
    if(play.api.Play.isProd || play.api.Play.isTest) {
      Future.successful(InternalServerError(com.lucidchart.piezo.admin.views.html.errors.error(Option(ex.getMessage))(request)))
    } else {
      super.onError(request, ex)
    }
  }

}
