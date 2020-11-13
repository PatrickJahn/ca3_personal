package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;


@Entity
@NamedQuery(name = "LikedMovie.deleteAllRows", query = "DELETE from LikedMovie")
public class LikedMovie implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String url;
    
    
    @ManyToMany(mappedBy = "likedMovies")
    private List<User> users = new ArrayList();
    

    
    public LikedMovie() {}
    
         public LikedMovie(String url) {
             this.url = url;
    }
   
    
         public String getUrl(){
             return this.url;
         }
         
          public void setUrl(String url){
             this.url = url;
         }
   
          
   
}
