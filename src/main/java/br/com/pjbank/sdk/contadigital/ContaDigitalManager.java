package br.com.pjbank.sdk.contadigital;

import br.com.pjbank.sdk.api.PJBankClient;
import br.com.pjbank.sdk.auth.PJBankAuthenticatedService;
import br.com.pjbank.sdk.exceptions.PJBankException;
import br.com.pjbank.sdk.models.common.Boleto;
import br.com.pjbank.sdk.models.contadigital.StatusAdministrador;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Vinícius Silva <vinicius.silva@superlogica.com>
 * @version 1.0
 * @since 1.0
 */
public class ContaDigitalManager extends PJBankAuthenticatedService {
    /**
     * EndPoint a ser requisitado na API
     */
    private String endPoint = "contadigital/{{credencial-conta}}";

    public ContaDigitalManager(String credencial, String chave) {
        super(credencial, chave);
    }

    /**
     * Realiza a emissão do boleto bancário para adicionar saldo à conta digital
     * @param valor: valor do saldo à ser adicionado
     * @return Boleto
     */
    public Boleto addBalance(double valor) throws IOException, PJBankException {
        PJBankClient client = new PJBankClient(this.endPoint.replace("{{credencial-conta}}", this.credencial));
        HttpPost httpPost = client.getHttpPostClient();
        httpPost.addHeader("x-chave-conta", this.chave);

        JSONObject params = new JSONObject();
        params.put("valor", valor);

        httpPost.setEntity(new StringEntity(params.toString(), StandardCharsets.UTF_8));

        String response = EntityUtils.toString(client.doRequest(httpPost).getEntity());

        JSONObject responseObject = new JSONObject(response).getJSONObject("data");

        return new Boleto(responseObject.getString("nosso_numero"),
                responseObject.getString("link_boleto"),
                responseObject.getString("linha_digitavel"));
    }

    /**
     * Adiciona uma pessoa física como administradora da conta digital
     * @param email: E-mail da pessoa física à ser adicionada como administradora
     * @return boolean
     */
    public boolean addAdmin(String email) throws IOException, PJBankException {
        PJBankClient client = new PJBankClient(this.endPoint.replace("{{credencial-conta}}", this.credencial).concat("/administradores"));
        HttpPost httpPost = client.getHttpPostClient();
        httpPost.addHeader("x-chave-conta", this.chave);

        JSONObject params = new JSONObject();
        params.put("email", email);

        httpPost.setEntity(new StringEntity(params.toString(), StandardCharsets.UTF_8));

        return client.doRequest(httpPost).getStatusLine().getStatusCode() == 200;
    }

    /**
     * Retorna o status de um administrador da conta digital
     * @param email: E-mail do administrador à ser consultado
     * @return StatusAdministrador
     */
    public StatusAdministrador getStatusAdmin(String email) throws IOException, PJBankException {
        PJBankClient client = new PJBankClient(this.endPoint.replace("{{credencial-conta}}", this.credencial).concat("/administradores/").concat(email));
        HttpGet httpGet = client.getHttpGetClient();
        httpGet.addHeader("x-chave-conta", this.chave);

        String response = EntityUtils.toString(client.doRequest(httpGet).getEntity());

        JSONObject responseObject = new JSONObject(response).getJSONObject("data");

        return new StatusAdministrador(responseObject.getInt("statusVinculo"), responseObject.getString("msg"));
    }

    /**
     * Remove um administrador da conta digital
     * @param email: E-mail do administrador à ser consultado
     * @return boolean
     */
    public boolean delAdmin(String email) throws IOException, PJBankException {
        PJBankClient client = new PJBankClient(this.endPoint.replace("{{credencial-conta}}", this.credencial).concat("/administradores/").concat(email));
        HttpDelete httpDelete = client.getHttpDeleteClient();
        httpDelete.addHeader("x-chave-conta", this.chave);

        return client.doRequest(httpDelete).getStatusLine().getStatusCode() == 200;
    }
}
