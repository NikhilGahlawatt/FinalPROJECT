package FlightData;

import android.database.Cursor;

import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algonquin.cst2335.finalproject.FlightData.FlightDAO;
import algonquin.cst2335.finalproject.FlightData.FlightDetails;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FlightDAO_Impl implements FlightDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FlightDetails> __insertionAdapterOfFlightDetails;

  private final EntityDeletionOrUpdateAdapter<FlightDetails> __deletionAdapterOfFlightDetails;

  public FlightDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFlightDetails = new EntityInsertionAdapter<FlightDetails>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `FlightDetails` (`flightID`,`flightNumber`,`arrivalAirport`,`terminal`,`gate`,`delay`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FlightDetails value) {
        stmt.bindLong(1, value.id);
        if (value.getFlightNumber() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getFlightNumber());
        }
        if (value.getArrivalAirport() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getArrivalAirport());
        }
        if (value.getTerminal() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTerminal());
        }
        if (value.getGate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getGate());
        }
        if (value.getDelay() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getDelay());
        }
      }
    };
    this.__deletionAdapterOfFlightDetails = new EntityDeletionOrUpdateAdapter<FlightDetails>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `FlightDetails` WHERE `flightID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FlightDetails value) {
        stmt.bindLong(1, value.id);
      }
    };
  }

  @Override
  public void insertFlight(final FlightDetails flight) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFlightDetails.insert(flight);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteFlight(final FlightDetails flight) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFlightDetails.handle(flight);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<FlightDetails> getAllFlights() {
    final String _sql = "SELECT * FROM FlightDetails";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "flightID");
      final int _cursorIndexOfFlightNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "flightNumber");
      final int _cursorIndexOfArrivalAirport = CursorUtil.getColumnIndexOrThrow(_cursor, "arrivalAirport");
      final int _cursorIndexOfTerminal = CursorUtil.getColumnIndexOrThrow(_cursor, "terminal");
      final int _cursorIndexOfGate = CursorUtil.getColumnIndexOrThrow(_cursor, "gate");
      final int _cursorIndexOfDelay = CursorUtil.getColumnIndexOrThrow(_cursor, "delay");
      final List<FlightDetails> _result = new ArrayList<FlightDetails>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FlightDetails _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpFlightNumber;
        if (_cursor.isNull(_cursorIndexOfFlightNumber)) {
          _tmpFlightNumber = null;
        } else {
          _tmpFlightNumber = _cursor.getString(_cursorIndexOfFlightNumber);
        }
        final String _tmpArrivalAirport;
        if (_cursor.isNull(_cursorIndexOfArrivalAirport)) {
          _tmpArrivalAirport = null;
        } else {
          _tmpArrivalAirport = _cursor.getString(_cursorIndexOfArrivalAirport);
        }
        final String _tmpTerminal;
        if (_cursor.isNull(_cursorIndexOfTerminal)) {
          _tmpTerminal = null;
        } else {
          _tmpTerminal = _cursor.getString(_cursorIndexOfTerminal);
        }
        final String _tmpGate;
        if (_cursor.isNull(_cursorIndexOfGate)) {
          _tmpGate = null;
        } else {
          _tmpGate = _cursor.getString(_cursorIndexOfGate);
        }
        final String _tmpDelay;
        if (_cursor.isNull(_cursorIndexOfDelay)) {
          _tmpDelay = null;
        } else {
          _tmpDelay = _cursor.getString(_cursorIndexOfDelay);
        }
        _item = new FlightDetails(_tmpId,_tmpFlightNumber,_tmpArrivalAirport,_tmpTerminal,_tmpGate,_tmpDelay);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public FlightDetails getFlightByNumber(final String flightNumber) {
    final String _sql = "SELECT * FROM FlightDetails WHERE flightNumber = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (flightNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, flightNumber);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "flightID");
      final int _cursorIndexOfFlightNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "flightNumber");
      final int _cursorIndexOfArrivalAirport = CursorUtil.getColumnIndexOrThrow(_cursor, "arrivalAirport");
      final int _cursorIndexOfTerminal = CursorUtil.getColumnIndexOrThrow(_cursor, "terminal");
      final int _cursorIndexOfGate = CursorUtil.getColumnIndexOrThrow(_cursor, "gate");
      final int _cursorIndexOfDelay = CursorUtil.getColumnIndexOrThrow(_cursor, "delay");
      final FlightDetails _result;
      if(_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpFlightNumber;
        if (_cursor.isNull(_cursorIndexOfFlightNumber)) {
          _tmpFlightNumber = null;
        } else {
          _tmpFlightNumber = _cursor.getString(_cursorIndexOfFlightNumber);
        }
        final String _tmpArrivalAirport;
        if (_cursor.isNull(_cursorIndexOfArrivalAirport)) {
          _tmpArrivalAirport = null;
        } else {
          _tmpArrivalAirport = _cursor.getString(_cursorIndexOfArrivalAirport);
        }
        final String _tmpTerminal;
        if (_cursor.isNull(_cursorIndexOfTerminal)) {
          _tmpTerminal = null;
        } else {
          _tmpTerminal = _cursor.getString(_cursorIndexOfTerminal);
        }
        final String _tmpGate;
        if (_cursor.isNull(_cursorIndexOfGate)) {
          _tmpGate = null;
        } else {
          _tmpGate = _cursor.getString(_cursorIndexOfGate);
        }
        final String _tmpDelay;
        if (_cursor.isNull(_cursorIndexOfDelay)) {
          _tmpDelay = null;
        } else {
          _tmpDelay = _cursor.getString(_cursorIndexOfDelay);
        }
        _result = new FlightDetails(_tmpId,_tmpFlightNumber,_tmpArrivalAirport,_tmpTerminal,_tmpGate,_tmpDelay);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
