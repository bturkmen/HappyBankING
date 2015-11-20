package ingbank.com.tr.happybanking.map.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Iterator;
import java.util.List;

import ingbank.com.tr.happybanking.common.util.Utils;
import ingbank.com.tr.happybanking.map.model.map.Channel;

public class AtmBranchDbHelper {

    public static final String KEY_ROWID = "rowid";
    public static final String KEY_CITY = "KEY_CITY";
    public static final String KEY_CITY_COUNTY = "KEY_CITY_COUNTY";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_OBJECT = "KEY_OBJECT";
    public static final String KEY_IS_ATM = "KEY_IS_ATM";
    public static final String KEY_IS_BRANCH = "KEY_IS_BRANCH";
    public static final String KEY_IS_SPECIAL = "KEY_IS_SPECIAL";
    public static final String KEY_IS_CONCEPT = "KEY_IS_CONCEPT";
    public static final String KEY_IS_DISABLED = "KEY_IS_DISABLED";
    //public static final String KEY_NAME_NORMALIZE = "name_normalize";
    public static final String KEY_SEARCHVALUE = "search_value";
    private static final String DATABASE_NAME = "AtmChannelDB";
    private static final String FTS_VIRTUAL_TABLE = "AtmChannelTable";
    //Create a FTS3 Virtual Table for fast searches
    private static final String DATABASE_CREATE =
            "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3(" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    KEY_CITY + "," +
                    KEY_CITY_COUNTY + "," +
                    //KEY_NAME_NORMALIZE + "," +
                    KEY_NAME + "," +
                    KEY_SEARCHVALUE + "," +
                    KEY_OBJECT + "," +
                    KEY_IS_ATM + " INTEGER, " +
                    KEY_IS_BRANCH + " INTEGER, " +
                    KEY_IS_SPECIAL + " INTEGER, " +
                    KEY_IS_CONCEPT + " INTEGER, " +
                    KEY_IS_DISABLED + " INTEGER, " +
                    //KEY_SEARCH + "," +
                    " UNIQUE (" + KEY_ROWID + "));";
    private static final int DATABASE_VERSION = 9;
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Gson gson;

    public AtmBranchDbHelper(Context ctx) {
        this.mCtx = ctx;
    }

    public AtmBranchDbHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        gson = new Gson();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
            mCtx.deleteDatabase(DATABASE_NAME);
        }
    }

    public long createAtmBranch(List<Channel> items) {
        mDb.beginTransaction();
        Iterator<Channel> iterator = items.iterator();

        String sql = "INSERT INTO " + FTS_VIRTUAL_TABLE + "(" +
                KEY_CITY + "," +
                KEY_CITY_COUNTY + "," +
                //KEY_NAME_NORMALIZE + "," +
                KEY_NAME + "," +
                KEY_SEARCHVALUE + "," +
                KEY_OBJECT + "," +
                KEY_IS_ATM + " , " +
                KEY_IS_BRANCH + " , " +
                KEY_IS_SPECIAL + " , " +
                KEY_IS_CONCEPT + " , " +
                KEY_IS_DISABLED + " " + ")" + " VALUES (?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDb.compileStatement(sql);

        while (iterator.hasNext()) {
            Channel item = iterator.next();
            if (item.getBankChannelType() == null)
                continue;

            statement.clearBindings();
            statement.bindString(1, Utils.strNormalize(item.getCity().toLowerCase(Utils.TargetLocal)));
            statement.bindString(2, Utils.strNormalize(item.getCounty().toLowerCase(Utils.TargetLocal)));
            statement.bindString(3, Utils.strNormalize(item.getName().toLowerCase(Utils.TargetLocal)));
            statement.bindString(4, Utils.strNormalize(item.getName().toLowerCase(Utils.TargetLocal) + " " + item.getAdress().toLowerCase(Utils.TargetLocal)));
            statement.bindString(5, gson.toJson(item));
            statement.bindLong(6, item.isAtm() ? 1 : 0);
            statement.bindLong(7, item.isBranch() ? 1 : 0);
            statement.bindLong(8, item.isSpecialBank() ? 1 : 0);
            statement.bindLong(9, item.isConceptBank() ? 1 : 0);
            statement.bindLong(10, item.isSightlessDisabled() || item.isPhsycaldisabled() ? 1 : 0);
            statement.execute();
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        return Long.MAX_VALUE;
    }

    public Cursor searchRegionByCity(String inputText, int filterValue) {
        //Log.w(TAG, inputText);
        String query = "SELECT *" +
                " FROM " + FTS_VIRTUAL_TABLE + " ";


        String filter = "";
        inputText = inputText.replace(" ", "");
        if (!TextUtils.isEmpty(inputText)) {
            filter += KEY_CITY + " LIKE '%" + Utils.strNormalize(inputText.toLowerCase(Utils.TargetLocal)) + "%' ";
        }

        String atmBranchFilter = "";
        if ((filterValue & AtmBranchListFilter.ATM_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_ATM + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.BRANCH_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_BRANCH + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.SPECIAL_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_SPECIAL + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.CONCEPT_SELECTED) != 0) { // TODO: check this control
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_CONCEPT + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.DISABLED_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_DISABLED + " = 1 ";
        }

        if (!TextUtils.isEmpty(atmBranchFilter)) {
            filter += (TextUtils.isEmpty(filter) ? " " : " AND ") + "(" + atmBranchFilter + ")";
        }

        if (!TextUtils.isEmpty(filter)) {
            query += " WHERE " + filter;
        }

        query += ";";

        //Log.w(TAG, query);
        try {
            Cursor mCursor = mDb.rawQuery(query, null);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }

    }


    public Cursor searchRegionByCounty(String inputText, int filterValue) {
        //Log.w(TAG, inputText);
        String query = "SELECT *" +
                " FROM " + FTS_VIRTUAL_TABLE + " ";


        String filter = "";
        inputText = inputText.replace(" ", "");
        if (!TextUtils.isEmpty(inputText)) {
            filter += KEY_CITY_COUNTY + " LIKE '%" + Utils.strNormalize(inputText.toLowerCase(Utils.TargetLocal)) + "%' ";
        }

        String atmBranchFilter = "";
        if ((filterValue & AtmBranchListFilter.ATM_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_ATM + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.BRANCH_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_BRANCH + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.SPECIAL_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_SPECIAL + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.CONCEPT_SELECTED) != 0) { // TODO: check this control
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_CONCEPT + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.DISABLED_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_DISABLED + " = 1 ";
        }

        if (!TextUtils.isEmpty(atmBranchFilter)) {
            filter += (TextUtils.isEmpty(filter) ? " " : " AND ") + "(" + atmBranchFilter + ")";
        }

        if (!TextUtils.isEmpty(filter)) {
            query += " WHERE " + filter;
        }

        query += ";";

        //Log.w(TAG, query);
        try {
            Cursor mCursor = mDb.rawQuery(query, null);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }

    }


    public Cursor searchChannelAtm(String inputText, int filterValue) throws SQLException {
        //Log.w(TAG, inputText);
        String query = "SELECT *" +
                " FROM " + FTS_VIRTUAL_TABLE + " ";

        String filter = "";
        inputText = inputText.replace(" ", "");
        if (!TextUtils.isEmpty(inputText)) {
            filter += KEY_SEARCHVALUE + " LIKE '%" + Utils.strNormalize(inputText.toLowerCase(Utils.TargetLocal)) + "%' ";
        } else {
        }

        String atmBranchFilter = "";
        if ((filterValue & AtmBranchListFilter.ATM_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_ATM + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.BRANCH_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_BRANCH + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.SPECIAL_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_SPECIAL + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.CONCEPT_SELECTED) != 0) { // TODO: check this control
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_CONCEPT + " = 1 ";
        }

        if ((filterValue & AtmBranchListFilter.DISABLED_SELECTED) != 0) {
            atmBranchFilter += !TextUtils.isEmpty(atmBranchFilter) ? " or " : " ";
            atmBranchFilter += KEY_IS_DISABLED + " = 1 ";
        }

        if (!TextUtils.isEmpty(atmBranchFilter)) {
            filter += (TextUtils.isEmpty(filter) ? " " : " AND ") + "(" + atmBranchFilter + ")";
        }

        if (!TextUtils.isEmpty(filter)) {
            query += " WHERE " + filter;
        }

        query += ";";
        //Log.w(TAG, query);
        try {
            Cursor mCursor = mDb.rawQuery(query, null);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;

        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }

    }


    public boolean deleteAllChannelAtms() {
        int doneDelete;
        doneDelete = mDb.delete(FTS_VIRTUAL_TABLE, null, null);
        //Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            //Log.w(TAG, DATABASE_CREATE);
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }
}
