package com.share_will.mobile.utils;

import android.content.Context;
import android.util.Log;

import com.share_will.mobile.model.entity.DaoMaster;
import com.share_will.mobile.model.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ChenGD on 2018/3/22.
 *
 * @author chenguandu
 */

public class DBUtils {
    public static final String DATABASE_NAME = "sharewill";
    private static Context mContext;
    private static DaoSession mDaoSession;
    public static void initDB(Context context){
        mContext = context.getApplicationContext();
        ProOpenHelper openHelper = new ProOpenHelper(mContext, DATABASE_NAME);
        Database db = openHelper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }
    public static DaoSession getDaoSession(){
        if (mDaoSession == null){
            throw new RuntimeException("GreenDao not init yet!");
        }
        return mDaoSession;
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class ProOpenHelper extends DaoMaster.OpenHelper {

        public ProOpenHelper(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + DaoMaster.SCHEMA_VERSION);
            DaoMaster.createAllTables(db, false);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            DaoMaster.dropAllTables(db, true);
            onCreate(db);
        }
    }
}
