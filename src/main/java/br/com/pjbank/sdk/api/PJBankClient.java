package br.com.pjbank.sdk.api;

import br.com.pjbank.sdk.exceptions.PJBankException;
import br.com.pjbank.sdk.exceptions.PJBankExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Vinícius Silva <vinicius.silva@superlogica.com>
 * @version 1.0
 * @since 1.0
 */
public class PJBankClient {

    /**
     * URL completa a ser requisitado na API
     */
    private String absolutePath;

    public PJBankClient(String endPoint) {
        if (StringUtils.isBlank(endPoint))
            throw new IllegalArgumentException("Endpoint não informado");

        this.absolutePath = PJBankConfig.getApiBaseUrl().concat(endPoint);

        System.setProperty("jsse.enableSNIExtension", "true");
    }

    /**
     * Retorna uma instância do HttpGet pré-configurada
     * @return HttpGet
     */
    public HttpGet getHttpGetClient() {
        HttpGet client = new HttpGet(this.absolutePath);
        client.addHeader("Accept", PJBankConfig.accept);
        client.addHeader("Content-Type", PJBankConfig.contentType);
        client.addHeader("Source", PJBankConfig.source);

        return client;
    }

    /**
     * Retorna uma instância do HttpPost pré-configurada
     * @return HttpPost
     */
    public HttpPost getHttpPostClient() {
        HttpPost client = new HttpPost(this.absolutePath);
        client.addHeader("Accept", PJBankConfig.accept);
        client.addHeader("Content-Type", PJBankConfig.contentType);
        client.addHeader("Source", PJBankConfig.source);

        return client;
    }

    /**
     * Retorna uma instância do HttpPut pré-configurada
     * @return HttpPut
     */
    public HttpPut getHttpPutClient() {
        HttpPut client = new HttpPut(this.absolutePath);
        client.addHeader("Accept", PJBankConfig.accept);
        client.addHeader("Content-Type", PJBankConfig.contentType);
        client.addHeader("Source", PJBankConfig.source);

        return client;
    }

    /**
     * Retorna uma instância do HttpDelete pré-configurada
     * @return HttpDelete
     */
    public HttpDelete getHttpDeleteClient() {
        HttpDelete client = new HttpDelete(this.absolutePath);
        client.addHeader("Accept", PJBankConfig.accept);
        client.addHeader("Content-Type", PJBankConfig.contentType);
        client.addHeader("Source", PJBankConfig.source);

        return client;
    }

    /**
     * Retorna uma instância do HttpClient
     * @return HttpClient
     */
    private HttpClient getHttpClient() {
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .build();

            SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1.2"},
                    null,
                    new DefaultHostnameVerifier());

            return HttpClients.custom()
                    .setSSLSocketFactory(f)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();

            return HttpClientBuilder.create().build();
        }
    }

    /**
     * Executa a requisição e retorna o Response
     * @param  httpRequestClient Objeto HttpGet, HttpPost, HttpPut ou HttpDelete com as informações da requisição
     * @return HttpResponse
     * @throws IOException Em caso de problema ou conexão abortada
     * @throws PJBankException Em caso de retorno de erro na solicitação por parte da API (ex: 400, 401, 403, 404, 500, 503)
     */
    public HttpResponse doRequest(HttpRequestBase httpRequestClient) throws IOException, PJBankException {
        HttpClient client = this.getHttpClient();

        HttpResponse response = client.execute(httpRequestClient);

        if (response.getStatusLine().getStatusCode() >= 400)
            throw PJBankExceptionHandler.handleFromJSONResponse(EntityUtils.toString(response.getEntity()), response.getStatusLine().getStatusCode());

        return response;
    }
}
