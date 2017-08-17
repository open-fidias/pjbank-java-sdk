package br.com.pjbank.sdk.utils;

import br.com.pjbank.sdk.api.ApiClient;
import br.com.pjbank.sdk.models.Banco;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bancos {
    /**
     * EndPoint a ser requisitado na API
     */
    private String endPoint = "bancos";

    public List<Banco> get() throws IOException, JSONException {
        ApiClient client = new ApiClient(endPoint);
        HttpGet httpGet = client.getHttpGetClient();

        String response = EntityUtils.toString(client.doRequest(httpGet).getEntity());

        JSONObject responseObject = new JSONObject(response);
        JSONObject bancosObject = responseObject.getJSONObject("bancos");

        List<Banco> bancos = new ArrayList<>();

        Iterator<String> keys = bancosObject.keys();
        while (keys.hasNext()) {
            String codigo = keys.next();
            Banco banco = new Banco(codigo, bancosObject.getString(codigo));
            bancos.add(banco);
        }

        return bancos;
    }
}