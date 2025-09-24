package app.utils;

import app.daos.PoemDAO;
import app.dtos.PoemDTO;
import app.enums.PoemType;
import jakarta.persistence.EntityManagerFactory;

import java.io.BufferedReader; // Til at læse filen linje for linje
import java.io.InputStream;    // Til at få filen som InputStream
import java.io.InputStreamReader; // Konverterer InputStream til Reader, som BufferedReader kan bruge

public class PoemDataLoader {


    // Metode til at læse digte fra fil og indsætte dem i databasen
    public static void loadData(EntityManagerFactory emf) {
        PoemDAO dao = PoemDAO.getInstance(emf); // Henter en instans af DAO med den givne EntityManagerFactory

        // Prøver at åbne filen "poems.txt" fra resources-mappen og læse den linje for linje
        try (InputStream is = PoemDataLoader.class.getResourceAsStream("/poems.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line; // Variabel til at holde hver linje fra filen
            while ((line = br.readLine()) != null) { // Læser filen linje for linje indtil slut
                String[] parts = line.split("\\|", 5); // Splitter linjen ved "|" i op til 5 dele (id, title, text, author, type)
                if (parts.length < 5) continue; // Hopper over linjer der ikke har alle 5 dele

                String title = parts[1]; // Henter titlen fra den anden del
                String text = parts[2].replace("\\n", "\n"); // Henter teksten og erstatter "\n" med faktisk linjeskift
                String author = parts[3]; // Henter forfatteren
                PoemType type = PoemType.valueOf(parts[4].toUpperCase()); // Henter typen af digt og konverterer til enum

                PoemDTO dto = new PoemDTO(); // Opretter en ny DTO til digtet
                dto.setTitle(title); // Sætter titel
                dto.setText(text);   // Sætter tekst
                dto.setAuthor(author); // Sætter forfatter
                dto.setType(type);    // Sætter type

                dao.create(dto); // Gemmer digtet i databasen via DAO
            }

            System.out.println("success Poems loaded into DB via DAO"); // Logger at indlæsningen er færdig

        } catch (Exception e) { // Fanger fejl under fil-læsning eller databaseoperationer
            e.printStackTrace(); // Printer stacktrace til konsollen
        }
    }
}



