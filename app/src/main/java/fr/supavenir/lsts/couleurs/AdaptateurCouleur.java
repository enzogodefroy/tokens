package fr.supavenir.lsts.couleurs;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 *  L'adaptateur pour visualiser le modèle : une liste d'objets Couleur
 */

// TODO : Mettre en place un cache dans la methode getView()

public class AdaptateurCouleur extends BaseAdapter {

    private ListeCouleurs context;
    private ModeleListeCouleurs modele = new ModeleListeCouleurs();
    private int positionEnCours = 0;

    public int getPositionEnCours() {
        return this.positionEnCours;
    }

    public AdaptateurCouleur( Context context , ArrayList<Couleur> couleurs ) {
        this.context = (ListeCouleurs) context;
        modele.setLesCouleurs( couleurs );
    }

    public void ajouterCouleur( Couleur couleur, String name ) {

        String[] nom = new String[]{name};
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.updateCouleurByNom(couleur, nom);
        this.notifyDataSetChanged();
    }

    public void supprimerCouleur( String name ) {

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.deleteCouleurByNom(name);
        this.notifyDataSetChanged();
    }

    public void changerCouleur( int position , Couleur  couleur ) {
        modele.modifierCouleur( position , couleur );
        this.notifyDataSetChanged();
    }

    /** On adapte les methodes pour visualiser le modèle en mémoire. */

    @Override
    public int getCount() {
        return modele.getLesCouleurs().size();
    }

    @Override
    public Object getItem(int position) {
        return modele.getLesCouleurs().get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Important : le code pour recuperer la vue de l'item par son layout
        View itemView = LayoutInflater.from( context ).inflate(R.layout.liste_couleur_item,
                parent , false );

        TextView tvCouleur = itemView.findViewById( R.id.tvCouleur);
        TextView tvNomCouleur = itemView.findViewById( R.id.tvNomCouleur );
        Button btnModifier = itemView.findViewById( R.id.btnModifier );
        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lancer l'activite choix couleur en mode modif
                Intent intentionChoixCouleur = new Intent( context , ActiviteChoixCouleur.class );
                Couleur couleur = modele.getLesCouleurs().get( position );
                intentionChoixCouleur.putExtra("a" , couleur.getA());
                intentionChoixCouleur.putExtra("r" , couleur.getR());
                intentionChoixCouleur.putExtra("v" , couleur.getV());
                intentionChoixCouleur.putExtra("b" , couleur.getB());
                intentionChoixCouleur.putExtra("nom" , couleur.getNom());
                (AdaptateurCouleur.this).positionEnCours = position;
                context.lancerActiviteChoixCouleurAvecIntent( intentionChoixCouleur );
            }
        });

        Button btnSupprimer = itemView.findViewById( R.id.btnSupprimer );
        btnSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Couleur couleur = modele.getLesCouleurs().get( position );
                String name = couleur.getNom();
                (AdaptateurCouleur.this).supprimerCouleur( name );
                context.formatListCouleur();
            }
        });


        int a = modele.getLesCouleurs().get( position ).getA();
        int r = modele.getLesCouleurs().get( position ).getR();
        int v = modele.getLesCouleurs().get( position ).getV();
        int b = modele.getLesCouleurs().get( position ).getB();

        tvCouleur.setBackgroundColor(Color.argb( a , r, v ,b ));
        tvNomCouleur.setText( ((Couleur)getItem( position )).getNom() );
        btnSupprimer.setTag( ((Couleur)getItem( position )).getId() );
        //itemView.setBackgroundColor(  Color.argb(255, 200,200,200));
        return itemView;
    }
}
