package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.filmDTO;
import entities.User;
import errorhandling.API_Exception;
import facades.LikedMovieFacade;
import facades.RemoteServerFacade;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * @author Patrick
 */
@Path("info")
public class DemoResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final LikedMovieFacade FACADE =  LikedMovieFacade.getFacadeExample(EMF);
   private final RemoteServerFacade remoteFACADE =  RemoteServerFacade.getRemoteServerFacade(EMF);
    
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
    
    
    
    /** OBS Nedestående endpoints er til for at vise hvordan man kan hente fra andre servere OBS  **/
    
    @GET
    @RolesAllowed({"admin","user"})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("filmsparallel")
    public String getFromServersParallel() throws IOException, InterruptedException, ExecutionException, API_Exception {
        return remoteFACADE.getAllFilmsParallel();
    }
    
     @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("films")
    public String getFromServers() throws IOException, API_Exception {

        return remoteFACADE.getAllFilms();
    }
    
    
    
     /** OBS Nedestående endpoints er tilføjet som ekstra i personlig CA3 OBS  **/
    
    @POST
    @RolesAllowed({"admin","user"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("likefilm")
    public String likeMovie(String jsonString) {
        
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
           String username = json.get("username").getAsString();
           String url = json.get("url").getAsString();
      
            FACADE.addLikedMovie(username, url);
           return "{\"msg\":\"Movie with url " + url + " was liked by " + username + " \"}";
    }
    
    
     @POST
    @RolesAllowed({"admin","user"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("dislikefilm")
    public String dislikeMovie(String jsonString) {
        
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
           String username = json.get("username").getAsString();
           String url = json.get("url").getAsString();
      
            FACADE.removeLikedMovie(username, url);
           return "{\"msg\":\"Movie with url " + url + " was disliked by " + username + " \"}";
    }
    
    @GET
    @RolesAllowed({"admin","user"})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allfilms")
    public String getFromServersAll() throws IOException, API_Exception, InterruptedException, ExecutionException {
         EntityManager em = EMF.createEntityManager();
                 String thisuser = securityContext.getUserPrincipal().getName();
       User u = em.find(User.class,thisuser);          
       
        return remoteFACADE.getAllFilmsParallel2(u.getLikedMovies());
    }
    
}   