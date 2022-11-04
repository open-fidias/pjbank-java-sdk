package br.com.pjbank.sdk.models.common;

/**
 *
 * @author lucas
 */
public enum EnumAtualizarVencimento {

    ATUALIZAR("0"),
    NAO_ATUALIZAR("1");

    private final String value;

    private EnumAtualizarVencimento(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
