package domotica;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Random;

public class ContattoPortaAuto {

    public static void main(String[] args) { 
        try {
            Socket socket = new Socket("localhost", 6789);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Random random = new Random();
            String[] zone = {"INGRESSO", "CUCINA", "GARAGE"};

            while (true) {

                int id = random.nextInt(1000);
                boolean stato = random.nextBoolean(); // true = aperta, false = chiusa
                String zona = zone[random.nextInt(zone.length)];
                String ora = LocalDateTime.now().toString();

                // JSON compatibile col server
                String json = "{"
                        + "\"id\":" + id + ","
                        + "\"tipo\":\"contatto\","
                        + "\"valore\":" + stato + ","
                        + "\"zona\":\"" + zona + "\","
                        + "\"ora\":\"" + ora + "\""
                        + "}";

                out.println(json);
                System.out.println("Inviato al server: " + json);

                Thread.sleep(3000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
