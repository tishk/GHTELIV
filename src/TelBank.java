
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import util.Util;

import java.io.IOException;

public class TelBank extends BaseAgiScript
{
  public static final Util UtilOfTelBank=new Util();

  @Override
  public  void    service(AgiRequest request, AgiChannel channel){

      //  request.getParameter("sangomaParam");
      startCall(request);

    }

    private void startCall(AgiRequest request) {
        try {
            new Start().execute();
            Util.printMessage("call hanged up for caller id : " + request.getCallerIdNumber().toString(), false);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
    }

}