package app.routes;

import app.controllers.PoemController;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class PoemRoutes {
   public static  void addRoutes(Javalin app){
       PoemController poemController = new PoemController();


       app.get("/poems", poemController::getAllPoems);
       app.get("/poems/{id}", poemController::getPoemById);
       app.post("/poems", poemController::createPoem);
       app.put("/poems/{id}", poemController::updatePoem);
       app.delete("/poems/{id}", poemController::deletePoem);
   }
}
