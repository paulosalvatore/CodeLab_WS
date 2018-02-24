package paulosalvatore.com.br.codelab_ws;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
    }

    public RequestQueue getQueue(Context c) {
        if (queue == null)
            queue = Volley.newRequestQueue(c.getApplicationContext());

        return queue;
    }

    public void buscaDados(View v) {
        queue = getQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=1a820804691945ee81cb42d64dab5f99",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Usar a resposta, já estamos na thread principal
                        textView.setText(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Previsao objeto = new Previsao();
                            JSONObject jsonObject2 = jsonObject.getJSONObject("main");
                            objeto.Temperatura = jsonObject2.getInt("temp");
                            JSONArray jsonObject3 = jsonObject.getJSONArray("weather");
                            objeto.Condicao = jsonObject3.getJSONObject(0).getString("description");
                            textView.setText(
                                    "Temperatura: " + objeto.Temperatura + "\n\n" +
                                    "Condição: " + objeto.Condicao
                            );
                        } catch (JSONException ex) {
                            String erro = ex.getMessage();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // O statusCode HTTP está disponível em error
                        // assim como algumas outras informações
                    }
                });

        queue.add(stringRequest);
    }
}
