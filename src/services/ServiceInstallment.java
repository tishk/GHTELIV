package services;

import model.Call;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceInstallment {

    private Call call;
    public ServiceInstallment(Call call) {
        this.call=call;
    }
    public void execute(){
        call.setServiceInstallment(this);
    }
}
