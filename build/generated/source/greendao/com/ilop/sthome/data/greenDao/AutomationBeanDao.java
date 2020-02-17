package com.ilop.sthome.data.greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ilop.sthome.utils.greenDao.converter.DeviceConverter;
import com.ilop.sthome.utils.greenDao.converter.SceneModelConverter;
import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "AUTOMATION_BEAN".
*/
public class AutomationBeanDao extends AbstractDao<AutomationBean, Long> {

    public static final String TABLENAME = "AUTOMATION_BEAN";

    /**
     * Properties of entity AutomationBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Code = new Property(2, String.class, "code", false, "CODE");
        public final static Property Mid = new Property(3, int.class, "mid", false, "MID");
        public final static Property DeviceName = new Property(4, String.class, "deviceName", false, "DEVICE_NAME");
        public final static Property Model = new Property(5, String.class, "model", false, "MODEL");
        public final static Property InputList = new Property(6, String.class, "inputList", false, "INPUT_LIST");
        public final static Property OutputList = new Property(7, String.class, "outputList", false, "OUTPUT_LIST");
    }

    private final SceneModelConverter modelConverter = new SceneModelConverter();
    private final DeviceConverter inputListConverter = new DeviceConverter();
    private final DeviceConverter outputListConverter = new DeviceConverter();

    public AutomationBeanDao(DaoConfig config) {
        super(config);
    }
    
    public AutomationBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"AUTOMATION_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"CODE\" TEXT," + // 2: code
                "\"MID\" INTEGER NOT NULL UNIQUE ," + // 3: mid
                "\"DEVICE_NAME\" TEXT UNIQUE ," + // 4: deviceName
                "\"MODEL\" TEXT," + // 5: model
                "\"INPUT_LIST\" TEXT," + // 6: inputList
                "\"OUTPUT_LIST\" TEXT);"); // 7: outputList
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"AUTOMATION_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AutomationBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(3, code);
        }
        stmt.bindLong(4, entity.getMid());
 
        String deviceName = entity.getDeviceName();
        if (deviceName != null) {
            stmt.bindString(5, deviceName);
        }
 
        SceneModelBean model = entity.getModel();
        if (model != null) {
            stmt.bindString(6, modelConverter.convertToDatabaseValue(model));
        }
 
        List inputList = entity.getInputList();
        if (inputList != null) {
            stmt.bindString(7, inputListConverter.convertToDatabaseValue(inputList));
        }
 
        List outputList = entity.getOutputList();
        if (outputList != null) {
            stmt.bindString(8, outputListConverter.convertToDatabaseValue(outputList));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AutomationBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(3, code);
        }
        stmt.bindLong(4, entity.getMid());
 
        String deviceName = entity.getDeviceName();
        if (deviceName != null) {
            stmt.bindString(5, deviceName);
        }
 
        SceneModelBean model = entity.getModel();
        if (model != null) {
            stmt.bindString(6, modelConverter.convertToDatabaseValue(model));
        }
 
        List inputList = entity.getInputList();
        if (inputList != null) {
            stmt.bindString(7, inputListConverter.convertToDatabaseValue(inputList));
        }
 
        List outputList = entity.getOutputList();
        if (outputList != null) {
            stmt.bindString(8, outputListConverter.convertToDatabaseValue(outputList));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AutomationBean readEntity(Cursor cursor, int offset) {
        AutomationBean entity = new AutomationBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // code
            cursor.getInt(offset + 3), // mid
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // deviceName
            cursor.isNull(offset + 5) ? null : modelConverter.convertToEntityProperty(cursor.getString(offset + 5)), // model
            cursor.isNull(offset + 6) ? null : inputListConverter.convertToEntityProperty(cursor.getString(offset + 6)), // inputList
            cursor.isNull(offset + 7) ? null : outputListConverter.convertToEntityProperty(cursor.getString(offset + 7)) // outputList
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AutomationBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMid(cursor.getInt(offset + 3));
        entity.setDeviceName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setModel(cursor.isNull(offset + 5) ? null : modelConverter.convertToEntityProperty(cursor.getString(offset + 5)));
        entity.setInputList(cursor.isNull(offset + 6) ? null : inputListConverter.convertToEntityProperty(cursor.getString(offset + 6)));
        entity.setOutputList(cursor.isNull(offset + 7) ? null : outputListConverter.convertToEntityProperty(cursor.getString(offset + 7)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AutomationBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AutomationBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AutomationBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}