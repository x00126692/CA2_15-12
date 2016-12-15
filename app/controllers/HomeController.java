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
public Result index() {
	return ok(index.render());
}

public Result divisions() {
	return ok(divisions.render());
}

public Result events() {
	return ok(events.render());
}

public Result gallery() {
	return ok(gallery.render());
}


public Result news() {
	return ok(news.render());
}

public Result ranking() {
	return ok(ranking.render());
}

public Result products() {
        List<Product> productsList = Product.findAll();

	return ok(products.render(productsList));
}

public Result addproduct() {
        Form<Product> addProductForm = formFactory.form(Product.class);
        return ok(addproduct.render(addProductForm));
}


private FormFactory formFactory;

@Inject
public HomeController(FormFactory f) {
this.formFactory=f;	
}

public Result addproductsubmit(){
Form<Product> newProductForm = formFactory.form(Product.class).bindFromRequest();

if(newProductForm.hasErrors()){
return badRequest(addproduct.render(newProductForm));
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

return ok(addproduct.render(productForm));

}

public Result deleteProduct(Long id){
Product.find.ref(id).delete();
flash("success", "Product has been deleted");
return redirect(routes.HomeController.products());
}
}
