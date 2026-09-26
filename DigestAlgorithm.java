public enum DigestAlgorithm {

    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    DigestAlgorithm(String jcaName) { this.jcaName = jcaName; }
    
    public String getJcaName() { return jcaName; }

    private final String jcaName;
}