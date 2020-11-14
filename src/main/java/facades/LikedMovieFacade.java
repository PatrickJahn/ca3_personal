package facades;

import entities.LikedMovie;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class LikedMovieFacade {

    private static LikedMovieFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private LikedMovieFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static LikedMovieFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new LikedMovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    

    
    
    public long getRenameMeCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long renameMeCount = (long)em.createQuery("SELECT COUNT(l) FROM LikedMovie l").getSingleResult();
            return renameMeCount;
        }finally{  
            em.close();
        }
        
    }
    
    public LikedMovie addLikedMovie(String userName, String url){
                   EntityManager em = emf.createEntityManager();
   
           LikedMovie m = new LikedMovie(url);
          User user = em.find(User.class, userName);
          user.addLikedMovie(m);
          
          em.getTransaction().begin(); 
          em.merge(user);
          em.getTransaction().commit();
       
       return m; 
    }

public LikedMovie removeLikedMovie(String userName, String url){
                   EntityManager em = emf.createEntityManager();
   
           LikedMovie m = new LikedMovie(url);
          User user = em.find(User.class, userName);
          user.removeLikedMovie(m);
          
          em.getTransaction().begin(); 
          em.merge(user);
          em.getTransaction().commit();
       
       return m; 
    }

}
