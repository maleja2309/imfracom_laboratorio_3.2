package contenedor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digest 
{
    /**
     * Obtiene el digest 
     * @param algorithm
     * @param buffer
     * @return
     */
    public static byte[] getDigestFile(String fileName) 
    {
        MessageDigest md = null;

        try 
        {
            md = MessageDigest.getInstance("SHA-256");
            FileInputStream in = new FileInputStream(fileName);
            byte [] buffer = new byte [1024*1024*300];
            
            int length;

            while ((length = in.read(buffer)) != -1)
            {
                md.update(buffer, 0, length);
            }            
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return md.digest();
    }


    public static String imprimirHexa(byte[] byteArray)
    {
        String out = "";
        for (int i = 0; i < byteArray.length; i++)
        {
            if ((byteArray[i] & 0xff) <= 0xf)
            {
                out +="0";
            }
            out += Integer.toHexString(byteArray[i] & 0xff).toLowerCase();
        }
        return out;
    }
}
