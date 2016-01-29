package edu.upc.epe.laboratorio4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NuevoProducto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nuevo_producto, menu);
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

    public void GrabarProducto(View view) {
        new httprest().execute();

    }
    public class httprest extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... params) {
            Log.i("===>", "Dentro de doInBackground()");
            try {
                Map<String, String> data = new HashMap<>();
                EditText txtcategoria=(EditText)findViewById(R.id.txtcategoria);
                EditText txtnombre=(EditText)findViewById(R.id.txtnombre);
                EditText txtprecio=(EditText)findViewById(R.id.txtprecio);

                data.put("idCategoria", txtcategoria.getText().toString());
                data.put("nombre", txtnombre.getText().toString());
                data.put("precio", txtprecio.getText().toString());

                HttpRequest httpRequest = HttpRequest.post("http://192.168.1.67:8080/TrastiendaWeb/rest/productos");
                httpRequest.form(data);
                String respuesta = httpRequest.body().toString();

                Gson gson = new Gson();
                Type stringStringMap =  new TypeToken<Map<String, Object>>(){}.getType();
                final Map<String, Object> retorno = gson.fromJson(respuesta, stringStringMap);
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getApplicationContext(), "=>" + retorno.get("estado"), Toast.LENGTH_SHORT).show();

                    }
                });

            }
            catch (Exception ex)
            {
                Log.e("===>", "Error: " + ex);
            }
           return null;
        }
    }

    public void Retornar(View view) {
        Intent i =new Intent(this,BuscaProductos.class);
        startActivity(i);
    }
}
