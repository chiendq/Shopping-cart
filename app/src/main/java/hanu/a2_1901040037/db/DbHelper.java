package hanu.a2_1901040037.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import hanu.a2_1901040037.models.Constant;


public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table
        final String CREATE_TABLES = "CREATE TABLE products(" +
                "id INTEGER PRIMARY KEY," +
                "thumbnail TEXT ," +
                "name TEXT," +
                "unitPrice INTEGER," +
                "quantity INTEGER)";

        sqLiteDatabase.execSQL(CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //drop tables - warning : lost data
        sqLiteDatabase.execSQL("DROP TABLE " + Constant.DB_TABLE_NAME);

        // create again
        onCreate(sqLiteDatabase);
    }
}
