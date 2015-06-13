package powerups.core.boardpowerup;

import com.avaje.ebean.Ebean;
import powerups.models.BoardPost;

import java.util.List;

public class BoardService {
    public List<BoardPost> findAllPosts() {
        return Ebean.find(BoardPost.class).findList();
    }
}
