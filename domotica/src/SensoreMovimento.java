import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class SensoreMovimento {
     public static void main(String[] args) {
        try {
             Socket socket = new Socket("localhost", 6789);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Random random = new Random();
            int id = random.nextInt(10000);
            boolean movimento = random.nextBoolean();
            String[] zone = {"cucina", "bagno", "giardino"};
             String zona = zone[random.nextInt(zone.length)];
            String ora = new SimpleDateFormat("HH:mm:ss").format(new Date());

             JSONObject msg = new JSONObject();
            msg.put("id", id);
             msg.put("tipo", "movimento");
            msg.put("movimento", movimento);
             msg.put("zona", zona);
            msg.put("ora", ora);

             System.out.println("Invio messaggio: " + msg.toString());
            out.println(msg.toString());

            String risposta = in.readLine();
             System.out.println("Risposta dal server: " + risposta);

             socket.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}