package trivia.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Response.class}, version=1)
public abstract class ResponseDatabase extends RoomDatabase {

    public abstract ResponseDAO rDAO();
}