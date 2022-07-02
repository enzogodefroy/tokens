package fr.supavenir.lsts.tokens;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private final static int dbVersion = 1;
    private final static String dbName="tokenDB";

    public DBHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    public boolean doesTokenExists(String name)
    {
        String[] columns = new String[] {"name"};
        String[] whereClause = new String[] {name};
        boolean returnValue = false;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        Cursor c = db.query("Token", columns, "name = ?", whereClause, null, null, null);

        if (c.moveToFirst())
        {
            returnValue =  true;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        return returnValue;
    }

    public void updateTokenByName(Token token, String name) {

        Log.d("DBHelper", token.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", token.getName() );
        contentValues.put("actualPrice", token.getActualPrice() );
        contentValues.put("lowerPrice", token.getLowerPrice() );
        contentValues.put("higherPrice", token.getHigherPrice() );

        String[] tokenName = new String[] {
            name
        };

        db.update("Token", contentValues, "name=?", tokenName);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void addToken(Token token) {

        Log.d("DBHelper", token.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", token.getName());
        contentValues.put("actualPrice", token.getActualPrice());
        contentValues.put("lowerPrice", token.getLowerPrice());
        contentValues.put("higherPrice", token.getHigherPrice());

        db.insert("Token", null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public Token getToken(String token)
    {
        String[] columns = new String[] {
                "name",
                "actualPrice",
                "lowerPrice",
                "higherPrice"
        };

        String[] whereClause = new String[] {
                token
        };

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        Cursor c = db.query("Token", columns, "name = ?", whereClause, null, null, "name");

        Token tokenObj = new Token(token, 0 , 0, 0);

        while (c.moveToNext())
        {
            tokenObj.setName(c.getString(c.getColumnIndex("name")));
            tokenObj.setActualPrice(c.getFloat(c.getColumnIndex("actualPrice")));
            tokenObj.setLowerPrice(c.getFloat(c.getColumnIndex("lowerPrice")));
            tokenObj.setHigherPrice(c.getFloat(c.getColumnIndex("higherPrice")));
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        return tokenObj;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE Token (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, actualPrice FLOAT, lowerPrice FLOAT, higherPrice FLOAT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int indexVersion = oldVersion; indexVersion < newVersion;
             indexVersion++) {
            int nextVersion = indexVersion + 1;
            switch (nextVersion) {
                case 2:
                    // upgrapdeToVersion2(db);
                    break;
                case 3:
                    // mise à jour future pour la version 3
                    break;
            }

        }
    }
}
