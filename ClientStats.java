package udp_ping_client;

import java.text.DecimalFormat;

/*ASSIGNMENT NUMERO 8 LABORATORIO DI RETI A.A 2019-2020
Nome Assignment : UDP_PING

Autore : Enrico Tomasi
Numero Matricola : 503527

OVERVIEW : Implementazione di un client PING volto a
misurare l'RTT (Round Trip Time) di un pacchetto UDP 
in collaborazione con il corrispondente server.
*/

/*
    @CLASS ClientStats
    @OVERVIEW Classe che implementa la creazione, l'aggiornamento e la stampa
    delle statistiche relative all'invio ed alla ricezione dei pacchetti.
*/
public class ClientStats 
{
    /*
    STATISTICHE:
    - received : Pacchetti ricevuti
    - lost : Pacchetti smarriti
    - RTTMax : Tempo intercorrente tra invio e conferma di ricezione di un pacchetto
    (Round Trip Time) massimo registrato
    - RTTMin : Round Trip Time minimo registrato
    - RTTAverage : Round Trip Time medio registrato
    */
    int received;
    int lost;
    long RTTMax;
    long RTTMin;
    float RTTAverage;
    
    //Il costruttore inizializza tutte le statistiche a zero
    public ClientStats()
    {
        received = 0;
        lost = 0;
        RTTMax = 0;
        RTTMin = 0;
        RTTAverage = 0;
    }
    
    /*
        @METHOD UpgradeReceived
    
        @OVERVIEW Incrementa di uno il contatore dei pacchetti ricevuti
    */
    public void UpgradeReceived()
    {
        received++;
    }
    
    /*
        @METHOD UpgradeLost
    
        @OVERVIEW Incrementa di uno il contatore dei pacchetti smarriti
    */
    public void UpgradeLost()
    {
        lost++;
    }
    
      /*
        @METHOD UpgradeRTT
        @OVERVIEW Modifica tutte le statistiche relative al Round Trip Time dei
        pacchetti in relazione all'ultimo RTT registrato.
        @PAR CurrentRTT Ultimo RTT Registrato.
    */
    public void UpgradeRTT(long CurrentRTT)
    {
        /*Innanzitutto controlliamo che il minimo registrato non sia zero,
        in modo da registrare il primo RTT come minimo ad inizio esecuzione del programma*/
        if(RTTMin == 0)
        {
            RTTMin = CurrentRTT;
        }
        
        //Aggiornamento di RTTMax ed RTTMin tramite confronto con CurrentRTT
        if(CurrentRTT > RTTMax)
        {
            RTTMax = CurrentRTT;
        } 
        else if(CurrentRTT < RTTMin)
        {
            RTTMin = CurrentRTT;
        }
        
        //Somma cumulativa finalizzata al calcolo del RTT Medio
        RTTAverage += CurrentRTT;
    }
    
    public void PrintStatistics()
    {
        /*Calcolo dell'RTT medio effettivo tramite divisione con il numero di 
        pacchetti ricevuti.*/
        RTTAverage = RTTAverage / (float) received;
        
        /*Formattazione dell'RTT medio in modo da essere rappresentato con precisione
        di due cifre decimali.*/
        DecimalFormat Fr = new DecimalFormat("#.##");
        String AverageRTT = Fr.format(RTTAverage);
        
        //Stampa effettiva delle statistiche.
        System.out.println("---- PING Statistics ----");
        System.out.println("10 packets transmitted, "+received+" packets received,"+(lost*10)+"% packets loss\n");
        System.out.println("round-trip (ms) min/avg/max = "+RTTMin+"/"+AverageRTT+"/"+RTTMax+"");
    }
}
