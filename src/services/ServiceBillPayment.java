package services;

import model.Call;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceBillPayment {


    private Call call;
    public ServiceBillPayment(Call call) {
        this.call=call;
    }
    public void execute(){
        call.setServiceBillPayment(this);
    }
}
