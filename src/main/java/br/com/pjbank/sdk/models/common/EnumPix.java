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

    private final String value;

    private EnumPix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EnumPix fromValue(String myValue) {
        for (EnumPix pix : values()) {
            if (myValue.equals(pix.value)) {
                return pix;
            }
        }
        return null;
    }
}
