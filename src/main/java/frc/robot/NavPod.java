package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.Hashtable;



class NavPodConfig
{
    public double	cableMountAngle;
    public boolean	fieldOrientedEnabled;
    public double	initialHeadingAngle;
    public double	mountOffsetX;
    public double	mountOffsetY;
    public double	rotationScaleFactorX;		// 0 == auto calculate (NOT IMPLEMENTED YET)
    public double	rotationScaleFactorY;		// 0 == auto calculate (NOT IMPLEMENTED YET)
    public double	translationScaleFactor;		// 0 == auto calculate (NOT IMPLEMENTED YET)
}



class NavPodUpdate
{
    public double	x;      // x position (inches)
    public double	sx;     // x speed (inches/second)
    public double	y;      // y position (inches)
    public double	sy;     // y speed (inches/second)
    public double	h;      // heading (degrees)
}

public class NavPod extends Thread
{
    /*
    private double _x = 0;
    private double _sx = 0;
    private double _y = 0;
    private double _sy = 0;
    private double _h = 0;
    */

    private ReentrantLock               _lock = null;
    private Condition                   _signal = null;
    private SerialPort                  _serialPort = null;
    private Hashtable<String, Consumer<Hashtable<String, String>>>  _cmdHandlerMap = null;
    private boolean                     _running = false;
	private Thread			            _requestor = null;
	private Hashtable<String, String>	_requestReply = null;
	private byte				        _requestTag = 0;
	private byte				        _nextTag = 1;
	private int				            _version = 0;
    private Consumer<NavPodUpdate>      _callback = null;

    public NavPod()
    {
        super();

        _lock = new ReentrantLock();
        _signal = _lock.newCondition();

        _cmdHandlerMap = new Hashtable<String, Consumer<Hashtable<String, String>>>();
        _cmdHandlerMap.put("GETC", reply -> _processReplyUnsolicited(reply));
        _cmdHandlerMap.put("DIST", reply -> _processReplyUnsolicited(reply));
        _cmdHandlerMap.put("PING", reply -> _processReplyUnsolicited(reply));
        _cmdHandlerMap.put("UPDT", reply -> _processReplyUpdate(reply));
        
        _serialPort = _openSerialPort();
        if (_serialPort == null)
            System.err.printf("ERROR: NavPod not found!\n");

        if (_serialPort != null)
        {
            _lock.lock();
            {
                start();
                while(!_running)
                {
                    try { _signal.await(); }
                    catch(Exception exception) { }
                }
            }
            _lock.unlock();
            
            // Query the firmware version. This also verifies our
            // ability to communicate end-to-end with the NavPod.
            if (!_ping())
                System.err.printf("ERROR: NavPod firmware version query failed!\n");
        }
    }

    boolean isValid()
    {
        boolean valid = false;
        _lock.lock();
        {
            valid = _isValid();
        }
        _lock.unlock();
        return valid;
    }



    NavPodConfig getConfig()
    {
        Hashtable<String, String> reply = new Hashtable<String, String>();
        if (!_request(reply, "GETC", null))
            return null;
        
        NavPodConfig config = new NavPodConfig();
        config.cableMountAngle = reply.containsKey("CMA") ? Double.parseDouble(reply.get("CMA")) : 0;
        config.fieldOrientedEnabled = reply.containsKey("FOE") ? Boolean.parseBoolean(reply.get("FOE")) : true;
        config.initialHeadingAngle = reply.containsKey("IHA") ? Double.parseDouble(reply.get("IHA")) : 0;
        config.mountOffsetX = reply.containsKey("MOX") ? Double.parseDouble(reply.get("MOX")) : 0;
        config.mountOffsetY = reply.containsKey("MOY") ? Double.parseDouble(reply.get("MOY")) : 0;
        config.rotationScaleFactorX = reply.containsKey("RSFX") ? Double.parseDouble(reply.get("RSFX")) : 0;
        config.rotationScaleFactorY = reply.containsKey("RSFY") ? Double.parseDouble(reply.get("RSFY")) : 0;
        config.translationScaleFactor = reply.containsKey("TSF") ? Double.parseDouble(reply.get("TSF")) : 0;
        return config;
    }
    
    
    
    public boolean setConfig(NavPodConfig config)
    {
        if (config == null)
            return false;
        
        return _request(null, "SETC", String.format("CMA=%f FOE=%b IHA=%f MOX=%f MOY=%f RSFX=%f RSFY=%f TSF=%f",
            config.cableMountAngle, config.fieldOrientedEnabled, config.initialHeadingAngle, config.mountOffsetX,
            config.mountOffsetY, config.rotationScaleFactorX, config.rotationScaleFactorY, config.translationScaleFactor));
    }



    double getDistance()
    {
        Hashtable<String, String> reply = new Hashtable<String, String>();
        if (_request(reply, "DIST", null) && reply.containsKey("Z"))
            return Double.parseDouble(reply.get("Z"));
        
        return 0;
    }



    NavPodUpdate getUpdate()
    {
        Hashtable<String, String> reply = new Hashtable<String, String>();
        if (!_request(reply, "UPDT", null))
            return null;
        
        NavPodUpdate update = new NavPodUpdate();
        update.x = reply.containsKey("X") ? Double.parseDouble(reply.get("X")) : 0;
        update.sx = reply.containsKey("SX") ? Double.parseDouble(reply.get("SX")) : 0;
        update.y = reply.containsKey("Y") ? Double.parseDouble(reply.get("Y")) : 0;
        update.sy = reply.containsKey("SY") ? Double.parseDouble(reply.get("SY")) : 0;
        update.h = reply.containsKey("H") ? Double.parseDouble(reply.get("H")) : 0;
        return update;
    }



    boolean resetH(double h)
    {
        return _request(null, "RSET", String.format("H=%f", h));
    }



    boolean resetXY(double x, double y)
    {
        return _request(null, "RSET", String.format("X=%f Y=%f", x, y));
    }



    boolean setAutoUpdate(double interval, Consumer<NavPodUpdate> callback)
    {
        _lock.lock();
        {
            _callback = callback;
        }
        _lock.unlock();
        return _request(null, "AUTO", String.format("AUI=%f", interval));
    }



    boolean _isValid()
    {
        return (_serialPort != null) && _running;
    }



    byte _getNextTag()
    {
        byte tag = _nextTag;
        
        if ((_nextTag += 1) == 0)
            _nextTag = 1;
        
        return tag;
    }
    
    
    
    boolean _ping()
    {
        Hashtable<String, String> reply = new Hashtable<String, String>();
        boolean success = false;
        
        if (_request(reply, "PING", null) && reply.containsKey("VERS"))
        {
            _version = Integer.parseInt(reply.get("VERS"));
            success = true;
        }
        
        return success;
    }
    
    
    
    private SerialPort _openSerialPort()
    {
        SerialPort port = null;

        try { port = new SerialPort(115200, SerialPort.Port.kUSB1); }
        catch(Exception exception1)
        {
            try { port = new SerialPort(115200, SerialPort.Port.kUSB2); }
            catch(Exception exception2) { }
        }
        
        if (port != null)
        {
            port.reset();
            port.disableTermination();
            port.setReadBufferSize(1);
            port.setTimeout(.001);
            port.setWriteBufferMode(SerialPort.WriteBufferMode.kFlushOnAccess);
        }

        return port;
    }
    
    
    
    boolean _request(Hashtable<String, String> reply, String cmd, String args)
    {
        boolean success = false;
        _lock.lock();
        {
            String data = String.format("%s:", cmd);
            byte tag = 0;
            
            if (reply != null)
            {
                tag = _getNextTag();
                data += String.format("TAG=%d ", (int)tag);
            }
            
            if (args != null)
                data += args;
            
            data += "\n";
            do {
                // Claim requestor status.
                while(_isValid() && (_requestor != null))
                {
                    try { _signal.await(); }
                    catch(Exception exception) { }
                }
                
                if (!_isValid())
                    break;
                
                _requestor = Thread.currentThread();
                _requestTag = tag;
                
                // Send request.
                _serialPort.writeString(data);
                if (!_isValid())
                    break;
                
                // Process reply.
                if (reply != null)
                {
                    while(_isValid() && (_requestReply == null))
                    {
                        try { _signal.await(); }
                        catch(Exception exception) { }
                    }
                    
                    if (!_isValid() || (_requestReply == null))
                        break;
                }
                
                success = true;
            } while(false);
            
            // Relinquish requestor status.
            if (_requestor == Thread.currentThread())
            {
                if (success && (_requestReply != null) && (reply != null))
                    reply.putAll(_requestReply);
                
                _requestor = null;
                _requestTag = 0;
                _requestReply = null;
                
                _signal.signalAll();
            }
        }
        _lock.unlock();
        return success;
    }



    public void run()
    {
        _lock.lock();
        {
            _running = true;
            _signal.signalAll();
        }
        _lock.unlock();
        
        try
        {
            String data = new String();
            int length = 0, limit = 255;
            
            while(true)
            {
                int available = limit - length;
                byte[] bytes = _serialPort.read(available);
                int count = bytes.length;
                if (count == 0)
                {
                    Thread.sleep(5, 0);
                    continue;
                }
                
                for (int index = 0; index < count; index += 1)
                {
                    char ch = (char)bytes[index];
                    if ((ch == '\n') || (ch == '\r'))
                    {
                        if (length > 0)
                        {
                            // Process reply.
                            _processReply(data);
                        }
                        
                        data = new String();
                        length = 0;
                    }
                    else
                    {
                        data += ch;
                        length += 1;

                        if (length == limit)
                        {
                            // Reply is invalid (malformed command string).
                            System.err.printf("ERROR: NavPod reply invalid: %s\n", data);
                            data = new String();
                            length = 0;
                        }
                    }
                }
            }
        }
        
        catch(Exception exception)
        {
            System.err.printf("ERROR: NavPod.run() threw an uncaught exception: %s\n", exception);
        }
        
        _lock.lock();
        {
            _running = false;
            _signal.signalAll();
        }
        _lock.unlock();
    }



    Hashtable<String, String> _parseReply(String data)
    {
        String[] cmdArgStrs = data.split(":", 2);
        if ((cmdArgStrs == null) || (cmdArgStrs.length < 2))
            return null;

        String cmd = cmdArgStrs[0];
        if (cmd.equals("MSG"))
        {
            System.err.printf("%s\n", cmdArgStrs[1]);
            return null;
        }
        
        if (!_cmdHandlerMap.containsKey(cmd))
        {
            System.err.printf("ERROR: NavPod reply unknown: %s\n", cmd);
            return null;
        }
        
        Hashtable<String, String> argValueMap = new Hashtable<String, String>();
        argValueMap.put("CMD", cmd);
        
        String[] argStrs = cmdArgStrs[1].split(" ");
        for (int index = 0; index < argStrs.length; index += 1)
        {
            String[] keyValueStrs = argStrs[index].split("=", 2);
            if ((keyValueStrs == null) || (keyValueStrs.length < 1))
            {
                System.err.printf("ERROR: NavPod %s reply argument invalid: %s\n", cmd, argStrs[index]);
                continue;
            }

            argValueMap.put(keyValueStrs[0], keyValueStrs[1]);
        }
        
        return argValueMap;
    }



    private void _processReply(String data)
    {
        Hashtable<String, String> args = _parseReply(data);
        if (args == null)
            return;
        
        if (args.containsKey("TAG"))
        {
            byte tag = (byte)Integer.parseUnsignedInt(args.get("TAG"));
            _lock.lock();
            do
            {
                if (_requestor == null)
                {
                    System.err.printf("ERROR: NavPod reply without requestor: %s (%d)\n", args.get("CMD"), (int)tag);
                    break;
                }

                if (_requestTag != tag)
                {
                    System.err.printf("ERROR: NavPod reply mismatch: %s (%d vs %d)\n", args.get("CMD"), (int)tag, (int)_requestTag);
                    break;
                }
                
                if (_requestReply != null)
                {
                    System.err.printf("ERROR: NavPod reply overflow: %s (%d) replaced %s (%d)\n", args.get("CMD"),
                        (int)tag, _requestReply.get("CMD"), Integer.parseUnsignedInt(_requestReply.get("TAG")));
                }
                
                _requestReply = args;
                _signal.signalAll();
            } while(false);
            _lock.unlock();
        }
        else
        {
            try
            {
                Consumer<Hashtable<String, String>> handler = _cmdHandlerMap.get(args.get("CMD"));
                handler.accept(args);
            }

            catch(Exception exception)
            {
                System.err.printf("ERROR: NavPod CMD handler invalid: %s\n", args.get("CMD"));
            }
        }
    }



    void _processReplyUpdate(Hashtable<String, String> reply)
    {
        Consumer<NavPodUpdate> callback = null;
        _lock.lock();
        {
            callback = _callback;
        }
        _lock.unlock();
        if (callback == null)
            return;
        
        NavPodUpdate update = new NavPodUpdate();
        update.x = reply.containsKey("X") ? Double.parseDouble(reply.get("X")) : 0;
        update.sx = reply.containsKey("SX") ? Double.parseDouble(reply.get("SX")) : 0;
        update.y = reply.containsKey("Y") ? Double.parseDouble(reply.get("Y")) : 0;
        update.sy = reply.containsKey("SY") ? Double.parseDouble(reply.get("SY")) : 0;
        update.h = reply.containsKey("H") ? Double.parseDouble(reply.get("H")) : 0;
        callback.accept(update);
    }
    
    
    
    void _processReplyUnsolicited(Hashtable<String, String> reply)
    {
        if (reply.containsKey("TAG"))
        {
            System.err.printf("ERROR: NavPod reply unsolicited: %s (%d)\n", reply.get("CMD"),
                Integer.parseUnsignedInt(reply.get("TAG")));
        }
        else
        {
            System.err.printf("ERROR: NavPod reply unsolicited: %s\n", reply.get("CMD"));
        }
    }
};
