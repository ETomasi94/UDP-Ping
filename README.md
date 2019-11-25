# UDP-Ping
An UDP transfer simulator

A program that simulates an UDP session.

OVERVIEW : This program simulates the transfer of 10 packets via UDP protocol, the packets can be effectively received whith the odds at 75%, otherwise they are considered lost.
At the end, the client program will print the statistics on the transfer.

HOW-TO-USE IT: First, open the Server program and insert a port number via command line, then open the Client program and insert in order an hostname and a port number identical to that you gave in input to the Server program via command line and watch the simulation going forward.

FILES: 
-Server.java : The class implementing Server's lifecycle. 
-Client.java : Class implementing Client's lifecycle. 
-ClientStats.java : Class implementing initialization, upgrade and print of transfer's statistics.

WRITTEN BY : Enrico Tomasi ON DATE : 25/11/2019
