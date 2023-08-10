package trivia.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResponseDAO {
    @Insert
    public long insertResponse(Response r);
    @Query("Select * from userResponses")
    public List<Response> getAllResponses();
    @Delete
    void deleteResponse(Response r);
}
