import java.io.*;
import java.net.Socket;
import java.util.*;

class NewGameHandler
    implements Runnable
{

    public NewGameHandler(Socket socket, TTTServer tttserver, int i, Date date)
    {
        BlockValue = new String[10][10];
        BlockX = new int[10][10];
        BlockY = new int[10][10];
        OrigServer = tttserver;
        playerNumber = i;
        mySocket = socket;
        gameInfoTime = date;
        if(playerNumber % 2 == 1)
            opponentnum = playerNumber + 1;
        else
            opponentnum = playerNumber - 1;
        opponentname = "PLAYER" + opponentnum;
        initBlock();
    }

    public void initBlock()
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
                BlockValue[i][j] = " ";

        }

    }

    public void run()
    {
        try
        {
            clientSend = new PrintWriter(mySocket.getOutputStream(), true);
            clientReceive = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            clientSend.println("+HELLO PLAYER" + playerNumber + " " + gameInfoTime);
            int i;
            if(playerNumber % 2 == 1)
                i = playerNumber + 1;
            else
                i = playerNumber - 1;
            String s;
            while((s = clientReceive.readLine()) != null) 
            {
                StringTokenizer stringtokenizer = new StringTokenizer(s);
                try
                {
                    String s1 = stringtokenizer.nextToken();
                    if(s1.equalsIgnoreCase("QUIT"))
                        TTTServer.playerObject[i].clientSend.println("QUIT");
                    if(s1.equalsIgnoreCase("CLEAR"))
                        TTTServer.playerObject[i].clientSend.println("CLEAR");
                    if(s1.equalsIgnoreCase("JOINED"))
                        TTTServer.playerObject[i].clientSend.println("JOINED");
                    if(s1.equalsIgnoreCase("JOINED2"))
                        TTTServer.playerObject[i].clientSend.println("JOINED2");
                    if(s1.equalsIgnoreCase("MESSAGE"))
                        TTTServer.playerObject[i].clientSend.println(s);
                    else
                    if(s1.equalsIgnoreCase("GAME:"))
                    {
                        String s2 = stringtokenizer.nextToken();
                        String s3 = stringtokenizer.nextToken();
                        String s4 = stringtokenizer.nextToken();
                        Integer.parseInt(s2);
                        Integer.parseInt(s3);
                        TTTServer.playerObject[i].clientSend.println("GAME: " + s2 + " " + s3 + " " + s4);
                    } else
                    {
                        clientSend.println("-ERR UNKNOWN COMMAND");
                    }
                }
                catch(NoSuchElementException _ex)
                {
                    clientSend.println("-ERR MALFORMED COMMAND");
                }
            }
        }
        catch(IOException _ex) { }
        finally
        {
            try
            {
                if(clientSend != null)
                    clientSend.close();
                if(clientReceive != null)
                    clientReceive.close();
                if(mySocket != null)
                    mySocket.close();
            }
            catch(IOException ioexception)
            {
                System.err.println("Failed I/O: " + ioexception);
            }
        }
    }

    protected String getBlock()
    {
        int i = 9;
        int j = 9;
        for(int k = 0; k < 3; k++)
        {
            for(int l = 0; l < 3; l++)
                if(BlockValue[k][l] == " ")
                {
                    BlockValue[k][l] = "X";
                    i = k;
                    j = l;
                    return "GAME: " + i + " " + j + " " + "X";
                }

        }

        return "GAME: " + i + " " + j + " " + "X";
    }

    private static final boolean AUTOFLUSH = true;
    private Socket mySocket;
    private PrintWriter clientSend;
    private BufferedReader clientReceive;
    private Hashtable gameInfo;
    private Date gameInfoTime;
    private int playerNumber;
    private TTTServer OrigServer;
    private String opponentname;
    private int opponentnum;
    String BlockValue[][];
    int BlockX[][];
    int BlockY[][];
}