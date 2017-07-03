package model;

import factory.AccountFacadeImpl;
import factory.PanFacadeImpl;
import services.*;
import util.StringUtils;
import util.Util;
import util.Voices;

public  class Call  {

    public StringUtils strUtils =new StringUtils();

    private String UniQID="";
    private String CallerID="";
    private String DateOfCall="";
    private String TimeOfCall="";
    private String ChannelName="";
    private String CallUniqueID="";
    private String UserEntrance="";

    private ServiceTelBank serviceTelBank;
    private ServiceAccount serviceAccount;
    private ServiceBillPayment serviceBillPayment;
    private ServicePOSTransactionAndCheque servicePOSTransactionAndCheque;
    private ServiceFaxReport serviceFaxReport;
    private ServiceLoanPayment serviceLoanPayment;
    private ServicePan servicePan;
    private ServiceShebaAndBranches serviceShebaAndBranches;
    private ServiceSMS serviceSMS;
    private Account account=null;
    private Pan pan=null;
    public AccountFacadeImpl accountFacade=new AccountFacadeImpl();
    public PanFacadeImpl panFacade=new PanFacadeImpl();
    public Voices playVoiceTool =new Voices();


    public StringUtils getStrUtils() {
        return strUtils;
    }

    public void setStrUtils(StringUtils strUtils) {
        this.strUtils = strUtils;
    }

    public String getUniQID() {
        return UniQID;
    }

    public void setUniQID(String uniQID) {
        UniQID = uniQID;
    }

    public String getCallerID() {
        return CallerID;
    }

    public void setCallerID(String callerID) {
        CallerID = callerID;
    }

    public String getDateOfCall() {
        return DateOfCall;
    }

    public void setDateOfCall(String dateOfCall) {
        DateOfCall = dateOfCall;
    }

    public String getTimeOfCall() {
        return TimeOfCall;
    }

    public void setTimeOfCall(String timeOfCall) {
        TimeOfCall = timeOfCall;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public String getCallUniqueID() {
        return CallUniqueID;
    }

    public void setCallUniqueID() {
        CallUniqueID= Util.clientNo+getReferenceCode();
    }

    private String getReferenceCode(){

        return String.valueOf(System.nanoTime()).substring(0,12);

    }
    public String getUserEntrance() {
        return UserEntrance;
    }

    public void setUserEntrance(String userEntrance) {
        UserEntrance = userEntrance;
    }

    public ServiceTelBank getServiceTelBank() {
        return serviceTelBank;
    }

    public void setServiceTelBank(ServiceTelBank serviceTelBank) {
        this.serviceTelBank = serviceTelBank;
    }

    public ServiceAccount getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(ServiceAccount serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public ServiceBillPayment getServiceBillPayment() {
        return serviceBillPayment;
    }

    public void setServiceBillPayment(ServiceBillPayment serviceBillPayment) {
        this.serviceBillPayment = serviceBillPayment;
    }

    public ServicePOSTransactionAndCheque getServicePOSTransactionAndCheque () {
        return servicePOSTransactionAndCheque;
    }

    public void setServicePOSTransactionAndCheque (ServicePOSTransactionAndCheque servicePOSTransactionAndCheque) {
        this.servicePOSTransactionAndCheque = servicePOSTransactionAndCheque;
    }

    public ServiceFaxReport getServiceFaxReport() {
        return serviceFaxReport;
    }

    public void setServiceFaxReport(ServiceFaxReport serviceFaxReport) {
        this.serviceFaxReport = serviceFaxReport;
    }

    public ServiceLoanPayment getServiceLoanPayment () {
        return serviceLoanPayment;
    }

    public void setServiceLoanPayment (ServiceLoanPayment serviceLoanPayment) {
        this.serviceLoanPayment = serviceLoanPayment;
    }

    public ServicePan getServicePan() {
        return servicePan;
    }

    public void setServicePan(ServicePan servicePan) {
        this.servicePan = servicePan;
    }

    public ServiceShebaAndBranches getServiceShebaAndBranches() {
        return serviceShebaAndBranches;
    }

    public void setServiceShebaAndBranches(ServiceShebaAndBranches serviceShebaAndBranches) {
        this.serviceShebaAndBranches = serviceShebaAndBranches;
    }

    public ServiceSMS getServiceSMS() {
        return serviceSMS;
    }

    public void setServiceSMS(ServiceSMS serviceSMS) {
        this.serviceSMS = serviceSMS;
    }

    public Voices getPlayVoiceTool () {
        return playVoiceTool;
    }

    public void setPlayVoiceTool (Voices playVoiceTool) {
        this.playVoiceTool = playVoiceTool;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Pan getPan() {
        return pan;
    }

    public void setPan(Pan pan) {
        this.pan = pan;
    }

    public AccountFacadeImpl getAccountFacade() {
        return accountFacade;
    }

    public void setAccountFacade(AccountFacadeImpl accountFacade) {
        this.accountFacade = accountFacade;
    }

    public PanFacadeImpl getPanFacade() {
        return panFacade;
    }

    public void setPanFacade(PanFacadeImpl panFacade) {
        this.panFacade = panFacade;
    }
}
