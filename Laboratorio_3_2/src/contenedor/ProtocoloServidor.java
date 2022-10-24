package intento;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ProtocoloServidor extends Thread
{
    static String option;
    private static DataOutputStream dataOutputStream = null;
    static DatagramSocket socket;
    static InetAddress IP_Cliente;
    static int port_Cliente;
    
    public  ProtocoloServidor(DatagramSocket socket2, String Option)
    {
        option = Option;
        socket = socket2; 
    }
    
    public void procesar() throws IOException
    {
        String inputLine;
        byte[] cifrado;
        String file;
        String name;
        float init = 0;

        DatagramPacket confConec = new DatagramPacket(new byte [256], 256);

        socket.receive(confConec);

        IP_Cliente = confConec.getAddress();
        port_Cliente = confConec.getPort();

        try 
        {                                   
            if (option.equals("1"))
            {
                init = System.nanoTime();
                file = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3.2/Laboratorio_3_2/Archivos_de_Envio/100MB.txt"; 
                name = "100MB";
            }
            else
            {
                init = System.nanoTime();
                file = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3.2/Laboratorio_3_2/Archivos_de_Envio/250MB.txt"; 
                name = "250MB";
            }
            
            // Enviar Digest 
            cifrado = Digest.getDigestFile(file);
            DatagramPacket digests = new DatagramPacket(cifrado, 
                                                        cifrado.length,
                                                        IP_Cliente,
                                                        port_Cliente);
            socket.send(digests);
            
            // Enviar nombre del archivo
            DatagramPacket nombre_file = new DatagramPacket(name.getBytes(), 
                                                            name.getBytes().length,
                                                            IP_Cliente,
                                                            port_Cliente);
            
            socket.send(nombre_file);
            
            // Env�o del archivo
            sendFile(file);
            
            System.out.println("Se transmiti� el archivo");

            // Tiempo Final
            float end = System.nanoTime();

            String time = Float.toString(end-init) + " ns";

            String file2 = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3.2/Laboratorio_3_2/Logs/";
            file2 = file2.concat(java.time.LocalDateTime.now().toString().replace(":","-"));
            file2 = file2.concat("-servidor.txt");

            log_s(file2, name, name, time);                     
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    // sendFile function define here
    private static void sendFile(String path) throws Exception
    {
        int bytes = 0;
        // Open the File where he located in your pc
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
 
        // Here we  break file into chunks
        byte buffer[] = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) 
        {
            DatagramPacket info_file = new DatagramPacket(buffer,
                                                          buffer.length, 
                                                          IP_Cliente, 
                                                          port_Cliente);
            
            // Env�o del segmento
            socket.send(info_file);
        }

        // close file
        fileInputStream.close();
    }

    public static void log_s(String path, String nombre, String tamanio, String tiempo)
    {
        try 
        {
            File file = new File(path);
            file.createNewFile();

            FileWriter writeFile = new FileWriter(path);
            writeFile.write("\n Nombre: " + nombre);
            writeFile.write("\n Tama�o: " + tamanio);
            writeFile.write("\n Tiempo: " + tiempo);
            writeFile.write("\n Log: " + "Servidor");
            writeFile.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }
}
