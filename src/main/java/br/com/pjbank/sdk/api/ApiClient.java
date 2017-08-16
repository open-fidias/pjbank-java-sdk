package br.com.pjbank.sdk.api;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

/**
 * @author Vinícius Silva
 * @version 1.0
 * @since 1.0
 */
public class ApiClient {

    /**
     * URL completa a ser requisitado na API
     */
    private String absolutePath;

    public ApiClient(String endPoint) {
        if (StringUtils.isBlank(endPoint))
            throw new IllegalArgumentException("Endpoint não informado");

        this.absolutePath = ApiConfig.apiBaseUrl.concat(endPoint);
    }

    /**
     * Retorna uma instância do HttpGet pré-configurada
     * @return HttpGet
     */
    public HttpGet getHttpGetClient() {
        HttpGet client = new HttpGet(this.absolutePath);
        client.addHeader("Accept", ApiConfig.accept);
        client.addHeader("Content-Type", ApiConfig.contentType);

        return client;
    }

    /**
     * Retorna uma instância do HttpPost pré-configurada
     * @return HttpPost
     */
    public HttpPost getHttpPostClient() {
        HttpPost client = new HttpPost(this.absolutePath);
        client.addHeader("Accept", ApiConfig.accept);
        client.addHeader("Content-Type", ApiConfig.contentType);

        return client;
    }

    /**
     * Retorna uma instância do HttpPut pré-configurada
     * @return HttpPut
     */
    public HttpPut getHttpPutClient() {
        HttpPut client = new HttpPut(this.absolutePath);
        client.addHeader("Accept", ApiConfig.accept);
        client.addHeader("Content-Type", ApiConfig.contentType);

        return client;
    }

    /**
     * Retorna uma instância do HttpDelete pré-configurada
     * @return HttpDelete
     */
    public HttpDelete getHttpDeleteClient() {
        HttpDelete client = new HttpDelete(this.absolutePath);
        client.addHeader("Accept", ApiConfig.accept);
        client.addHeader("Content-Type", ApiConfig.contentType);

        return client;
    }
}