package app;


import app.config.ApplicationConfig;
import app.config.HibernateConfig;

import app.routes.PoemRoutes;
import app.utils.PoemDataLoader;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


        PoemDataLoader.loadData(emf);

        Javalin app = Javalin.create().start(7070);

        PoemRoutes.addRoutes(app);



        app.events(event -> {
            event.serverStopping(() -> {
                emf.close();
                System.out.println(" EntityManagerFactory closed");
            });
        });

    }
}