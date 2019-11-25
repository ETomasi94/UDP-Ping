/*ASSIGNMENT NUMERO 8 LABORATORIO DI RETI A.A 2019-2020
Nome Assignment : UDP_PING (Lato Server)

Autore : Enrico Tomasi
Numero Matricola : 503527

OVERVIEW : Implementazione di un server PING volto a
misurare l'RTT (Round Trip Time) di un pacchetto UDP 
in collaborazione con il corrispondente client.
*/
package UDP_PING_SERVER;

import java.io.IOException;
import java.net.*;
import java.util.Random;

/*
    @CLASS MainClass
    @OVERVIEW Classe che implementa il ciclo di vita del server.
*/
public class MainClass
{
    public static int buffersize = 1024;
    public static int PORT = 1907;
    
    
    /*
     Main NumPort
    
     @METHOD main 
     @OVERVIEW Metodo che implementa il ciclo di vita del UDP_PING_SERVER.
     @PAR NumPort Intero che rappresenta il numero di porta tramite cui avviene
     la connessione.
    */
    public static void main(String[] args) 
    {
        /*Nel caso non arrivano parametri validi, indichiamo all'utente come
        utilizzare questo programma*/
        if(args.length != 1)
        {
            System.out.println("UTILIZZO DEL PROGRAMMA: Immettere un numero di porta da console\n");
                    
            return;
        }
        
        int port;
        
        try
        {
            //Leggiamo il numero di porta dall'input
            port = Integer.parseInt(args[0]);
        }
        catch(RuntimeException ex)
        {
            //Nel caso vi sia un errore, il programma stampa errore e termina
            System.out.println("ERR -arg 0");
            return;
        }
        
        try
        {
            /*----APERTURA DELLA CONNESSIONE----*/
            DatagramSocket SrvSocket = new DatagramSocket(port);
            
            InetAddress ServerAddress = InetAddress.getLocalHost();
            String ServerHostAddress = ServerAddress.getHostAddress();

            System.out.println("IL SERVER E' IN ESECUZIONE\n");
            
            //INIZIALIZZAZIONE DEL BUFFER
            byte[] buffer = new byte[buffersize];
            
            //NUMERO RANDOM CHE SIMULA LA RICEZIONE O LA PERDITA DEL PACCHETTO
            Random DoEcho = new Random();

            while(true)
            {
                /*----RICEZIONE DEL DATAGRAMMA----*/
                DatagramPacket packet = new DatagramPacket(buffer,buffersize);
    
                SrvSocket.receive(packet);
                
                String Packetcontent = new String(packet.getData(),0,buffersize,"US-ASCII").trim();
                
                String ConsoleMessage = (ServerHostAddress+":"+port+"> "+Packetcontent+" ");
                
                int Echo = DoEcho.nextInt(4)+1;

                String action;
               
                //Probabilità del 75% di inviare il pacchetto
                if(Echo > 1)
                {
                    //Inizio calcolo tempo di invio
                    long start = System.currentTimeMillis();
                    
                    //Simulazione latenza di rete
                    long SleepTime = DoEcho.nextInt(100);
                    
                    Thread.sleep(SleepTime);
                
                    SrvSocket.send(packet);
                    //Fine calcolo tempo di invio 
                    long end = System.currentTimeMillis() - start;
                    
                    action = ("ACTION: delayed "+end+" ms");
                    
                    Echo = DoEcho.nextInt(4)+1;
                }
                else//Probabilità del 25% di smarrire il pacchetto
                {
                    action = ("ACTION: not sent");
                    
                    Echo = DoEcho.nextInt(4)+1;
                }
                
                ConsoleMessage = ConsoleMessage.concat(action);
                    
                System.out.println(ConsoleMessage);
            }
            
        } 
        catch (SocketException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException ex) 
        {
           System.out.println("ERRORE DI IO SUL PACCHETTO\n");
        } catch (InterruptedException ex) 
        {
            System.out.println("SERVER INTERROTTO DURANTE GENERAZIONE DEL TEMPO DI SLEEP");
        }

    }
    
}
