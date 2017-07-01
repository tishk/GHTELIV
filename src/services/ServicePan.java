package services;

import model.Call;

/**
 * Created by Hamid on 6/5/2017.
 */
public class ServicePan {

    private Call call;
    public ServicePan(Call call) {
        this.call=call;
    }
    public void execute(){
      call.setServicePan(this);
    }
}
