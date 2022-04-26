package fr.supavenir.lsts.couleurs;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private final static int dbVersion = 1;
    private final static String dbName="couleursDB";

    public DBHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }


    public void deleteCouleurByNom( String nom ) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String[] args = { nom };
        db.delete( "Couleur" ,"nom=?", args );
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void updateCouleurByNom(Couleur couleur, String[] nom) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nom", couleur.getNom() );
        contentValues.put("a", couleur.getA() );
        contentValues.put("r", couleur.getR() );
        contentValues.put("g", couleur.getV() );
        contentValues.put("b", couleur.getB() );

        db.update("Couleur", contentValues, "name=?", nom);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Couleur (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, a INTEGER," +
                " r INTEGER, g INTEGER, b INTEGER );");

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
