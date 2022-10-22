package contenedor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProtocoloCliente 
{
    private static DatagramSocket socket = null;
    private static DataInputStream dataInputStream = null;
    static String id;

	public static void procesar(DatagramSocket socket2, String num_cliente) throws Exception
    {
        socket = socket2;
        id = num_cliente;
        
        // Enviar mensaje al servidor para la conexión 
        byte[] conec = "Confirmacion".getBytes();
        DatagramPacket confirmacion = new DatagramPacket(conec, 
                                                         conec.length,
                                                         InetAddress.getByName("localhost"),
                                                         3400);
        socket.send(confirmacion);
        //
        DatagramPacket archivo = new DatagramPacket(new byte [256], 256);
        String archivo2 = new String(archivo.getData(), StandardCharsets.UTF_8);
        socket.receive(archivo);

        String destino = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3.2/Laboratorio_3_2/ArchivosRecibidos/";
        destino = destino.concat("Cliente");
        destino = destino.concat(id);
        destino = destino.concat("-Prueba");
        destino = destino.concat(".txt"); 
        
        // Recibir archivo
        receiveFile(socket, destino, archivo2);    
    }
    
	// Receive file function is start here    
    private static void receiveFile(DatagramSocket socket, String fileName, String archivo) throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
     // Tiempo inicial
        float init = System.nanoTime();
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        byte[] buffer = new byte[4 * 1024];
        DatagramPacket info = new DatagramPacket(buffer, buffer.length);
        
        // Especificaci�n time Out
        socket.setSoTimeout(10000);
        
        while(true)
        {
            try
            { 
                socket.receive(info);                    
                fileOutputStream.write(info.getData(), 0, 1024);
            }
            catch (SocketTimeoutException e) 
            {
                // timeout exception.
                System.out.println("Timeout reached!!! " + e);

                // Ruta para los log
                String file2 = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3.2/Laboratorio_3_2/Logs/";
                file2 = file2.concat(java.time.LocalDateTime.now().toString().replace(":","-"));
                file2 = file2.concat("-cliente.txt");

                // Tiempo Final
                float end = System.nanoTime();

                String time = Float.toString(end-init) + " ns";
                log_c(file2, archivo, time);  
                System.out.println("Fin de la transmisi�n del archivo");
                socket.close();
                break;
            }           
        }
    }    
    
    public static void log_c(String path, String nombre, String tiempo)
    {
        try 
        {
            File file = new File(path);
            file.createNewFile();

            FileWriter writeFile = new FileWriter(path);
            writeFile.write("\n Nombre: " + nombre);
            writeFile.write("\n Tamaño: " + nombre);
            writeFile.write("\n Tiempo: " + tiempo);
            writeFile.write("\n Log: " + "Cliente");
            writeFile.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}