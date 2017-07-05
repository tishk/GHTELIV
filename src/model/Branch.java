package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 7/5/2017.
 */
public class Branch {

    private String branchCode="";
    private List<String> branchTelephoneNumbers=new ArrayList<String> ();
    private List<String> branchFaxNumbers=new ArrayList<String> ();;
    private String branchAddress="";

    public String getBranchCode () {
        return branchCode;
    }

    public void setBranchCode (String branchCode) {
        this.branchCode = branchCode;
    }

    public List<String> getBranchTelephoneNumbers () {
        return branchTelephoneNumbers;
    }

    public void setBranchTelephoneNumbers (List<String> branchTelephoneNumbers) {
        this.branchTelephoneNumbers = branchTelephoneNumbers;
    }

    public List<String> getBranchFaxNumbers () {
        return branchFaxNumbers;
    }

    public void setBranchFaxNumbers (List<String> branchFaxNumbers) {
        this.branchFaxNumbers = branchFaxNumbers;
    }

    public String getBranchAddress () {
        return branchAddress;
    }

    public void setBranchAddress (String branchAddress) {
        this.branchAddress = branchAddress;
    }
}
