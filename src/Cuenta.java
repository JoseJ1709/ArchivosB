public class Cuenta {
    private String tipo;
    private int saldoCupo;
    private String banco;
    private String redTarjeta;
    private String categoriaTarjeta;

    public Cuenta(String tipo, int saldoCupo, String banco, String redTarjeta, String categoriaTarjeta) {
        this.tipo = tipo;
        this.saldoCupo = saldoCupo;
        this.banco = banco;
        this.redTarjeta = redTarjeta;
        this.categoriaTarjeta = categoriaTarjeta;
    }

    public String getTipo() {
        return tipo;
    }

    public int getSaldoCupo() {
        return saldoCupo;
    }

    public String getBanco() {
        return banco;
    }

    public String getRedTarjeta() {
        return redTarjeta;
    }

    public String getCategoriaTarjeta() {
        return categoriaTarjeta;
    }

}
