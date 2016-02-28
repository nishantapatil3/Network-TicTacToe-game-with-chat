import java.applet.Applet;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
//import static java.lang.System.out;

public class NetworkTTT extends Applet
    implements Runnable
{

    public static void main(String args[])
    {
        inApplet = false;
        mainserverName = "";
        if(args.length > 0)
            mainserverName = args[0];
        NetworkTTT networkttt = new NetworkTTT();
        NetworkTTTFrame networktttframe = new NetworkTTTFrame("Network Tic-Tac-Toe", networkttt);
        networktttframe.add("Center", networkttt);
        networktttframe.resize(500, 500);
        networktttframe.show(true);
        networkttt.init();
        networkttt.start();
    }

//deleted getAppletInfo

    public void init()
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                BlockValue[i][j] = " ";
                BlockX[i][j] = (i + 1) * 50 + 25;
                BlockY[i][j] = (j + 1) * 50 + 25;
                repaint();
            }

        }

        chatField = new TextField(20);
        chatArea = new TextArea(30, 30);
        chatArea.setEditable(false);
        setLayout(new BorderLayout());
        add("East", chatArea);
        add("South", chatField);
        
        validate();
        newline = System.getProperty("line.separator");
        wordFont = new Font("Helvetica", 1, 12);
        if(wordFont == null)
            wordFont = getFont();
        wordMetrics = getFontMetrics(wordFont);
        message = "Network Tic-Tac-Toe with Chat";
        if(inApplet)
            serverName = getCodeBase().getHost();
        else
        if(mainserverName.equalsIgnoreCase(""))
            serverName = "vaio";
        else
            serverName = mainserverName;
        String s = contactServer();
        if(s != null)
            currentAsOf = s.substring(s.indexOf(" ") + 1);
    }

    public void start()
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                BlockValue[i][j] = " ";
                BlockX[i][j] = (i + 1) * 50 + 25;
                BlockY[i][j] = (j + 1) * 50 + 25;
                repaint();
            }

        }

        status = clientName + ": New Game";
        repaint();
        if(Integer.parseInt(clientName.substring(6, 7)) % 2 == 0)
        {
            val = "X";
            myTurn = false;
            SendMessage("JOINED");
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        g.clearRect(0, 0, 300, 300);
        g.setColor(new Color(255, 255, 255));
        g.fill3DRect(0, 0, 300, 500, true);
        g.setColor(new Color(255, 255, 255));
        g.fill3DRect(0, 270, 300, 30, true);
        g.setColor(new Color(0, 0, 0));
        if(clientName != null)
            g.drawString(String.valueOf(clientName), 50, 40);
        message = "";//message removed
        g.drawString(message, 14, 290);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                g.drawRect((j + 1) * 50, (i + 1) * 50, 50, 50);
                g.drawString(BlockValue[j][i], BlockX[j][i], BlockY[j][i]);
                g.drawString(status, 50, 220);
            }

        }

    }

    public boolean mouseDown(Event event, int i, int j)
    {
        boolean flag = false;
        boolean flag1 = false;
        sendI = 9;
        sendJ = 9;
        if(!myTurn)
        {
            status = "Not Ur turn";
            repaint();
            return false;
        }
        if(!opponentpresent)
        {
            status = "No Opponent present.";
            repaint();
            return false;
        }
        if(InProgress)
        {
            for(int k = 0; k < 3; k++)
            {
                for(int l = 0; l < 3; l++)
                    if(i > (k + 1) * 50 && i < (k + 1) * 50 + 50 && j > (l + 1) * 50 && j < (l + 1) * 50 + 50 && BlockValue[k][l] == " ")
                    {
                        clearRecX = BlockX[k][l];
                        clearRecY = BlockY[k][l];
                        repaint();
                        BlockValue[k][l] = val;
                        sendI = k;
                        sendJ = l;
                        repaint(BlockX[k][l], BlockY[k][l], 50, 50);
                        flag = true;
                        totalClicks++;
                        myTurn = false;
                        status = "";
                        repaint();
                    }

            }

            if(flag && IsWinner(val))
            {
                status = " Congrats!!! U win";
                totalClicks = 0;
                InProgress = false;
                boolean flag2 = true;
                myTurn = true;
            } else
            if(flag && totalClicks == 9)
            {
                status = " Game drawn : ";
                totalClicks = 0;
                InProgress = false;
            } else
            if(flag && totalClicks != 9 && !IsWinner(val))
                status = "Opponent's turn";
        } else
        {
            InProgress = true;
            status = "";
            SendMessage("CLEAR");
            start();
            myTurn = true;
            status = status + " - Ur turn";
        }
        if(flag)
            SendMessage("GAME: " + sendI + " " + sendJ + " " + val);
        return true;
    }

    public boolean handleEvent(Event event)
    {
        if((event.target instanceof TextField) && event.key == 10)
        {
            String s = chatField.getText();
            if(s.equalsIgnoreCase(""))
                return true;
            chatArea.appendText(clientName + ": " + s + newline);
            chatField.setText("");
            repaint();
            if(opponentpresent)
            {
                SendMessage("MESSAGE " + clientName + ": " + s);
            } else
            {
                status = "No Opponent Present";
                repaint();
            }
        }
        switch(event.id)
        {
        case 201: // Event.WINDOW_DESTROY
            System.exit(0);
            return true;
        }
        return super.handleEvent(event);
    }

    public boolean IsWinner(String s)
    {
        boolean flag = false;
        boolean flag1 = s.equalsIgnoreCase(BlockValue[0][0]) && s.equalsIgnoreCase(BlockValue[0][1]) && s.equalsIgnoreCase(BlockValue[0][2]);
        boolean flag2 = s.equalsIgnoreCase(BlockValue[1][0]) && s.equalsIgnoreCase(BlockValue[1][1]) && s.equalsIgnoreCase(BlockValue[1][2]);
        boolean flag3 = s.equalsIgnoreCase(BlockValue[2][0]) && s.equalsIgnoreCase(BlockValue[2][1]) && s.equalsIgnoreCase(BlockValue[2][2]);
        boolean flag4 = s.equalsIgnoreCase(BlockValue[0][0]) && s.equalsIgnoreCase(BlockValue[1][0]) && s.equalsIgnoreCase(BlockValue[2][0]);
        boolean flag5 = s.equalsIgnoreCase(BlockValue[0][1]) && s.equalsIgnoreCase(BlockValue[1][1]) && s.equalsIgnoreCase(BlockValue[2][1]);
        boolean flag6 = s.equalsIgnoreCase(BlockValue[0][2]) && s.equalsIgnoreCase(BlockValue[1][2]) && s.equalsIgnoreCase(BlockValue[2][2]);
        boolean flag7 = s.equalsIgnoreCase(BlockValue[0][0]) && s.equalsIgnoreCase(BlockValue[1][1]) && s.equalsIgnoreCase(BlockValue[2][2]);
        boolean flag8 = s.equalsIgnoreCase(BlockValue[2][0]) && s.equalsIgnoreCase(BlockValue[1][1]) && s.equalsIgnoreCase(BlockValue[0][2]);
        if(flag1 || flag2 || flag3 || flag4 || flag5 || flag6 || flag7 || flag8)
            flag = true;
        return flag;
    }

    protected String contactServer()
    {
        String s = null;
        try
        {
            gameSocket = new Socket(serverName, 1701);
            gameReceive = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
            gameSend = new PrintWriter(gameSocket.getOutputStream(), true);
            s = gameReceive.readLine();
            StringTokenizer stringtokenizer = new StringTokenizer(s);
            clientName = stringtokenizer.nextToken();
            clientName = stringtokenizer.nextToken();
            if(readThread == null)
            {
                readThread = new Thread(this, "ReadThread");
                readThread.start();
            }
        }
        catch(UnknownHostException unknownhostexception)
        {
            System.err.println("Unknown host " + serverName + ": " + unknownhostexception);
        }
        catch(IOException ioexception)
        {
            System.err.println("Failed I/O to " + serverName + ": " + ioexception);
        }
        return s;
    }

    protected void SendMessage(String s)
    {
        gameSend.println(s);
    }

    protected void GetAndParseMessage()
    {
        repaint();
        if(connectOK())
            try
            {
                String s = gameReceive.readLine();
	            System.out.println(s); //player chat string stored in s
                newstatus = newstatus + " / " + s;
                repaint();
                StringTokenizer stringtokenizer = new StringTokenizer(s);
                String s1 = stringtokenizer.nextToken();
		        System.out.println(s1);
                if(s1.equalsIgnoreCase("JOINED"))
                {
                    opponentpresent = true;
                    myTurn = true;
                    SendMessage("JOINED2");
                    status = "Opponent ready. Ur turn to start";
                }
                if(s1.equalsIgnoreCase("JOINED2"))
                {
                    opponentpresent = true;
                    status = "Opponent's turn to start.";
                    return;
                }
                if(s1.equalsIgnoreCase("QUIT"))
                {
                    opponentpresent = false;
                    status = "Opponent disconnected. Restart Console";
                    repaint();
                    return;
                }
                if(s1.equalsIgnoreCase("CLEAR"))
                {
                    InProgress = true;
                    myTurn = false;
                    start();
                    status = status + " - Opponents turn.";
                    return;
                }
                if(s1.equalsIgnoreCase("MESSAGE"))
                {
                    chatArea.appendText(s.substring(8) + newline);
                    return;
                }
                if(s1.equalsIgnoreCase("GAME:"))
                {
                    status = "Ur turn";
                    receivedI = Integer.parseInt(stringtokenizer.nextToken());
                    receivedJ = Integer.parseInt(stringtokenizer.nextToken());
                    String s2 = stringtokenizer.nextToken();
                    totalClicks++;
                    myTurn = true;
                    BlockValue[receivedI][receivedJ] = s2;
                    if(IsWinner(s2))
                    {
                        status = " You lost the game Try again";
                        repaint();
                        totalClicks = 0;
                        InProgress = false;
                    } else
                    if(totalClicks == 9)
                    {
                        status = " Game draw: Click anywhere to re-start.";
                        myTurn = true;
                        totalClicks = 0;
                        InProgress = false;
                    }
                    repaint();
                    return;
                }
            }
            catch(IOException ioexception)
            {
                System.err.println("Failed I/O to " + serverName + ": " + ioexception);
                return;
            }
    }

    void userExit()
    {
        SendMessage("QUIT");
        quitServer();
    }

    protected String quitServer()
    {
        String s = null;
        try
        {
            if(connectOK())
            {
                gameSend.println("QUIT");
                s = gameReceive.readLine();
            }
            if(gameSend != null)
                gameSend.close();
            if(gameReceive != null)
                gameReceive.close();
            if(gameSocket != null)
                gameSocket.close();
        }
        catch(IOException ioexception)
        {
            System.err.println("Failed I/O to server " + serverName + ": " + ioexception);
        }
        return s;
    }

    protected boolean connectOK()
    {
        return gameSend != null && gameReceive != null && gameSocket != null;
    }

    public void run()
    {
        for(Thread thread = Thread.currentThread(); readThread == thread;)
        {
            GetAndParseMessage();
            repaint();
            try
            {
                Thread.sleep(500L);
            }
            catch(InterruptedException _ex) { }
        }

    }

    public NetworkTTT()
    {
        BlockValue = new String[10][10];
        val = "O";
        BlockX = new int[10][10];
        BlockY = new int[10][10];
        status = "Network Tic-Tac-Toe game with chat";
        newstatus = " ";
        InProgress = true;
        winnerFound = false;
        myTurn = true;
        opponentpresent = false;
    }

    String BlockValue[][];
    String val;
    int BlockX[][];
    int BlockY[][];
    String status;
    String newstatus;
    String newline;
    int statusX;
    int statusY;
    int clearRecX;
    int clearRecY;
    int sendI;
    int sendJ;
    int receivedI;
    int receivedJ;
    int totalClicks;
    boolean InProgress;
    boolean winnerFound;
    boolean myTurn;
    boolean opponentpresent;
    static boolean inApplet = true;
    private Thread readThread;
    TextField chatField;
    TextArea chatArea;
    Button sendButton;
    String message;
    Font wordFont;
    FontMetrics wordMetrics;
    private static final int SERVER_PORT = 1701;
    private static final boolean AUTOFLUSH = true;
    private static String mainserverName;
    private String serverName;
    private String clientName;
    private Socket gameSocket;
    private BufferedReader gameReceive;
    private PrintWriter gameSend;
    private String stockIDs[];
    private String stockInfo[];
    private String currentAsOf;

}
