package FlightData;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import algonquin.cst2335.finalproject.FlightData.FlightDAO;
import algonquin.cst2335.finalproject.FlightData.FlightDatabase;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FlightDatabase_Impl extends FlightDatabase {
  private volatile FlightDAO _flightDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `FlightDetails` (`flightID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `flightNumber` TEXT, `arrivalAirport` TEXT, `terminal` TEXT, `gate` TEXT, `delay` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a6e7ffde1ca06fce0839fe746ea7da94')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `FlightDetails`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsFlightDetails = new HashMap<String, TableInfo.Column>(6);
        _columnsFlightDetails.put("flightID", new TableInfo.Column("flightID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlightDetails.put("flightNumber", new TableInfo.Column("flightNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlightDetails.put("arrivalAirport", new TableInfo.Column("arrivalAirport", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlightDetails.put("terminal", new TableInfo.Column("terminal", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlightDetails.put("gate", new TableInfo.Column("gate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFlightDetails.put("delay", new TableInfo.Column("delay", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFlightDetails = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFlightDetails = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFlightDetails = new TableInfo("FlightDetails", _columnsFlightDetails, _foreignKeysFlightDetails, _indicesFlightDetails);
        final TableInfo _existingFlightDetails = TableInfo.read(_db, "FlightDetails");
        if (! _infoFlightDetails.equals(_existingFlightDetails)) {
          return new RoomOpenHelper.ValidationResult(false, "FlightDetails(algonquin.cst2335.finalproject.FlightData.FlightDetails).\n"
                  + " Expected:\n" + _infoFlightDetails + "\n"
                  + " Found:\n" + _existingFlightDetails);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a6e7ffde1ca06fce0839fe746ea7da94", "ce8219052c12d0beda4e4f1bef965737");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "FlightDetails");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `FlightDetails`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FlightDAO.class, FlightDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public FlightDAO flightDao() {
    if (_flightDAO != null) {
      return _flightDAO;
    } else {
      synchronized(this) {
        if(_flightDAO == null) {
          _flightDAO = new FlightDAO_Impl(this);
        }
        return _flightDAO;
      }
    }
  }
}
