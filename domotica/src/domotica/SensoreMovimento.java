package domotica;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;

public class SensoreMovimento {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Random random = new Random();
            String[] zone = {"GIARDINO", "CUCINA", "BAGNO"};

            while (true) {

                int id = random.nextInt(1000);
                boolean valore = random.nextBoolean();
                String zona = zone[random.nextInt(zone.length)];
                String ora = LocalDateTime.now().toString();

                String json = "{"
                        + "\"id\":" + id + ","
                        + "\"tipo\":\"movimento\","
                        + "\"valore\":" + valore + ","
                        + "\"zona\":\"" + zona + "\","
                        + "\"ora\":\"" + ora + "\""
                        + "}";

                out.println(json);
                System.out.println("Inviato al server: " + json);

                Thread.sleep(4000); // ogni 4 secondi
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
