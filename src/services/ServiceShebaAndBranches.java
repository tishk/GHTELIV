package services;

import model.Call;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceShebaAndBranches {

    private Call call;
    public ServiceShebaAndBranches (Call call) {
        this.call=call;
    }
    public void execute(){
      call.setServiceShebaAndBranches(this);
    }
}
