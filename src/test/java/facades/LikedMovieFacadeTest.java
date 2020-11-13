package facades;

import utils.EMF_Creator;
import entities.LikedMovie;
import entities.User;
import errorhandling.API_Exception;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class LikedMovieFacadeTest {

    private static EntityManagerFactory emf;
    private static LikedMovieFacade facade;
    private static RemoteServerFacade remoteFacade;

    public LikedMovieFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = LikedMovieFacade.getFacadeExample(emf);
       remoteFacade = RemoteServerFacade.getRemoteServerFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            User u = new User("bobby", "123456");
            em.getTransaction().begin();
            em.createNamedQuery("LikedMovie.deleteAllRows").executeUpdate();
            em.persist(new LikedMovie("Some txt"));
            em.persist(new LikedMovie("aaa"));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void testAFacadeMethod() {
        assertEquals(2, facade.getRenameMeCount(), "Expects two rows in the database");
    }
    
    
    @Test
    public void testGetAllFilms() throws IOException, InterruptedException, ExecutionException, API_Exception {
            
          String films = remoteFacade.getAllFilmsParallel();
           
        assertEquals(true, films.contains("A New Hope"), "Expects to have the movie title A New Hope");
    }
    
      @Test
    public void testaddLikedMovie() throws IOException, InterruptedException, ExecutionException, API_Exception {
          
          LikedMovie m = facade.addLikedMovie("bobby", "Some url");
        assertEquals(3, facade.getRenameMeCount(), "Expects to have the movie title A New Hope");
    }

}
