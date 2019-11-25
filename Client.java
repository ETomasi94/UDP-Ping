package udp_ping_client;

/*ASSIGNMENT NUMERO 8 LABORATORIO DI RETI A.A 2019-2020
Nome Assignment : UDP_PING (Lato Client)

Autore : Enrico Tomasi
Numero Matricola : 503527

OVERVIEW : Implementazione di un client PING volto a
misurare l'RTT (Round Trip Time) di un pacchetto UDP 
in collaborazione con il corrispondente server.
*/

import java.io.IOException;
import java.net.*;
import java.util.Date;

/*
    @CLASS MainClass
    @OVERVIEW Classe che implementa il ciclo di vita del client.
*/
public class MainClass 
{
    public static int buffersize = 1024;
    public static int PORT = 1907;
     /*
     Main HostName NumPort
    
     @METHOD main 
     @OVERVIEW Metodo che implementa il ciclo di vita del UDP_PING_CLIENT.
     @PAR HostName Stringa che rappresenta il nome dell'host attraverso cui
     avviene la connessione.
     @PAR NumPort Intero che rappresenta il numero di porta tramite cui avviene
     la connessione.
    */
    public static void main(String[] args) 
    {
        /*Nel caso non arrivano parametri validi, indichiamo all'utente come
        utilizzare questo programma*/
        if(args.length != 2)
        {
            System.out.println("UTILIZZO DEL PROGRAMMA: Immettere un nome host e successivamente un numero di porta da console\n");
                    
            return;
        }
        
        int port;
        String hostname = null;
        
        try
        {
            //Leggiamo il numero di porta dall'input
            port = Integer.parseInt(args[1]);
        }
        catch(RuntimeException ex)
        {
            //Nel caso vi sia un errore, il programma stampa errore e termina
            System.out.println("ERR -arg 1");
            return;
        }

        if((hostname = args[0]) instanceof String)
        {
            hostname = args[0];
        }
        else
        {
            System.out.println("ERR -arg 0");
            return;
        }

        try
        {
            /*----INIZIALIZZAZIONE DELLA CONNESSIONE----*/
            DatagramSocket ClientSocket = new DatagramSocket();

            //Timeout della connessione
            ClientSocket.setSoTimeout(2000);

            InetAddress Address = InetAddress.getByName("localhost");
            
            SendPacket(Address,port,ClientSocket);

        } 
            catch (SocketException e) 
            {
                e.printStackTrace();
            } 
            catch (UnknownHostException ex) 
            {
               System.out.print("ERRORE: HOST SCONOSCIUTO\n");
            }
        }
    
        public static void SendPacket(InetAddress Addr,int connectionport, DatagramSocket connection)
        {      
            //Inizializzazione delle statistiche
            ClientStats St = new ClientStats();    
            
            int segno = 0;
            
            Date date = new Date();
            
            /*Ci assicuriamo di inviare esattamente 10 messaggi, per questo non esiste
            un parametro relativo ai messaggi inviati nelle statistiche.*/
            while(segno < 10)
            {
                long timestamp = System.currentTimeMillis();

                //Messaggio da inviare
                byte[] message = ("PING "+segno+" "+timestamp+" ").getBytes();
                
                DatagramPacket tosend = new DatagramPacket(message,message.length,Addr,connectionport);
                
                try 
                {
                    connection.send(tosend);
                    
                    //Inizio calcolo tempo di invio e ricezione                   
                    long start = System.currentTimeMillis();           
                 
                    connection.receive(tosend);
                    
                    //Fine calcolo tempo di invio e ricezione
                    long end = System.currentTimeMillis() - start;
                    
                    String RTT = (" RTT: "+end+" ms");
                    
                    St.UpgradeRTT(end);

                    String content;
                    
                    //Conversione contenuto del pacchetto in stringa.
                    content = new String(tosend.getData(),0,tosend.getLength(),"US-ASCII");

                    //Stampa contenuto del pacchetto.
                    System.out.println(content+RTT);
                    
                    St.UpgradeReceived();
                    
                    segno++;
                }
                /*Nel caso scada il timeout del client, il pacchetto viene considerato
                smarrito*/
                catch (SocketTimeoutException e) 
                {
                    String NotReceived = "RTT: *";
                    
                    St.UpgradeLost();

                    System.out.println(NotReceived);

                    segno++;

                } 
                catch (IOException ex) 
                {   
                    System.out.println("ERRORE DI IO");
                }   
            } 
            
            St.PrintStatistics();
        }
    }

