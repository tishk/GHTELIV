package services;

import model.Call;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceCheque {

    private Call call;
    public ServiceCheque(Call call) {
        this.call=call;
    }
    public void execute(){
        call.setServiceCheque(this);
    }
}
