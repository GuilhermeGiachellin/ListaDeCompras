package com.example.listadecompras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CadastrarLista extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private ListView listViewProdutos;
    private EditText editTextNomeLista;
    private TextView valorTextView;
    private String produtoSelecionado;
    private long listaId;
    private ArrayList<Long> arrayIds;
    private ArrayList<Double> arrayValor = new ArrayList<>();
    private ArrayList<Long> produtoIdArray = new ArrayList<>();
    private double valorTotal = 0.0;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_lista);

        listViewProdutos = (ListView) this.findViewById(R.id.listViewProdutos);
        editTextNomeLista = (EditText) this.findViewById(R.id.editTextNomeLista);
        valorTextView = (TextView) this.findViewById(R.id.valorTextView);
        valorTextView.setText(Double.toString(valorTotal));
        listarProdutos();

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                produtoSelecionado = (String) (listViewProdutos.getItemAtPosition(position));
                produtoIdArray.add(arrayIds.get(position));
                mensagemSelecao();
                somaValor(position);
            }
        });
    }

    public void adcionarNovaLista() {
        if(editTextNomeLista.getText().toString().trim().equals("")) {
            Uteis.Alert(this, "Nome da lista é obrigatório");
            editTextNomeLista.requestFocus();

        } else if(produtoSelecionado.isEmpty()) {
            Uteis.Alert(this, "Selecione um produto");

        } else {
            try {
                bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);


                String sql = "INSERT INTO lista (nome, data) VALUES(?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);

                stmt.bindString(1, editTextNomeLista.getText().toString());
                stmt.bindString(2, String.valueOf(Calendar.getInstance().getTime()));
                //EXECUTA INSERT E PEGA A ID DA NOVA LISTA
                listaId = stmt.executeInsert();

                //INSERE NA TABELA DE LIGACAO
                for(int i = 0; i < produtoIdArray.size(); i++) {
                    sql = "INSERT INTO listaComprasProdutos (produto_id, lista_id) VALUES(?, ?)";
                    stmt = bancoDados.compileStatement(sql);

                    stmt.bindLong(1, produtoIdArray.get(i));
                    stmt.bindLong(2, listaId);
                    stmt.executeInsert();
                }

                bancoDados.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listarProdutos() {
        try {
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome, valor FROM produto", null);
            ArrayList<String> linhasDados = new ArrayList<String>();

            ArrayAdapter adapterDados = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhasDados
            );

            listViewProdutos.setAdapter(adapterDados);
            meuCursor.moveToFirst();
            while(meuCursor != null) {
                arrayIds.add(meuCursor.getLong(0));
                linhasDados.add(meuCursor.getString(1));
                arrayValor.add(meuCursor.getDouble(2));
                meuCursor.moveToNext();
            }

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void somaValor(int position) {
        valorTotal += arrayValor.get(position);
        df.format(valorTotal);
        valorTextView.setText(Double.toString(valorTotal));
    }

    public void salvarListaButton(View v) {
        adcionarNovaLista();
        Uteis.Alert(this,"LISTA SALVA");
        limparCampos();
        startActivity(new Intent(this, MainActivity.class));

    }

    public void mensagemSelecao() {
        Toast.makeText(this, produtoSelecionado + " Selecionado", Toast.LENGTH_LONG).show();
    }

    public void limparCampos() {
        produtoSelecionado = "";
        valorTotal = 0.0;
        arrayValor.clear();
        arrayIds.clear();
        editTextNomeLista.setText(null);
    }
}