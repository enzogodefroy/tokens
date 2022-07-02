package fr.supavenir.lsts.tokens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class AdaptateurToken extends BaseAdapter {

    private ListToken context;
    private ModelListToken modele = new ModelListToken();
    private int positionEnCours = 0;

    public AdaptateurToken( Context context , ArrayList<Token> tokens ) {
        this.context = (ListToken) context;
        modele.setTokens( tokens );
    }

    /** On adapte les methodes pour visualiser le modèle en mémoire. */

    @Override
    public int getCount() {
        return modele.getTokens().size();
    }

    @Override
    public Object getItem(int position) {
        return modele.getTokens().get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Important : le code pour recuperer la vue de l'item par son layout
        View itemView = LayoutInflater.from( context ).inflate(R.layout.list_token_item,
                parent , false );

        TextView tvTokenName = itemView.findViewById( R.id.tvTokenName );
        TextView tvTokenActualPrice = itemView.findViewById( R.id.tvTokenActualPrice );
        TextView tvTokenLowerPrice = itemView.findViewById( R.id.tvTokenLowerPrice );
        TextView tvTokenHigherPrice = itemView.findViewById( R.id.tvTokenHigherPrice );


        Token token = modele.getTokens().get(position);
        String name = token.getName();
        float actualPrice = token.getActualPrice();
        float lowerPrice = token.getLowerPrice();
        float higherPrice = token.getHigherPrice();

        tvTokenName.setText(name);
        tvTokenActualPrice.setText(String.valueOf(actualPrice));
        tvTokenLowerPrice.setText(String.valueOf(lowerPrice));
        tvTokenHigherPrice.setText(String.valueOf(higherPrice));
        return itemView;
    }
}
