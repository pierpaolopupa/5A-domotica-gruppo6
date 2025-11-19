package domotica;

import java.io.*;
import java.net.*;
// Classe che rappresenta il contatto porta
class ContattoPorta {
    private String id;
    private String tipo = "contatto";
    private boolean valore;
    private String zona; // ingresso, cucina, garage
    public ContattoPorta(String id, boolean valore, String zona) {
        this.id = id;
        this.valore = valore;
        this.zona = zona;
    }
    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public boolean getValore() { return valore; }
    public String getZona() { return zona; }
    public void setValore(boolean valore) { this.valore = valore; }
    public void setZona(String zona) { this.zona = zona; }
    public String toString() {
        return "{ \"id\": \"" + id + "\", " +
               "\"tipo\": \"" + tipo + "\", " +
               "\"valore\": " + valore + ", " +
               "\"zona\": \"" + zona + "\" }";
    }
}
// Classe client che comunica con il server
public class MultiClient{
    String nomeServer = "localhost"; // indirizzo server locale
    int portaServer = 6789;          // porta del server
    Socket miosocket;
    BufferedReader tastiera;
    String stringaRicevutaDalServer;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;
    // Connessione al server
    public Socket connetti() {
        System.out.println("CLIENT avviato...");
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            miosocket = new Socket(nomeServer, portaServer);
            outVersoServer = new DataOutputStream(miosocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
            System.out.println("Connessione stabilita con il server su " + nomeServer + ":" + portaServer);
        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione!");
            System.exit(1);
        }
        return miosocket;
    }
    // Comunicazione con il server
    public void comunica() {
        for (;;) {
            try {
                System.out.println("\nInserisci ID del contatto (o FINE per terminare):");
                String id = tastiera.readLine();
                if (id.equalsIgnoreCase("FINE")) {
                    System.out.println("CLIENT: termina e chiude la connessione.");
                    miosocket.close();
                    break;
                }
                System.out.println("Inserisci zona (ingresso, cucina, garage):");
                String zona = tastiera.readLine();
                System.out.println("Stato porta (true = aperta, false = chiusa):");
                boolean valore = Boolean.parseBoolean(tastiera.readLine());
                // Crea l’oggetto contatto
                ContattoPorta contatto = new ContattoPorta(id, valore, zona);
                // Converte in stringa e invia al server
                String json = contatto.toString();
                System.out.println("Invio al server: " + json);
                outVersoServer.writeBytes(json + '\n');
                // Riceve la risposta
                stringaRicevutaDalServer = inDalServer.readLine();
                System.out.println("Risposta dal server:\n" + stringaRicevutaDalServer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore durante la comunicazione col server!");
                System.exit(1);
            }
        }
    }
    public static void main(String[] args) {
        Multiclient cliente = new Multiclient();
        cliente.connetti();
        cliente.comunica();
    }
}
