
import java.net.*;
import java.io.*;
import org.json.JSONObject;

class ServerThread extends Thread {

    Socket client;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;

    public ServerThread(Socket socket) {
        this.client = socket;
    }
    
    @Override
    public void run() {
        try {
            comunica();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }



    public void comunica() throws Exception {
        inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outVersoClient = new DataOutputStream(client.getOutputStream());

        String stringaRicevuta;

        for (;;) {
            stringaRicevuta = inDalClient.readLine();

            if (stringaRicevuta == null || stringaRicevuta.equals("FINE")) {
                outVersoClient.writeBytes("Connessione terminata\n");
                break;
            }

            System.out.println("Messaggio ricevuto: " + stringaRicevuta);

            try {
                JSONObject json = new JSONObject(stringaRicevuta);
                String tipo = json.getString("tipo");
                String risposta = "OK ricevuto da sensore " + tipo;

                switch (tipo) {
                    case "temperatura" -> {
                        int temp = json.getInt("temperatura");
                        if (temp >= 40) {
                            risposta = "ALLARME: temperatura alta (" + temp + "°C)";
                        }
                    }

                    case "contatto" -> {
                        boolean contatto = json.getBoolean("contatto");
                        if (!contatto) {
                            risposta = "ALLARME: porta aperta!";
                        }
                    }

                    case "movimento" -> {
                        boolean movimento = json.getBoolean("movimento");
                        if (movimento) {
                            risposta = "ALLARME: movimento rilevato nella zona " + json.getString("zona");
                        }
                    }

                    case "fumo" -> {
                        boolean fumo = json.getBoolean("fumo");
                        if (fumo) {
                            risposta = "ALLARME: fumo rilevato!";
                        }
                    }

                    default ->
                        risposta = "Tipo sensore non riconosciuto";
                }

                outVersoClient.writeBytes(risposta + "\n");
                System.out.println("Risposta inviata: " + risposta);

            } catch (IOException e) {
                outVersoClient.writeBytes("Errore: formato JSON non valido\n");
                System.out.println("Errore parsing JSON: " + e.getMessage());
            }
        }

        outVersoClient.close();
        inDalClient.close();
        client.close();
        System.out.println("Connessione chiusa con: " + client);
    }
}

public class MultiServer {

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            System.out.println("Server avviato sulla porta 6789...");

            for (;;) {
                Socket socket = serverSocket.accept();
                System.out.println("Nuovo client connesso: " + socket);
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            }

        } catch (IOException e) {
            System.out.println("Errore durante l'istanza del server: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        MultiServer tcpServer = new MultiServer();
        tcpServer.start();
    }
}
