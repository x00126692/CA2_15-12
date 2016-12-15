package controllers;

import play.api.Environment;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import views.html.*;

import models.*;
import models.users.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

private User getUserFromSession() {
	  return User.getUserById(session().get("email"));
}
public Result index() {
	return ok(index.render(getUserFromSession()));
}

public Result divisions() {
	return ok(divisions.render(getUserFromSession()));
}

public Result events() {
	return ok(events.render(getUserFromSession()));
}

public Result gallery() {
	return ok(gallery.render(getUserFromSession()));
}


public Result news() {
	return ok(news.render(getUserFromSession()));
}

public Result ranking() {
	return ok(ranking.render(getUserFromSession()));
}

public Result products() {
        List<Product> productsList = Product.findAll();

	return ok(products.render(productsList,getUserFromSession()));
}

public Result addproduct() {
        Form<Product> addProductForm = formFactory.form(Product.class);
        return ok(addproduct.render(addProductForm,getUserFromSession()));
}

private FormFactory formFactory;

@Inject
public HomeController(FormFactory f) {
this.formFactory=f;	
}

public Result addproductsubmit(){
Form<Product> newProductForm = formFactory.form(Product.class).bindFromRequest();

if(newProductForm.hasErrors()){
return badRequest(addproduct.render(newProductForm,getUserFromSession()));
	}

Product p = newProductForm.get();

if (p.getId() == 0) {
   p.save();
}
else if(p.getId() != 0) {
   p.update();
}

flash("success", "Product " +p.getName() + " has been created/updated");

return redirect(controllers.routes.HomeController.products());

}

@Security.Authenticated(Secured.class)
@Transactional
public Result updateProduct(Long id) {

Product p;
Form<Product> productForm;

try {
     p=Product.find.byId(id);

     productForm = formFactory.form(Product.class).fill(p);

} catch (Exception ex){
     return badRequest("error");
}

return ok(addproduct.render(productForm,getUserFromSession()));

}
@Security.Authenticated(Secured.class)
@Transactional
public Result deleteProduct(Long id){
Product.find.ref(id).delete();
flash("success", "Product has been deleted");
return redirect(routes.HomeController.products());
}
}
