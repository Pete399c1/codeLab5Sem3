package app;


import app.config.ApplicationConfig;
import app.config.HibernateConfig;

import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
       EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
       //ApplicationConfig.startServer(7007);
       emf.close();
    }
}