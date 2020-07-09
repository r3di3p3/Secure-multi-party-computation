package smpc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author r3di3p3
 */
public class Part implements Serializable{
    private final String version = "1.0.0";
    private boolean useBigInteger;
    private String ID_USER = "";
    private String HASH ="";
    private int key;
    private BigDecimal keyValue;
    private BigInteger keyAdveced;
    
    public Part(int pKey,BigDecimal pKeyValue)
    {
        this.useBigInteger = false;
        this.key = pKey;
        this.keyValue = pKeyValue ;
    }
    
    public Part(BigInteger pKey,BigDecimal pKeyValue)
    {
        this.useBigInteger = false;
        this.keyAdveced = pKey;
        this.keyValue = pKeyValue ;
    }
    public Part(int pKey,BigDecimal pKeyValue, String pId, String pHash)
    {
        this.useBigInteger = false;
        this.key = pKey;
        this.keyValue = pKeyValue ;
    }
    
    public Part(BigInteger pKey,BigDecimal pKeyValue, String pId, String pHash)
    {
        this.useBigInteger = false;
        this.keyAdveced = pKey;
        this.keyValue = pKeyValue ;
    }
    
    public String getVersion() {
        return version;
    }

    public boolean isUseBigInteger() {
        return useBigInteger;
    }

    public int getKey() {
        if(!useBigInteger)
            return key;
        else
            return -1;
    }

    public BigDecimal getKeyValue() {
        return keyValue;
    }

    public BigInteger getKeyAdveced() {
        if(useBigInteger)
            return keyAdveced;
        else
            return null;
    }
    
    
    
}
