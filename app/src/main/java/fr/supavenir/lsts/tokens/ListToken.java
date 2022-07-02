package fr.supavenir.lsts.tokens;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.supavenir.lsts.tokens.R;

public class ListToken extends AppCompatActivity {

    private ListView lvListToken;
    private AdaptateurToken adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_list_tokens);

        // clearDb();

        ArrayList<String> tokens = new ArrayList<String>() {
            {
                add("BTC");
                add("ETH");
                add("SOL");
                add("AVAX");
                add("USDT");
                add("MANA");
            }
        };

        for (int i = 0; i < tokens.size(); i++) {
            initializeApp(tokens.get(i));
        }
    }

    private void initializeApp(String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.coinbase.com/v2/prices/" + token + "-EUR/buy", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    DBHelper dbHelper = new DBHelper(ListToken.this);
                    Token tokenObj= dbHelper.getToken(token);

                    JSONObject obj = response.getJSONObject("data");
                    float price = BigDecimal.valueOf(obj.getDouble("amount")).floatValue();
                    tokenObj.setActualPrice(price);

                    if (tokenObj.getHigherPrice() == 0 || tokenObj.getHigherPrice() < price)
                    {
                        tokenObj.setHigherPrice(price);
                    }
                    if (tokenObj.getLowerPrice() == 0 || tokenObj.getLowerPrice() > price)
                    {
                        tokenObj.setLowerPrice(price);
                    }

                    if(dbHelper.doesTokenExists(tokenObj.getName()))
                    {
                        Log.i("ListToken", "UPDATE");
                        dbHelper.updateTokenByName(tokenObj, tokenObj.getName());
                    }
                    else {
                        Log.i("ListToken", "ADD");
                        dbHelper.addToken(tokenObj);
                    }

                    formatListToken();
                } catch (Exception e) {
                    Log.e("ListToken", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ListToken", error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void formatListToken() {
        adaptateur = new AdaptateurToken(this, getTokensFromDB());
        lvListToken = findViewById(R.id.lvTokens);
        lvListToken.setAdapter(adaptateur);
    }

    private void clearDb() {
        DBHelper dbHelper = new DBHelper(ListToken.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Token;");
    }

    @SuppressLint("Range")
    private ArrayList<Token> getTokensFromDB() {

        String sqlQuery = "Select * from Token order by name";
        DBHelper dbHelper = new DBHelper(ListToken.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);
        ArrayList<Token> tokens = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex("name"));
            float actualPrice = cursor.getFloat(cursor.getColumnIndex("actualPrice"));
            float lowerPrice = cursor.getFloat(cursor.getColumnIndex("lowerPrice"));
            float higherPrice = cursor.getFloat(cursor.getColumnIndex("higherPrice"));

            tokens.add(new Token(name, actualPrice, lowerPrice, higherPrice));
        }
        return tokens;
    }


}