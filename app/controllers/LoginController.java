package controllers;

import play.api.Environment;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import views.html.*;

import models.users.*;

public class LoginController extends Controller{

private FormFactory formFactory;

private Environment env;

private User getUserFromSession() {
	  return User.getUserById(session().get("email"));
}
@Inject
public LoginController(Environment e, FormFactory f){
this.env = e;
this.formFactory = f;
}

public Result login(){
Form<Login> loginForm = formFactory.form(Login.class);

return ok(login.render(loginForm,getUserFromSession()));
}


public Result loginSubmit(){
Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

if(loginForm.hasErrors()){
return badRequest(login.render(loginForm,getUserFromSession()));
}
else{
session().clear();

session("email", loginForm.get().getEmail());
}
return redirect(controllers.routes.HomeController.index());
}

public Result logout(){

session().clear();
flash("success", "you've been logged out");
return redirect(routes.LoginController.login());
}


}
