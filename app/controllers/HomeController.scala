package controllers

import com.google.gson.Gson
import global.ResultSupport.RichResult
import javax.inject._
import play.api.db._
import play.api.mvc._
import service.UserService

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(dbApi: DBApi, val controllerComponents: ControllerComponents) extends BaseController {

  private val userService = new UserService(dbApi)
  private val gson = new Gson()

  def index(): Action[AnyContent] =
    Action { Ok(gson.toJson(userService.findAll())).enableCors }
}
