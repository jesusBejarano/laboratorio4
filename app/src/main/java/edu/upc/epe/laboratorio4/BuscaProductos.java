package edu.upc.epe.laboratorio4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class BuscaProductos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_productos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_busca_productos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void BuscarProdcto(View view) {

        new httprest().execute();
    }

    public void CargarNuevo(View view) {
        Intent i =new Intent(this,NuevoProducto.class);
        startActivity(i);

    }

    public class httprest extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("===>", "Dentro de doInBackground()");

            final ListView lstProductos = (ListView)findViewById(R.id.listProductos);
            TextView txtProducto = (TextView)findViewById(R.id.etCategoria );

            try {
                HttpRequest httpRequest = HttpRequest.get("http://192.168.1.67:8080/TrastiendaWeb/rest/productos/" + txtProducto.getText());

                String respuesta = httpRequest.body().toString();

                Log.i("===>", respuesta);

                Gson gson = new Gson();
                Type stringStringMap =  new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
                final ArrayList<Map<String, Object>> retorno = gson.fromJson(respuesta, stringStringMap);

                final String[] matriz = new String[retorno.size()];
                int i = 0;

                for(Map<String, Object> x : retorno){
                    matriz[i++] = (String)(x.get("nombre") +  " - S/. " + x.get("precio"));
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                BuscaProductos.this,
                                android.R.layout.simple_list_item_1,
                                matriz);
                        lstProductos.setAdapter(adaptador);
                    }
                });

            } catch (Exception ex) {
                Log.e("===>", "Error: " + ex);
            }
            return null;

        }
    }

}

