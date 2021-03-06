package br.com.pjbank.sdk.contadigital;

import br.com.pjbank.sdk.api.PJBankConfigTest;
import br.com.pjbank.sdk.enums.FormatoExtrato;
import br.com.pjbank.sdk.enums.TipoAnexo;
import br.com.pjbank.sdk.enums.TipoConta;
import br.com.pjbank.sdk.exceptions.PJBankException;
import br.com.pjbank.sdk.models.common.Boleto;
import br.com.pjbank.sdk.models.contadigital.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.*;

/**
 * @author Vinícius Silva <vinicius.silva@superlogica.com>
 * @version 1.0
 * @since 1.0
 */
public class ContaDigitalManagerTest {
    private String credencial;
    private String chave;

    @Before
    public void init() {
        this.credencial = PJBankConfigTest.credencialContaDigital;
        this.chave = PJBankConfigTest.chaveContaDigital;
    }

    @Test
    public void addBalanceComValorValido() throws IOException, JSONException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        Boleto boleto = contaDigitalManager.addBalance(25.50);

        Assert.assertThat(boleto.getIdUnico(), not(is(emptyOrNullString())));
        Assert.assertThat(boleto.getLinkBoleto(), not(is(emptyOrNullString())));
        Assert.assertThat(boleto.getLinhaDigitavel(), not(is(emptyOrNullString())));
    }

    @Test(expected = PJBankException.class)
    public void addBalanceComValorMenorQueVinteECincoReais() throws IOException, JSONException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        Boleto boleto = contaDigitalManager.addBalance(1.50);

        Assert.assertThat(boleto.getIdUnico(), not(is(emptyOrNullString())));
        Assert.assertThat(boleto.getLinkBoleto(), not(is(emptyOrNullString())));
        Assert.assertThat(boleto.getLinhaDigitavel(), not(is(emptyOrNullString())));
    }

    @Test
    @Ignore
    public void addAdmin() throws IOException, JSONException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        contaDigitalManager.addAdmin("api@pjbank.com.br");
    }

    @Test
    public void getStatusAdmin() throws IOException, JSONException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        Assert.assertThat(contaDigitalManager.getStatusAdmin("api@pjbank.com.br"), not(is(emptyOrNullString())));
    }

    @Test
    @Ignore
    public void delAdmin() throws IOException, JSONException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        contaDigitalManager.delAdmin("api@pjbank.com.br");
    }

    @Test
    public void expenseBarcodePaymentComUmaDespesaComoLista() throws IOException, JSONException, ParseException, PJBankException {
        List<TransacaoCodigoBarras> despesas = new ArrayList<>();

        TransacaoCodigoBarras despesa = new TransacaoCodigoBarras();
        despesa.setDataPagamento(new Date());
        despesa.setDataVencimento(new Date());
        despesa.setValor(1.50);
        despesa.setCodigoBarras("03399699255870000105853613001014281190000005075");

        despesas.add(despesa);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponsePagamento responsePagamento = contaDigitalManager.expenseBarcodePayment(despesas).get(0);

        Assert.assertThat(responsePagamento.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(responsePagamento.getDataPagamento(), not(is(nullValue())));
    }

    @Test
    public void expenseBarcodePaymentComUmaDespesaComoObjeto() throws IOException, JSONException, ParseException, PJBankException {
        TransacaoCodigoBarras despesa = new TransacaoCodigoBarras();
        despesa.setDataPagamento(new Date());
        despesa.setDataVencimento(new Date());
        despesa.setValor(1.50);
        despesa.setCodigoBarras("03399699255870000105853613001014281190000005075");

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponsePagamento responsePagamento = contaDigitalManager.expenseBarcodePayment(despesa);

        Assert.assertThat(responsePagamento.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(responsePagamento.getDataPagamento(), not(is(nullValue())));
    }

    @Test
    public void expenseBarcodePaymentComMaisDeUmaDespesa() throws IOException, JSONException, ParseException, PJBankException {
        List<TransacaoCodigoBarras> despesas = new ArrayList<>();

        TransacaoCodigoBarras despesa = new TransacaoCodigoBarras();
        despesa.setDataPagamento(new Date());
        despesa.setDataVencimento(new Date());
        despesa.setValor(1.50);
        despesa.setCodigoBarras("03399699255870000105853613001014281190000005075");

        despesas.add(despesa);

        despesa = new TransacaoCodigoBarras();
        despesa.setDataPagamento(new Date());
        despesa.setDataVencimento(new Date());
        despesa.setValor(1.50);
        despesa.setCodigoBarras("03399699255870000105853613001014281190000005076");
        despesas.add(despesa);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponsePagamento responsePagamento = contaDigitalManager.expenseBarcodePayment(despesas).get(0);

        Assert.assertThat(responsePagamento.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(responsePagamento.getDataPagamento(), not(is(nullValue())));
    }


    @Test
    public void docTedTransferComUmaTransferenciaComoLista() throws IOException, JSONException, ParseException, PJBankException {
        List<TransacaoTransferencia> transferencias = new ArrayList<>();

        TransacaoTransferencia transacaoTransferencia = new TransacaoTransferencia();
        transacaoTransferencia.setDataPagamento(new Date());
        transacaoTransferencia.setDataVencimento(new Date());
        transacaoTransferencia.setValor(1.50);
        transacaoTransferencia.setBancoFavorecido("033");
        transacaoTransferencia.setAgenciaFavorecido("1111");
        transacaoTransferencia.setContaFavorecido("11111");
        transacaoTransferencia.setNomeFavorecido("Cliente Exemplo");
        transacaoTransferencia.setCnpjFavorecido("45475754000136");
        transacaoTransferencia.setIdentificador("123123");
        transacaoTransferencia.setDescricao("Descrição de exemplo");
        transacaoTransferencia.setSolicitante("Teste DOC/TED");
        transacaoTransferencia.setTipoContaFavorecido(TipoConta.CORRENTE);

        transferencias.add(transacaoTransferencia);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponseTransferencia responseTransferencia = contaDigitalManager.docTedTransfer(transferencias).get(0);

        Assert.assertThat(responseTransferencia.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getIdentificador(), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getDataPagamento(), not(is(nullValue())));
    }

    @Test
    public void docTedTransferComUmaTransferenciaComoObjeto() throws IOException, JSONException, ParseException, PJBankException {
        TransacaoTransferencia transacaoTransferencia = new TransacaoTransferencia();
        transacaoTransferencia.setDataPagamento(new Date());
        transacaoTransferencia.setDataVencimento(new Date());
        transacaoTransferencia.setValor(1.50);
        transacaoTransferencia.setBancoFavorecido("033");
        transacaoTransferencia.setAgenciaFavorecido("1111");
        transacaoTransferencia.setContaFavorecido("11111");
        transacaoTransferencia.setNomeFavorecido("Cliente Exemplo");
        transacaoTransferencia.setCnpjFavorecido("45475754000136");
        transacaoTransferencia.setIdentificador("123123");
        transacaoTransferencia.setDescricao("Descrição de exemplo");
        transacaoTransferencia.setSolicitante("Teste DOC/TED");
        transacaoTransferencia.setTipoContaFavorecido(TipoConta.CORRENTE);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponseTransferencia responseTransferencia = contaDigitalManager.docTedTransfer(transacaoTransferencia);

        Assert.assertThat(responseTransferencia.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getIdentificador(), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getDataPagamento(), not(is(nullValue())));
    }

    @Test
    public void docTedTransferComMaisDeUmaTransferencia() throws IOException, JSONException, ParseException, PJBankException {
        List<TransacaoTransferencia> transferencias = new ArrayList<>();

        TransacaoTransferencia transacaoTransferencia = new TransacaoTransferencia();
        transacaoTransferencia.setDataPagamento(new Date());
        transacaoTransferencia.setDataVencimento(new Date());
        transacaoTransferencia.setValor(1.50);
        transacaoTransferencia.setBancoFavorecido("033");
        transacaoTransferencia.setAgenciaFavorecido("1111");
        transacaoTransferencia.setContaFavorecido("11111");
        transacaoTransferencia.setNomeFavorecido("Cliente Exemplo");
        transacaoTransferencia.setCnpjFavorecido("45475754000136");
        transacaoTransferencia.setIdentificador("123123");
        transacaoTransferencia.setDescricao("Descrição de exemplo");
        transacaoTransferencia.setSolicitante("Teste DOC/TED");
        transacaoTransferencia.setTipoContaFavorecido(TipoConta.CORRENTE);

        transferencias.add(transacaoTransferencia);

        transacaoTransferencia = new TransacaoTransferencia();
        transacaoTransferencia.setDataPagamento(new Date());
        transacaoTransferencia.setDataVencimento(new Date());
        transacaoTransferencia.setValor(1.50);
        transacaoTransferencia.setBancoFavorecido("033");
        transacaoTransferencia.setAgenciaFavorecido("1111");
        transacaoTransferencia.setContaFavorecido("11111");
        transacaoTransferencia.setNomeFavorecido("Cliente Exemplo");
        transacaoTransferencia.setCnpjFavorecido("45475754000136");
        transacaoTransferencia.setIdentificador("123123");
        transacaoTransferencia.setDescricao("Descrição de exemplo");
        transacaoTransferencia.setSolicitante("Teste DOC/TED");
        transacaoTransferencia.setTipoContaFavorecido(TipoConta.CORRENTE);

        transferencias.add(transacaoTransferencia);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponseTransferencia responseTransferencia = contaDigitalManager.docTedTransfer(transferencias).get(0);

        Assert.assertThat(responseTransferencia.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getIdentificador(), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getDataPagamento(), not(is(nullValue())));
    }

    @Test
    public void delTransaction() throws IOException, JSONException, ParseException, PJBankException {
        TransacaoTransferencia transacaoTransferencia = new TransacaoTransferencia();
        transacaoTransferencia.setDataPagamento(new Date());
        transacaoTransferencia.setDataVencimento(new Date());
        transacaoTransferencia.setValor(1.50);
        transacaoTransferencia.setBancoFavorecido("033");
        transacaoTransferencia.setAgenciaFavorecido("1111");
        transacaoTransferencia.setContaFavorecido("11111");
        transacaoTransferencia.setNomeFavorecido("Cliente Exemplo");
        transacaoTransferencia.setCnpjFavorecido("45475754000136");
        transacaoTransferencia.setIdentificador("123123");
        transacaoTransferencia.setDescricao("Descrição de exemplo");
        transacaoTransferencia.setSolicitante("Teste DOC/TED");
        transacaoTransferencia.setTipoContaFavorecido(TipoConta.CORRENTE);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponseTransferencia responseTransferencia = contaDigitalManager.docTedTransfer(transacaoTransferencia);

        contaDigitalManager.delTransaction(responseTransferencia.getIdOperacao());
    }

    @Test
    public void delTransactions() throws IOException, JSONException, ParseException, PJBankException {
        List<TransacaoTransferencia> transferencias = new ArrayList<>();

        TransacaoTransferencia transacaoTransferencia = new TransacaoTransferencia();
        transacaoTransferencia.setDataPagamento(new Date());
        transacaoTransferencia.setDataVencimento(new Date());
        transacaoTransferencia.setValor(1.50);
        transacaoTransferencia.setBancoFavorecido("033");
        transacaoTransferencia.setAgenciaFavorecido("1111");
        transacaoTransferencia.setContaFavorecido("11111");
        transacaoTransferencia.setNomeFavorecido("Cliente Exemplo");
        transacaoTransferencia.setCnpjFavorecido("45475754000136");
        transacaoTransferencia.setIdentificador("123123");
        transacaoTransferencia.setDescricao("Descrição de exemplo");
        transacaoTransferencia.setSolicitante("Teste DOC/TED");
        transacaoTransferencia.setTipoContaFavorecido(TipoConta.CORRENTE);

        transferencias.add(transacaoTransferencia);

        transacaoTransferencia = new TransacaoTransferencia();
        transacaoTransferencia.setDataPagamento(new Date());
        transacaoTransferencia.setDataVencimento(new Date());
        transacaoTransferencia.setValor(1.50);
        transacaoTransferencia.setBancoFavorecido("033");
        transacaoTransferencia.setAgenciaFavorecido("1111");
        transacaoTransferencia.setContaFavorecido("11111");
        transacaoTransferencia.setNomeFavorecido("Cliente Exemplo");
        transacaoTransferencia.setCnpjFavorecido("45475754000136");
        transacaoTransferencia.setIdentificador("123123");
        transacaoTransferencia.setDescricao("Descrição de exemplo");
        transacaoTransferencia.setSolicitante("Teste DOC/TED");
        transacaoTransferencia.setTipoContaFavorecido(TipoConta.CORRENTE);

        transferencias.add(transacaoTransferencia);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponseTransferencia responseTransferencia = contaDigitalManager.docTedTransfer(transferencias).get(0);

        Set<String> idsTransacoes = new HashSet<>();
        idsTransacoes.add(responseTransferencia.getIdOperacao());

        responseTransferencia = contaDigitalManager.docTedTransfer(transferencias).get(1);
        idsTransacoes.add(responseTransferencia.getIdOperacao());

        contaDigitalManager.delTransactions(idsTransacoes);
    }

    @Test
    public void get() throws IOException, JSONException, ParseException, URISyntaxException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        List<TransacaoExtrato> transacoesExtrato = contaDigitalManager.get(dateFormat.parse("08/01/2017"), dateFormat.parse("09/01/2017"), FormatoExtrato.JSON);

        // O teste só será executado caso haja algum registro no extrato, do contrário não há como testar
        // Utiliza-se o período de 01/08/2017 à 01/09/2017 pois é sabido que atualmente há registros nesse período
        // porém no futuro isso pode ser alterado.
        if (transacoesExtrato.size() > 0) {
            TransacaoExtrato transacaoExtrato = transacoesExtrato.get(0);
            Assert.assertThat(transacaoExtrato.getIdTransacao(), not(is(emptyOrNullString())));
            Assert.assertThat(transacaoExtrato.getDataPagamento(), not(is(nullValue())));
            Assert.assertThat(String.valueOf(transacaoExtrato.getValor()), not(is(emptyOrNullString())));
            Assert.assertThat(transacaoExtrato.getHistorico(), not(is(emptyOrNullString())));
            Assert.assertThat(transacaoExtrato.getTipo(), not(is(nullValue())));
        }
    }

    @Test
    public void accountToSubaccountTransferComUmaTransferenciaComoObjeto() throws IOException, JSONException, ParseException, PJBankException {
        TransacaoTransferenciaInterna transacaoTransferenciaInterna = new TransacaoTransferenciaInterna();
        transacaoTransferenciaInterna.setValor(1.50);
        transacaoTransferenciaInterna.setDataVencimento(new Date());
        transacaoTransferenciaInterna.setContaDestino("b2240b16b373446935a2a7ab437577a823f22eaa");

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponseTransferencia responseTransferencia = contaDigitalManager.accountToSubaccountTransfer(transacaoTransferenciaInterna);
        Assert.assertThat(responseTransferencia.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(String.valueOf(responseTransferencia.getStatus()), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getMessage(), not(is(emptyOrNullString())));
    }

    @Test
    public void accountToSubaccountTransferComUmaTransferenciaComoLista() throws IOException, JSONException, ParseException, PJBankException {
        List<TransacaoTransferenciaInterna> transacoesTransferenciasContaSubconta = new ArrayList<>();

        TransacaoTransferenciaInterna transacaoTransferenciaInterna = new TransacaoTransferenciaInterna();
        transacaoTransferenciaInterna.setValor(1.50);
        transacaoTransferenciaInterna.setDataVencimento(new Date());
        transacaoTransferenciaInterna.setContaDestino("b2240b16b373446935a2a7ab437577a823f22eaa");

        transacoesTransferenciasContaSubconta.add(transacaoTransferenciaInterna);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponseTransferencia responseTransferencia = contaDigitalManager.accountToSubaccountTransfer(transacoesTransferenciasContaSubconta).get(0);
        Assert.assertThat(responseTransferencia.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(String.valueOf(responseTransferencia.getStatus()), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getMessage(), not(is(emptyOrNullString())));
    }

    @Test
    public void accountToSubaccountTransferComMaisDeUmaTransferencia() throws IOException, JSONException, ParseException, PJBankException {
        List<TransacaoTransferenciaInterna> transacoesTransferenciasContaSubconta = new ArrayList<>();

        TransacaoTransferenciaInterna transacaoTransferenciaInterna = new TransacaoTransferenciaInterna();
        transacaoTransferenciaInterna.setValor(1.50);
        transacaoTransferenciaInterna.setDataVencimento(new Date());
        transacaoTransferenciaInterna.setContaDestino("b2240b16b373446935a2a7ab437577a823f22eaa");

        transacoesTransferenciasContaSubconta.add(transacaoTransferenciaInterna);

        transacaoTransferenciaInterna = new TransacaoTransferenciaInterna();
        transacaoTransferenciaInterna.setValor(1.50);
        transacaoTransferenciaInterna.setDataVencimento(new Date());
        transacaoTransferenciaInterna.setContaDestino("b2240b16b373446935a2a7ab437577a823f22eaa");

        transacoesTransferenciasContaSubconta.add(transacaoTransferenciaInterna);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        ResponseTransferencia responseTransferencia = contaDigitalManager.accountToSubaccountTransfer(transacoesTransferenciasContaSubconta).get(0);
        Assert.assertThat(responseTransferencia.getIdOperacao(), not(is(emptyOrNullString())));
        Assert.assertThat(String.valueOf(responseTransferencia.getStatus()), not(is(emptyOrNullString())));
        Assert.assertThat(responseTransferencia.getMessage(), not(is(emptyOrNullString())));
    }

    @Test
    @Ignore
    public void manageWebhookURL() throws IOException, JSONException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        Assert.assertTrue(contaDigitalManager.manageWebhookURL("https://pjbank.com.br"));
    }

    @Test
    public void getTransactionFilesSemTipoEspecificado() throws IOException, JSONException, URISyntaxException, ParseException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        List<AnexoTransacao> anexosTransacao = contaDigitalManager.getTransactionFiles("1259", null);

        // O teste só será executado caso haja algum anexo na transação, do contrário não há como testar
        if (anexosTransacao.size() > 0) {
            AnexoTransacao anexoTransacao = anexosTransacao.get(0);
            Assert.assertThat(anexoTransacao.getUrl(), not(is(emptyOrNullString())));
            Assert.assertThat(anexoTransacao.getTipo(), not(is(nullValue())));
            Assert.assertThat(anexoTransacao.getNome(), not(is(emptyOrNullString())));
            Assert.assertThat(anexoTransacao.getFormato(), not(is(nullValue())));
            Assert.assertThat(String.valueOf(anexoTransacao.getTamanho()), not(is(emptyOrNullString())));
            Assert.assertThat(anexoTransacao.getData(), not(is(nullValue())));
        }
    }

    @Test
    public void getTransactionFilesComTipoEspecificado() throws IOException, JSONException, URISyntaxException, ParseException, PJBankException {
        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        List<AnexoTransacao> anexosTransacao = contaDigitalManager.getTransactionFiles("1000000001259", TipoAnexo.NOTAFISCAL);

        // O teste só será executado caso haja algum anexo na transação, do contrário não há como testar
        if (anexosTransacao.size() > 0) {
            AnexoTransacao anexoTransacao = anexosTransacao.get(0);
            Assert.assertThat(anexoTransacao.getUrl(), not(is(emptyOrNullString())));
            Assert.assertThat(anexoTransacao.getTipo(), not(is(nullValue())));
            Assert.assertEquals(TipoAnexo.NOTAFISCAL, anexoTransacao.getTipo());
            Assert.assertThat(anexoTransacao.getNome(), not(is(emptyOrNullString())));
            Assert.assertThat(anexoTransacao.getFormato(), not(is(nullValue())));
            Assert.assertThat(String.valueOf(anexoTransacao.getTamanho()), not(is(emptyOrNullString())));
            Assert.assertThat(anexoTransacao.getData(), not(is(nullValue())));
        }
    }

    @Test
    @Ignore
    // TODO: Entrar em contato com a equipe responsável pela API para verificar se está sendo recebido lá e qual o real erro que está sendo retornado
    public void attachFileToTransaction() throws IOException, PJBankException {
        URL url = new URL("https://pjbank.com.br/contrato/contrato.pdf");
        File arquivo = File.createTempFile( "arquivo", ".pdf");
        arquivo.deleteOnExit();
        FileUtils.copyURLToFile(url, arquivo);

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        Assert.assertTrue(contaDigitalManager.attachFileToTransaction("1000000001259", arquivo, TipoAnexo.NOTAFISCAL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void attachFileToTransactionComExtensaoNaoPermitida() throws IOException, PJBankException {
        File arquivo = File.createTempFile( "arquivo", ".txt");
        arquivo.deleteOnExit();

        ContaDigitalManager contaDigitalManager = new ContaDigitalManager(this.credencial, this.chave);

        Assert.assertTrue(contaDigitalManager.attachFileToTransaction("1000000001259", arquivo, TipoAnexo.NOTAFISCAL));
    }
}
