import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Hashtable;

public class TTTServer
{

    public static void main(String args[])
    {
        server = new TTTServer();
        server.gameWatch();
    }

    public TTTServer()
    {
        keepRunning = true;
        for(int i = 1; i < MAX_CLIENTS; i++)
            playerName[i] = "Vacant";

        try
        {
            listenSocket = new ServerSocket(1701, MAX_CLIENTS);
            return;
        }
        catch(IOException ioexception)
        {
            System.err.println("Unable to listen on port " + 1701 + ": " + ioexception);
        }
        System.exit(1);
    }

    public void gameWatch()
    {
        Object obj = null;
        try
        {
            while(keepRunning) 
            {
                java.net.Socket socket = listenSocket.accept();
                playerNum++;
                NewGameHandler newgamehandler = new NewGameHandler(socket, server, playerNum, gameInfoTime);
                playerObject[playerNum] = newgamehandler;
                playerName[playerNum] = "PLAYER" + playerNum;
                Thread thread = new Thread(newgamehandler);
                thread.start();
            }
            listenSocket.close();
            return;
        }
        catch(IOException ioexception)
        {
            System.err.println("Failed I/O: " + ioexception);
        }
    }

    protected void stop()
    {
        if(keepRunning)
            keepRunning = false;
    }

    private static final int SERVER_PORT = 1701;
    private static final int MAX_CLIENTS = 50;
    private ServerSocket listenSocket;
    private Hashtable gameInfo;
    private Date gameInfoTime;
    private boolean keepRunning;
    static TTTServer server;
    public int playerNum;
    static NewGameHandler playerObject[] = new NewGameHandler[MAX_CLIENTS];
    static String playerName[] = new String[MAX_CLIENTS];

}
