package util;

/**
 * Created by Hamid on 6/5/2017.
 */
public class Tokenize {
    
    private String originalString;
    private String result;
    char    token='*';


    public Tokenize(String originalString) {
        this.originalString = originalString;
    }



    public   String  tokenizeResponse(){
        try {

            for (int i = 0; i <originalString.length() ; i++) {
                if (originalString.charAt(i)==getToken()){
                    result=originalString.substring(0,i);
                    originalString=originalString.substring(i+1);
                    return result;
                }
            }
            return null;
        }catch (Exception e){

            return null
                    ;
        }
    }


    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public char getToken() {
        return token;
    }

    public void setToken(char token) {
        this.token = token;
    }
}
