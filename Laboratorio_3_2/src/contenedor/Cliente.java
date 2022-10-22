package contenedor;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Cliente 
{
    public static final int PUERTO = 1300;
    public static final String SERVIDOR = "localhost";

    public static void main(final String[] args) throws Exception
    {
        System.out.println("Especifique el cliente que recibe el archivo: ");
        Scanner consola = new Scanner(System.in);
        String num_cliente = consola.nextLine();
        
        DatagramSocket socket = null;

        System.out.println("Cliente ... ");

        try 
        {
            // Crea el Socket en el lado cliente 
            socket = new DatagramSocket(PUERTO, InetAddress.getByName(SERVIDOR));
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            System.exit(-1);           
        }
        
        ProtocoloCliente.procesar(socket, num_cliente);
    }    
}
