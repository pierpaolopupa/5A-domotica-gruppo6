package domotica;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Random;

public class SensoreTemperatura {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 6789);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Random random = new Random();
            String[] zone = {"SALONE", "CAMERA", "GARAGE"};

            while (true) {

                int id = random.nextInt(1000);
                double valore = 20 + random.nextDouble() * 30;  // 20–50 °C
                String zona = zone[random.nextInt(zone.length)];
                String ora = LocalDateTime.now().toString();

                String json = "{"
                        + "\"id\":" + id + ","
                        + "\"tipo\":\"temperatura\","
                        + "\"valore\":" + valore + ","
                        + "\"zona\":\"" + zona + "\","
                        + "\"ora\":\"" + ora + "\""
                        + "}";

                out.println(json);
                System.out.println("Inviato al server: " + json);

                Thread.sleep(3000); // ogni 3 secondi
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
