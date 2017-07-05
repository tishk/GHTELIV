import model.Call;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import util.Util;


public class Start extends BaseAgiScript {

    public Start(){

    }
    public void execute() throws Exception {

        StartTelBank();
    }


    public   void    StartTelBank() throws Exception {
        answer();
        Call call= firstInitializeCall();
        new IVRThread(call);

    }
    private  Call firstInitializeCall() throws Exception {
        return new Call(){{
                setUniQID(getUniqueId());
                setChannelName(getName());
                setCallerID(getFullVariable("${CALLERID(num)}", getUniqueId()));
                setDateOfCall(Util.persianDateTime.GetNowDate());
                setTimeOfCall(Util.persianDateTime.GetNowTime());
                setCallUniqueID();
        }};
    }
    @Override
    public   void    service(AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {

        //nothing to do yet
    }

}
