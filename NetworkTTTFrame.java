import java.awt.*;

class NetworkTTTFrame extends Frame
{

    public NetworkTTTFrame(String s, NetworkTTT networkttt)
    {
        super(s);
        NTTT = networkttt;
    }

    public boolean handleEvent(Event event)
    {
        switch(event.id)
        {
        case 201: // Event.WINDOW_DESTROY
            NTTT.userExit();
            dispose();
            System.exit(0);
            return true;
        }
        return super.handleEvent(event);
    }

    NetworkTTT NTTT;
}