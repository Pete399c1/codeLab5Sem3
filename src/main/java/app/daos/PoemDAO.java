package app.daos;

import app.entities.Poem;
import app.dtos.PoemDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;


public class PoemDAO {

    private static PoemDAO instance;
    private static EntityManagerFactory emf;

    private PoemDAO() {}

    public static PoemDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new PoemDAO();
            PoemDAO.emf = emf;
        }
        return instance;
    }


    public List<PoemDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Poem> query = em.createQuery("SELECT p FROM Poem p", Poem.class);
            return PoemDTO.toDTOList(query.getResultList());
        }
    }


    public PoemDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Poem poem = em.find(Poem.class, id);
            if (poem != null) {
                return new PoemDTO(poem);
            }
            return null;
        }
    }


    public PoemDTO create(PoemDTO poemDTO) {
        Poem poem = new Poem(poemDTO);
        try (EntityManager em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            em.persist(poem);
            tx.commit();
            return new PoemDTO(poem);
        }
    }


    public PoemDTO update(int id, PoemDTO poemDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            Poem poem = em.find(Poem.class, id);
            if (poem != null) {
                var tx = em.getTransaction();
                tx.begin();
                poem.setTitle(poemDTO.getTitle());
                poem.setText(poemDTO.getText());
                poem.setAuthor(poemDTO.getAuthor());
                poem.setType(poemDTO.getType());
                em.merge(poem);
                tx.commit();
                return new PoemDTO(poem);
            }
            return null;
        }
    }


    public boolean delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Poem poem = em.find(Poem.class, id);
            if (poem != null) {
                // var is compiler finding out the type its self
                var tx = em.getTransaction();
                tx.begin();
                em.remove(poem);
                tx.commit();
                return true;
            }
            return false;
        }
    }

    // good to test maybe use later on
    public void deleteAll() {
        try (EntityManager em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            em.createQuery("DELETE FROM Poem").executeUpdate();
            tx.commit();
        }
    }
}
