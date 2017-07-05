package model;

/**
 * Created by Administrator on 7/5/2017.
 */
public class Branch {

    private String branchCode="";
    private String branchTelephoneNumber="";
    private String branchFaxNumber="";
    private String branchAddress="";

    public String getBranchCode () {
        return branchCode;
    }

    public void setBranchCode (String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchTelephoneNumber () {
        return branchTelephoneNumber;
    }

    public void setBranchTelephoneNumber (String branchTelephoneNumber) {
        this.branchTelephoneNumber = branchTelephoneNumber;
    }

    public String getBranchFaxNumber () {
        return branchFaxNumber;
    }

    public void setBranchFaxNumber (String branchFaxNumber) {
        this.branchFaxNumber = branchFaxNumber;
    }

    public String getBranchAddress () {
        return branchAddress;
    }

    public void setBranchAddress (String branchAddress) {
        this.branchAddress = branchAddress;
    }
}
