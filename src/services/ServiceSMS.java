package services;

import model.Call;

/**
 * Created by Hamid on 6/5/2017.
 */
public class ServiceSMS {

    private Call call;
    public ServiceSMS(Call call) {
        this.call=call;
    }
    public void execute(){
       call.setServiceSMS(this);
    }
}
