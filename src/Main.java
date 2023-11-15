import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Cliente> clientes = leerArchivoTexto("src/Txt/clientes.txt");

        generarReporteRiesgos(clientes);

        generarArchivoBanco(clientes, "Davivienda");

        obtenerSaldoTotalPorTipo(clientes, "123", "CA");
    }

    public static List<Cliente> leerArchivoTexto(String nombreArchivo) {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (!linea.equals("#") && !linea.equals("FIN")) {
                    String[] datosCliente = linea.split("/");
                    String nombre = datosCliente[0];
                    String identificacion = datosCliente[1];
                    String direccion = datosCliente[2];
                    String ciudad = datosCliente[3];

                    Cliente cliente = new Cliente(nombre, identificacion, direccion, ciudad);
                    while ((linea = br.readLine()) != null && !linea.equals("#")) {
                        if (linea.startsWith("PC=")) {
                            String[] datosPuntuacion = linea.split("=");
                            cliente.setPuntuacion(datosPuntuacion[1]);
                        } else {
                            String[] datosCuenta = linea.split(",");

                            if (datosCuenta.length == 3) {
                                String tipo = datosCuenta[0];
                                int saldoCupo = Integer.parseInt(datosCuenta[1]);
                                String banco = datosCuenta[2];
                                cliente.agregarCuenta(tipo, saldoCupo, banco, "", "");
                            } else if (datosCuenta.length == 4) {
                                String tipo = datosCuenta[0];
                                int saldoCupo = Integer.parseInt(datosCuenta[1]);
                                String banco = datosCuenta[2];
                                String redTarjeta = datosCuenta[3];
                                cliente.agregarCuenta(tipo, saldoCupo, banco, redTarjeta, "");
                            } else if (datosCuenta.length == 5) {
                                String tipo = datosCuenta[0];
                                int saldoCupo = Integer.parseInt(datosCuenta[1]);
                                String banco = datosCuenta[2];
                                String redTarjeta = datosCuenta[3];
                                String categoriaTarjeta = datosCuenta[4];
                                cliente.agregarCuenta(tipo, saldoCupo, banco, redTarjeta, categoriaTarjeta);
                            }
                        }
                    }

                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public static void generarReporteRiesgos(List<Cliente> clientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("riesgos.txt"))) {
            for (Cliente cliente : clientes) {
                bw.write(cliente.getIdentificacion() + " - " + cliente.getNombre() + " - " + cliente.getCiudad() + "\n");
                bw.write("Cuentas bancarias\n");
                for (Cuenta cuenta : cliente.getCuentas()) {
                    formatoCuenta(bw, cuenta);
                }

                String resultado = obtenerResultadoRiesgo(Integer.parseInt(cliente.getPuntuacion()));

                bw.write("Reporte: " + cliente.getPuntuacion() + " - " + resultado + "\n#\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void formatoCuenta(BufferedWriter bw, Cuenta cuenta) throws IOException {
        String tipo = cuenta.getTipo();
        int saldoCupo = cuenta.getSaldoCupo();
        String banco = cuenta.getBanco();
        String redTarjeta = cuenta.getRedTarjeta();
        String categoriaTarjeta = cuenta.getCategoriaTarjeta();

        bw.write(tipo + " - " + banco + " - " + redTarjeta + " - " + categoriaTarjeta);

        if (tipo.equals("TC")) {
            bw.write(", " + redTarjeta + " - " + categoriaTarjeta);
        }

        bw.write("\n");
    }

    private static String obtenerResultadoRiesgo(int puntaje) {
        if (150 <= puntaje && puntaje <= 300) {
            return "malo";
        } else if (301 <= puntaje && puntaje <= 475) {
            return "regular";
        } else if (476 <= puntaje && puntaje <= 670) {
            return "bueno";
        } else if (671 <= puntaje && puntaje <= 950) {
            return "excelente";
        } else {
            return "desconocido";
        }
    }
    public static void generarArchivoBanco(List<Cliente> clientes, String nombreBanco) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreBanco.toLowerCase() + ".txt"))) {
            bw.write("Cuentas del banco " + nombreBanco + "\n#\n");

            for (Cliente cliente : clientes) {
                boolean clienteEncontrado = false;

                for (Cuenta cuenta : cliente.getCuentas()) {
                    if (cuenta.getBanco().equalsIgnoreCase(nombreBanco)) {
                        if (!clienteEncontrado) {
                            bw.write(cliente.getIdentificacion() + " " + cliente.getNombre() + "\n");
                            clienteEncontrado = true;
                        }

                        formatoCuentaBanco(bw, cuenta);
                    }
                }

                if (clienteEncontrado) {
                    bw.write("#\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void formatoCuentaBanco(BufferedWriter bw, Cuenta cuenta) throws IOException {
        String tipo = cuenta.getTipo();
        int saldoCupo = cuenta.getSaldoCupo();
        String redTarjeta = cuenta.getRedTarjeta();
        String categoriaTarjeta = cuenta.getCategoriaTarjeta();

        bw.write(tipo + " " + saldoCupo + " " + redTarjeta + " " + categoriaTarjeta);
        bw.write("\n");
    }

    public static void obtenerSaldoTotalPorTipo(List<Cliente> clientes, String identificacion, String tipoCuenta) {
        boolean clienteEncontrado = false;

        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion().equals(identificacion)) {
                clienteEncontrado = true;
                int saldoTotal = 0;

                for (Cuenta cuenta : cliente.getCuentas()) {
                    if (cuenta.getTipo().equals(tipoCuenta)) {
                        saldoTotal += cuenta.getSaldoCupo();
                    }
                }

                if (saldoTotal > 0) {
                    System.out.println("El saldo total para el tipo de cuenta " + tipoCuenta + " del cliente "
                            + cliente.getNombre() + " es: " + saldoTotal);
                } else {
                    System.out.println("El cliente " + cliente.getNombre() + " no tiene cuentas del tipo " + tipoCuenta);
                }
            }
        }

        if (!clienteEncontrado) {
            System.out.println("Cliente con identificación " + identificacion + " no encontrado.");
        }
    }

}
