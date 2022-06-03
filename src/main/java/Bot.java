import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;

import com.google.api.services.drive.model.File;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;

import javax.swing.*;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;
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
    public static String leerFichero(java.io.File file) {

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

    /**
     * @param token     para el funcionamiento del bot
     * @param lista     lista de archivos recogidos de drive
     * @param parametro palabra para buscar
     *                  Este método crea un embed que saca por discord una lista de imagenes
     *                  o muestra una imagen dependiendo del comando utilizado
     */
    public static void bot(String token, List<File> lista, String parametro) {

        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();
        try {
            DriveQuickstart.driveDescargar(parametro);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            EmbedCreateSpec embed = EmbedCreateSpec.builder()
                    .color(Color.GREEN)
                    .image("attachment://amongos.jpg")
                    .build();
            if ((parametro).equals(message.getContent())) {

                final MessageChannel channel = message.getChannel().block();
                EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();
                //Array de archivos recibido de drive
                for (File str : lista) {
                    builder.description(str.getName());
                    channel.createMessage(builder.build()).block();
                }
            } else if (("!" + parametro).equals(message.getContent())) {
                final MessageChannel channel = message.getChannel().block();

                InputStream fileAsInputStream = null;
                try {
                    fileAsInputStream = new FileInputStream("/home/dam1/IdeaProjects/BotDrive/src/main/resources/" + parametro + ".jpg");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                channel.createMessage(MessageCreateSpec.builder()
                        .content("content? content")
                        .addFile(parametro + ".jpg", fileAsInputStream)
                        .addEmbed(embed)
                        .build()).subscribe();
            }
        });

        gateway.onDisconnect().block();
    }

    public static void botExamen(String token, String parametro) {
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();
        try {
            DriveQuickstart.downloadFile(parametro);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            EmbedCreateSpec embed = EmbedCreateSpec.builder()
                    .color(Color.GREEN)
                    .image("attachment://amongos.jpg")
                    .build();
            if (("/pdf").equals(message.getContent())) {

                final MessageChannel channel = message.getChannel().block();
                EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();
                builder.description("Archivo descargado");
            }
        });
        gateway.onDisconnect().block();

    }
}

