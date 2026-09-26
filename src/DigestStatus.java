/*
 * INF1416 - Segurança da Informação
 * Trabalho 2 - DigestCalculator
 *
 * Alunos:
 * João Pedro Zaidman dos Santos Gonçalves - Matrícula: 2320464
 * Breno de Andrade Soares - Matrícula: 2320363
 */

public enum DigestStatus
{
    OK("OK"),
    NOT_OK("NOT OK"),
    NOT_FOUND("NOT FOUND"),
    COLISION("COLISION");

    DigestStatus(String displayName) { this.displayName = displayName; }

    @Override
    public String toString() { return displayName; }

    private final String displayName;
}