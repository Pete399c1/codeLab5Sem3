package app.controllers;

import app.config.HibernateConfig;
import app.daos.PoemDAO;
import app.dtos.PoemDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class PoemController {


    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


    private final PoemDAO poemDAO = PoemDAO.getInstance(emf);


    public void createPoem(Context ctx) {
        PoemDTO poemDTO = ctx.bodyAsClass(PoemDTO.class);
        PoemDTO created = poemDAO.create(poemDTO);
        ctx.status(HttpStatus.CREATED);
        ctx.json(created);
    }


    public void getAllPoems(Context ctx) {
        List<PoemDTO> poems = poemDAO.getAll();
        ctx.status(HttpStatus.OK);
        ctx.json(poems);
    }


    public void getPoemById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        PoemDTO poem = poemDAO.getById(id);
        if (poem != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(poem);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Poem not found");
        }
    }


    public void updatePoem(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        PoemDTO poemDTO = ctx.bodyAsClass(PoemDTO.class);
        PoemDTO updated = poemDAO.update(id, poemDTO);
        if (updated != null) {
            ctx.status(HttpStatus.OK);
            // JSON answer by using context from javalin
            ctx.json(updated);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Poem not found");
        }
    }


    public void deletePoem(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean deleted = poemDAO.delete(id);
        if (deleted) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Poem not found");
        }
    }

}
