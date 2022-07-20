package br.com.pjbank.sdk.models.common;

/**
 * Valores que podem ser usados:
 * <ul>
 * <li>{@link #PIX}</li>
 * <li>{@link #PIX_E_BOLETO}</li>
 * </ul>
 *
 * @author lucas
 */
public enum EnumPix {

    /**
     * Substitui o boleto pelo QRCode do Pix
     */
    PIX("pix"),
    /**
     * Imprimi o QRCode no boleto
     */
    PIX_E_BOLETO("pix-e-boleto");

    private final String descricao;

    private EnumPix(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
