package main;
import java.math.BigDecimal;
import java.math.MathContext;
import java.security.SecureRandom;
import smpc.Polynome;

public class Calcule {
    //The Secret DATA
    private BigDecimal secret;
    //Number of Keys to Share with
    private int numkeys;
    
    private int threshhold;
    private Polynome p;
    private boolean isSecrectCalcul = false;
    public Calcule()
    {
        isSecrectCalcul = true;
    }
    public Calcule(BigDecimal pSecret,int pNumberOfKeys,int t)
    {
        this.secret = pSecret;
        this.numkeys = pNumberOfKeys;
        this.threshhold = t;
        this.p =   new Polynome(t-1,secret);
       
    }
    
    public BigDecimal[] split(int pXValues[]) throws Exception
    {
        if(isSecrectCalcul)
            throw new Exception("This object is for Caclule Secret !");
        if(pXValues.length != numkeys)
        {
            throw new Exception("Nbr of Keys is not the same.");
        }
        BigDecimal[] res = new BigDecimal[this.threshhold];
        int i;
        for(i=0;i<this.numkeys;i++) {
                res[i] = new BigDecimal("0");
                res[i] = p.compute(new BigDecimal(""+(pXValues[i])));
        }
        return res;
    }
    
    /**
     * this fucntion will calcule secret with keys, and x values
     * @param l
     * @param in
     * @return 
     */
    public BigDecimal recon(BigDecimal[] keys,int[] values) 
    {
        int n = keys.length-1,i=0,j=0;
        
        BigDecimal res = new BigDecimal("1");
        BigDecimal res2 = new BigDecimal("0");
        
        for(i=0;i<=n;i++) {
                res = keys[i];
                for(j=0;j<=n;j++) {
                        if(i!=j) {
                                res = res.multiply(new BigDecimal(values[j]/values[j]-values[i]));
                        }
                }
                res2 = res2.add(res);
        }
        return res2;
    }
    
    public BigDecimal calculeSecret(int[] pXValues, BigDecimal[] keys) throws Exception
    {
        if(pXValues.length != keys.length)
            throw new Exception("Nbr of Keys is not the same.");
        BigDecimal secret = new BigDecimal("0");
        //Table fo LaGrangeValue in the start
        BigDecimal lagrangeValue[] = new BigDecimal[pXValues.length];
        for(int i = 0 ;i<pXValues.length;i++)
        {
            lagrangeValue[i] = this.calculeLagrangeCoeff(i, pXValues);
        }
        for(int i = 0;i < pXValues.length ;i++)
        {
            secret = secret.add(lagrangeValue[i].multiply(keys[i]));
        }
        return secret;
    }
    /**
     * Lagrange coff = beta(i) = Pi[j/(j-i)] || j = {Keys} - Keys[index]
     * @param pXValue   : the Value of F(pXValue) = pi
     * @param index     : the i index
     * @param keys      : Keys (P1,2,3,... ) liste 
     * @return 
     */
    public BigDecimal calculeLagrangeCoeff(int index,int[] pXValues)
    {
        BigDecimal coeffLaGrange = new BigDecimal("1");
        //System.out.println("VALUE : " + keys[index].toString());
        for(int i = 0;i< pXValues.length;i++)
        {
            if(i != index)
            { 
                BigDecimal p = new BigDecimal(pXValues[i]);
                BigDecimal q = new BigDecimal(pXValues[i]);
                q = q.subtract(new BigDecimal(pXValues[index]));
                p = p.divide(q,MathContext.DECIMAL128);
                coeffLaGrange = coeffLaGrange.multiply(p);
            }
        }
        //System.out.println("f("+pXValue+") = "+keys[index].toString()+" ||| " + coeffLaGrange.toPlainString());
        return coeffLaGrange;
    }
    public int[] generateXValue(int nbrOfKeys , int max)
    {
        SecureRandom r = new SecureRandom();
        int[] toReturn = new int[nbrOfKeys];
        for(int i =0;i < nbrOfKeys;i++)
            toReturn[i] = r.nextInt(max);
        return toReturn;
    }
    public BigDecimal getSecret() {
        return secret;
    }

    public int getNumkeys() {
        return numkeys;
    }

    public int getThreshhold() {
        return threshhold;
    }

    public Polynome getP() {
        return p;
    }

}
