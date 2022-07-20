package br.com.pjbank.sdk.models.recebimento;

import br.com.pjbank.sdk.models.common.Boleto;
import br.com.pjbank.sdk.models.common.Cliente;
import br.com.pjbank.sdk.models.common.EnumPix;

import java.util.Date;

/**
 * @author Vinícius Silva <vinicius.silva@superlogica.com>
 * @version 1.0
 * @since 1.0
 */
public class BoletoRecebimento extends Boleto {
    /**
     * Dados para requisição
     */

    /**
     * Cliente para o qual o boleto será gerado
     */
    private Cliente cliente;
    private double valor;
    private double juros;
    private double multa;
    private double desconto;
    private Date vencimento;
    private String logoUrl;
    private String texto;
    private String grupo;
    private String pedidoNumero;
    private int diasIndisponibilizar = 58;
    private int diasJuros = 1;
    private int diasMulta = 1;
    private int nuncaAtualizarBoleto = 0;
    private EnumPix pix = EnumPix.PIX_E_BOLETO;

    /**
     * Dados para resposta
     */
    private String id;
    private String banco;
    private String tokenFacilitador;
    private String linkGrupo;

    public BoletoRecebimento() {
    }

    public BoletoRecebimento(Cliente cliente, double valor, double juros, double multa, double desconto, Date vencimento,
                             String logoUrl, String texto, String grupo, String pedidoNumero) {
        this.cliente = cliente;
        this.valor = valor;
        this.juros = juros;
        this.multa = multa;
        this.desconto = desconto;
        this.vencimento = vencimento;
        this.logoUrl = logoUrl;
        this.texto = texto;
        this.grupo = grupo;
        this.pedidoNumero = pedidoNumero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getJuros() {
        return juros;
    }

    public void setJuros(double juros) {
        this.juros = juros;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getPedidoNumero() {
        return pedidoNumero;
    }

    public void setPedidoNumero(String pedidoNumero) {
        this.pedidoNumero = pedidoNumero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getTokenFacilitador() {
        return tokenFacilitador;
    }

    public void setTokenFacilitador(String tokenFacilitador) {
        this.tokenFacilitador = tokenFacilitador;
    }

    public String getLinkGrupo() {
        return linkGrupo;
    }

    public void setLinkGrupo(String linkGrupo) {
        this.linkGrupo = linkGrupo;
    }

    public int getDiasIndisponibilizar() {
        return diasIndisponibilizar;
    }

    /**
     * Número de dias não pode ser maior do que 58.<br><br>
     * <b>Importante: </b>Só vai funcionar em boletos emitidos nos bancos: 
     * <ul>
     * <li>BPP</li>
     * <li>Itaú</li>
     * <li>Santander</li>
     * </ul>
     * Para travar um banco basta nos solicitar pelo e-mail 
     * atendimento@pjbank.com.br e informar qual prefere usar. Isso não impacta 
     * em nada para você, seu cliente ou quem irá pagar).
     *
     * @param diasIndisponibilizar
     */
    public void setDiasIndisponibilizar(int diasIndisponibilizar) {
        this.diasIndisponibilizar = diasIndisponibilizar;
    }

    public int getDiasJuros() {
        return diasJuros;
    }

    /**
     * Indica a quantidade de dias após o vencimento para se cobrar juros.<br>
     * Bancos que aceitam o "atraso" na cobrança de juros:
     * <ul>
     * <li>001 - Banco do Brasil</li>
     * <li>033 - Santander</li>
     * <li>341 - BPP</li>
     * </ul>
     *
     * @param diasJuros
     */
    public void setDiasJuros(int diasJuros) {
        this.diasJuros = diasJuros;
    }

    public int getDiasMulta() {
        return diasMulta;
    }

    /**
     * Indica a quantidade de dias após o vencimento para se cobrar multa.<br>
     * Bancos que aceitam o "atraso" na cobrança de multa: 
     * <ul>
     * <li>001 - Banco do Brasil</li>
     * <li>033 - Santander</li>
     * <li>341 - BPP</li>
     * </ul>
     *
     * @param diasMulta
     */
    public void setDiasMulta(int diasMulta) {
        this.diasMulta = diasMulta;
    }

    public int getNuncaAtualizarBoleto() {
        return nuncaAtualizarBoleto;
    }

    /**
     * Informar valor 1 para não atualizar o vencimento boleto de forma
     * automática. Valor padrão 0.
     *
     * @param nuncaAtualizarBoleto
     */
    public void setNuncaAtualizarBoleto(int nuncaAtualizarBoleto) {
        this.nuncaAtualizarBoleto = nuncaAtualizarBoleto;
    }

    public EnumPix getPix() {
        return pix;
    }

    /**
     * Mostra o QR Code no boleto ou substitui o boleto por apenas QR
     * Code.<br><br>
     * <b>Importante:</b>
     * Não gera o QRCode se gerar um boleto vencido
     * 
     * @see br.com.pjbank.sdk.models.common.EnumPix
     * @param pix
     */
    public void setPix(EnumPix pix) {
        this.pix = pix;
    }
}
