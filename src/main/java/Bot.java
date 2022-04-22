import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;

import javax.swing.*;
import java.io.*;
import java.time.Instant;
import java.util.Scanner;

public class Bot {
    private static String IMAGE_URL;
    private static String ANY_URL;
    static PrintWriter escribir;
    static FileWriter fich;

    /**
     * Metodo que sirve para escribir un texto corto en un fichero
     *
     * @param fichero Nombre del fichero donde escribir el texto
     */
    public static void escribirToken(String fichero) {
        try {
            fich = new FileWriter(fichero, false);
            escribir = new PrintWriter(fich);
            escribir.println(JOptionPane.showInputDialog("Introducir token"));
            System.out.println("Fichero creado con exito");

        } catch (IOException e) {
            System.out.println("Error escritura" + e.getMessage());
        } finally {
            escribir.close();
        }
    }

    /**
     * Lee un fichero y devuelve su contenido en forma de string
     *
     * @param file El fichero del qu se desean extrae los datos
     * @return Un string con el contenido del fichero
     */
    public static String leerFichero(File file) {

        Scanner sc = null;

        String message = "";

        try {
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                message += sc.nextLine();
            }

        } catch (FileNotFoundException ex) {

            System.out.println("Error:" + ex.getMessage());

        } finally {
            sc.close();
        }

        return message;
    }

    public static void bot(String token) {
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            EmbedCreateSpec embed = EmbedCreateSpec.builder()
                    .build();
            if ("garfield".equals(message.getContent())) {

                final MessageChannel channel = message.getChannel().block();
                EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();
                    /*for (String str : lista) {
                        builder.description(str);
                        channel.createMessage(builder.build()).block();
                    }*/
            }

        });

        gateway.onDisconnect().block();
    }

}

