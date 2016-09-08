# Network Tic-tac-toe with chat

##CMPE 207 Fall 2015
In this project server is setup that keeps running, two or more clients connect to the server, when two players play the game they can even communicate by chat interface within the game, the server can handle multiple client connections through client applet and play-chat simultaneously. Along with this we used multithread concept as well to bring the synchronization among the clients.

Required environment:
java runtime environment or java development kit

```
$ sudo apt-get update
$ sudo apt-get install default-jre
$ sudo apt-get install default-jdk
$ sudo apt-get install oracle-java6-installer
```


compile:
```
$ make
```

or

```
$ javac TTTServer.java
$ javac NetworkTTT.java
```


run:
```
$ java TTTServer
$ java NetworkTTT <pcname or ipaddress>

ex: java NetworkTTT 127.0.0.1
```