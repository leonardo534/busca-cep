package com.leonardosilva.conferecep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView textoResultado;
    private EditText editCep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoResultado = findViewById(R.id.textoResultado);
        editCep = findViewById(R.id.editCep);
    }

    public void buscarCep(View view) {
        if(editCep.getText().toString().length() == 8 ) {
            MyTask task = new MyTask();
            String urlApiCep = "https://viacep.com.br/ws/" + editCep.getText().toString() + "/json";
            task.execute(urlApiCep);
        }else{
            Toast.makeText(this, "Cep inválido ou incompleto", Toast.LENGTH_SHORT).show();
        }
    }

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader bufferedReader = null;
            StringBuffer buffer = null;
            try {
                URL url = new URL(strings[0]); //BUSCA A URL COLOCADA NO task.execute()
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection(); // Abre a conexão com a API

                InputStream inputStream = conexao.getInputStream(); // Recupera os dados em Bytes

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //Decodifica os dados de Bytes para Caracteres

                bufferedReader = new BufferedReader(inputStreamReader); //Coloca em caracteres que seja possivel a compreensão
                String linha = "";
                buffer = new StringBuffer();
                while((linha = bufferedReader.readLine()) != null){ //Enquanto o bufferedReader possuir dados ele vai ler uma linha e jogar na variavel Linha
                    buffer.append(linha); //Buffer.append não sobrepõe o arquivo cada vez que é colocado uma linha, mas ele coloca um embaixo do outro
                    /* Exemplo: caneta
                                lapis
                                folha*/

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString(); //Retorna os dados coletados
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String cep = null;
            String logradouro = null;
            String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;

            try {
                JSONObject jsonObject = new JSONObject(s); // Converte para JSON os dados buscados

                cep = jsonObject.getString("cep"); // Variavel cep armazena tudo que tiver valor "cep" na arvore JSON
                logradouro = jsonObject.getString("logradouro");
                complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");


            } catch (JSONException e) {
                e.printStackTrace();
            }


            textoResultado.setText("CEP: "+cep+"\nLogradouro: "+logradouro+"\nComplemento: "+complemento+"\nBairro: "+bairro+"\nLocalidade: "
                    +localidade+"\nUF: "+uf);
        }
    }
}
