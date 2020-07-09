/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smpc;

import java.math.BigDecimal;
import java.security.SecureRandom;

/**
 *
 * @author DERRADJI Zakaria
 */
public class Polynome{	
    private int degree;             //the degree of polynomial / (Nombre of Keys to share +1)
    private BigDecimal[] coefficientsTab;   //coefficient ( s + ax + bx^2 ...
    private final int MAX_COEFFICIENT_VALUE = 100000 ;
    
    public Polynome(int degree, BigDecimal pSecret){
            this.degree = degree;
            this.coefficientsTab = new BigDecimal[degree+1]; // s + (a1)x +(a2)x^2  ||(2 + 1 = 3)
            this.coefficientsTab[0] = pSecret ; //a0
            initCoefficientsTab();
    }
    /**
     * This function will Calcule f(x)
     * f(x) = s + a0*x^1 + a1*x^2 ...
     * @param x
     * @return 
     */
    public BigDecimal compute(BigDecimal x) {
        int i;
        int j;
        BigDecimal y = new BigDecimal("1");
        BigDecimal res = this.coefficientsTab[0];
        for(i=1;i<=degree;i++) {
            /*
            y = new BigInteger("1");
            for(j=1;j<=i;j++) {
                    y = y.multiply(x);
            }
            */
            /**/
           // System.out.println("x^"+i+" * " + coefficientsTab[i].toString() + "   =   " + x.pow(i).multiply(coefficientsTab[i]).toString());
            res = res.add(x.pow(i).multiply(coefficientsTab[i]));
        }
        return res;
    }
    /**
     * this function show the function 
     * f(x) = s + ....etc
     */
    public String printFunction()
    {
        String msg = "f(x) = " + this.coefficientsTab[0].toString() ;
        for(int i = 1 ;i < this.coefficientsTab.length;i++){
            msg += " + " + this.coefficientsTab[i] + " x ^( " + i + " )";
        }
        return msg;
    }
    
    private void initCoefficientsTab()
    {
        int tmp ;
        SecureRandom r = new SecureRandom();
        for(int i=1;i < coefficientsTab.length;i++) {
            do
                tmp = r.nextInt(MAX_COEFFICIENT_VALUE);
            while(tmp == 0);
            this.coefficientsTab[i] = BigDecimal.valueOf(tmp);
        }
    }
    
    public int getDegree() {
        return degree;
    }

    public BigDecimal[] getCoefficientsTab() {
        return coefficientsTab;
    }

    public int getMAX_COEFFICIENT_VALUE() {
        return MAX_COEFFICIENT_VALUE;
    }
}