package fr.supavenir.lsts.couleurs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


// TODO pouvoir modifier et supprimer la couleur sélectionnée dans la liste
// TODO en ajoutant deux éléments visibles comportant des îcones de type delete et edit


public class ListeCouleurs extends AppCompatActivity {

    private ListView lvListeCouleurs;
    private Button btnAjouterCouleur;
    private AdaptateurCouleur adaptateur;
    private int positionEnCours = 0;

    public void setPositionEnCours( int position ) {
        this.positionEnCours = position;
    }

    private ActivityResultLauncher<Intent> lanceurActiviteChoixCouleur = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        int a = result.getData().getIntExtra("a", 255);
                        int r = result.getData().getIntExtra("r", 255);
                        int v = result.getData().getIntExtra("v", 255);
                        int b = result.getData().getIntExtra("b", 255);
                        String nomCouleur = result.getData().getStringExtra("nom");
                        //Log.i("COULEUR","couleur "+a+" , " + r + " , " + v +" , "+ b +" " + nomCouleur);

                        String requete = result.getData().getStringExtra("requete");
                        if ( requete.equals("AJOUT")) {
                            adaptateur.ajouterCouleur(new Couleur(a, r, v, b, nomCouleur));
                        }
                        else if ( requete.equals("MODIF"))  {
                            /*adaptateur.changerCouleur( adaptateur.getPositionEnCours() , new Couleur(0,a, r, v, b, nomCouleur) );*/
                        }
                    }
                    else if ( result.getResultCode() == RESULT_CANCELED )
                    {
                        Toast.makeText( ListeCouleurs.this , "Opération annulée" , Toast.LENGTH_SHORT).show();
                    }
                }
            } );

    public void lancerActiviteChoixCouleurAvecIntent( Intent intention )  {
        lanceurActiviteChoixCouleur.launch( intention );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activite_liste_couleurs );

        // La base de donnée a-t'elle été remplie ?
        // Si c'est le cas, cela a été sauvegardé dans les préférences

        boolean dbUpToDate = checkDbState();
        if(!dbUpToDate) {
            createAndPopulateDb();
            writeDbState();
        }

        formatListCouleur();

        btnAjouterCouleur = findViewById( R.id.btnAjouterCouleur );
        btnAjouterCouleur.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentionChoixCouleur = new Intent( ListeCouleurs.this , ActiviteChoixCouleur.class );
                lanceurActiviteChoixCouleur.launch( intentionChoixCouleur );
            }
        });


    }

    public void formatListCouleur() {
        adaptateur = new AdaptateurCouleur( this , getCouleursFromDB() );
        lvListeCouleurs = findViewById( R.id.lvCouleurs );
        lvListeCouleurs.setAdapter( adaptateur );
    }

    public ArrayList<Couleur> generationListeCouleurs() {
        ArrayList<Couleur> listeCouleursInitiale = new ArrayList<>();
        listeCouleursInitiale.add( new Couleur(255, 0 , 0, 0 , "Noir absolu"));
        listeCouleursInitiale.add( new Couleur(255, 255 , 255, 255 , "Blanc absolu"));
        return listeCouleursInitiale;
    }

    private boolean checkDbState() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getBoolean("dbUpToDate", false);
    }

    private void writeDbState() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dbUpToDate", true);
        editor.commit();
    }

    private void createAndPopulateDb() {

        DBHelper dbHelper = new DBHelper(ListeCouleurs.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        for(int i =0;i<3;i++) {
            String nom = String.format("couleur%d", i+1);
            int a = (int) (Math.random()*255.0);
            int r = (int) (Math.random()*255.0);
            int g = (int) (Math.random()*255.0);
            int b = (int) (Math.random()*255.0);

            ContentValues contentValues = new ContentValues();
            contentValues.put("nom", nom );
            contentValues.put("a", a );
            contentValues.put("r", r );
            contentValues.put("g", g );
            contentValues.put("b", b );

            db.insert("Couleur", null, contentValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    @SuppressLint("Range")
    private ArrayList<Couleur> getCouleursFromDB() {

        String sqlQuery = "Select * from Couleur order by nom";
        DBHelper dbHelper = new DBHelper(ListeCouleurs.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);
        ArrayList<Couleur> lesCouleurs = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {

            String nom = cursor.getString(cursor.getColumnIndex("nom"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int  a = cursor.getInt(cursor.getColumnIndex("a"));
            int  r = cursor.getInt(cursor.getColumnIndex("r"));
            int  g = cursor.getInt(cursor.getColumnIndex("g"));
            int  b = cursor.getInt(cursor.getColumnIndex("b"));

            lesCouleurs.add( new Couleur(a, r ,g ,b , nom));
        }
        return lesCouleurs;
    }




}