package fr.supavenir.lsts.tokens;

import java.util.ArrayList;

public class ModelListToken {

    private ArrayList<Token> tokens;

    public  ModelListToken() {
        tokens = new ArrayList<Token>();
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens( ArrayList<Token> tokens ) {
        this.tokens = tokens;
    }

    public void addToken( Token token ) {
        tokens.add( token );
    }
}
