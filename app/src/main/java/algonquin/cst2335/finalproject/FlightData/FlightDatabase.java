package algonquin.cst2335.finalproject.FlightData;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * This abstract class defines the database configuration and serves as the main access point to the persisted data.
 * The entities that belong to the database and the version of the database are defined here.
 * It also contains an abstract method for each @Dao.
 */
@Database(entities = {FlightDetails.class}, version = 2, exportSchema = false)
public abstract class FlightDatabase extends RoomDatabase {

    /**
     * Singleton instance of the FlightDatabase.
     */
    private static FlightDatabase instance;

    /**
     * Define an abstract method for each @Dao.
     * Room will generate an implementation of this.
     *
     * @return Instance of FlightDAO
     */
    public abstract FlightDAO flightDao();

    /**
     * Returns a singleton instance of the FlightDatabase.
     *
     * @param context The context of the caller
     * @return The singleton instance of FlightDatabase
     */
    public static synchronized FlightDatabase getInstance(Context context) {
        // If the singleton instance is null, create a new one
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            FlightDatabase.class, "flight_database")
                    .fallbackToDestructiveMigration()  // If the schema changes and you didn't define a migration,
                    // it will be destroyed and recreated.
                    .build();
        }
        // If the singleton instance already exists, return it
        return instance;
    }
}
