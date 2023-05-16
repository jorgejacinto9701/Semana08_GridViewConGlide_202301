package com.example.semana07;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.example.semana07.adapter.ProductoAdapter;
import com.example.semana07.entity.Producto;
import com.example.semana07.service.ServiceProducto;
import com.example.semana07.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Boton
    Button btnListar;

    //ListView
    GridView gridProductos;
    ArrayList<Producto> data = new ArrayList<Producto>();
    ProductoAdapter adpatador;

    //Servicio
    ServiceProducto serviceProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListar = findViewById(R.id.btnLista);

        gridProductos = findViewById(R.id.gridProductos);
        adpatador = new ProductoAdapter(this, R.layout.activity_item_producto, data);
        gridProductos.setAdapter(adpatador);

        serviceProducto = ConnectionRest.getConnection().create(ServiceProducto.class);


        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaProductos();
            }
        });

        gridProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Producto objProducto = data.get(position);

                String title = objProducto.getTitle();
                String description = objProducto.getDescription();
                String category = objProducto.getCategory();
                double price = objProducto.getPrice();
                double rate = objProducto.getRating().getRate();
                int count = objProducto.getRating().getCount();

                String msg = description;
                msg += "\n\n";
                msg += "Category : " + category + "\n";
                msg += "Price : " + price + "\n";
                msg += "Rate : " + rate + "\n";
                msg += "Count : " + count + "\n";

                mensajeAlert(title, msg);
            }
        });
    }

    public void listaProductos(){
        Call<List<Producto>> call = serviceProducto.listaProducto();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful()){
                    List<Producto> lista = response.body();
                    data.clear();
                    data.addAll(lista);
                    adpatador.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {

            }
        });
    }

    public void mensajeAlert(String title, String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setTitle(title);
        alertDialog.setPositiveButton("Comprar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

}