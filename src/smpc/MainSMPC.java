package smpc;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;


/**
 *
 * @author DERRADJI Zakaria
 */
public class MainSMPC {
    /* Variables*/
    private int MAX_PRECISION = 30;
    private BigDecimal tmp = null;
    
    public Part[] generateParts(byte[] pSecret, int nbrOfPart)
    {
        return null;
    }
    public byte[] generateSecret(Part[] pParts)
    {
        return null;
    }
    
    /**
     * in work
        calculates parts of a secret from an array(integer) of values of X using MainSMPC
        this function return parties in BigDecimal array.
     * the number of parts is the length of pTableXValues
     * @param pSecret   Secret in byte array (The Secret)
     * @param pTableXValues     The X Values for each parts in array integer 
     * @param degree    the polynomial degree
     * @return      Parts  
     */
    public BigDecimal[] generateKeys(byte[] pSecret,BigInteger[] pTableXValues, int degree) throws Exception
    {
        if(pTableXValues.length -1 < degree)
        {
            throw new Exception("You need at less (Degree +1) Parts to get the secret");
        }
        BigDecimal secret = new BigDecimal(new BigInteger(pSecret));
        BigDecimal[] toReturn = new BigDecimal[pTableXValues.length];
        Polynome pol =   new Polynome(degree,secret);
        for(int i = 0; i<pTableXValues.length; i++) {
                toReturn[i] = new BigDecimal("0");
                toReturn[i] = pol.compute(new BigDecimal(pTableXValues[i]));
        }
        return toReturn;
    }
    /**
     * in work
     * calculate Secret from the parts pKeys (BigDecimal array) and the 
     * values X (Integer array) and return the secret in byte array
     * the number of parts is the length of pTableXValues and pKeys
     * @param pTableXValues     X Values for Each parts
     * @param pKeys     Parts in BigDecimal
     * @return      the Secret in byte array
     */
    public byte[] generateSecret(BigInteger[] pTableXValues,BigDecimal[] pKeys) throws Exception
    {
        if(pTableXValues.length != pKeys.length)
            throw new Exception("Nbr of Keys (Parts) is not the same with Nbr of X Values.");
        BigDecimal secret = new BigDecimal("0");
        //Table fo LaGrangeValue in the start
        BigDecimal lagrangeValue[] = new BigDecimal[pTableXValues.length];
        for(int i = 0 ;i<pTableXValues.length;i++)
        {
            lagrangeValue[i] = this.calculeLagrangeCoeff(i, pTableXValues);
        }
        for(int i = 0;i < pTableXValues.length ;i++)
        {
            secret = secret.add(lagrangeValue[i].multiply(pKeys[i]));
        }
        return secret.toBigInteger().toByteArray();
    }
    
    /**
     * calculates parts of a secret from an array(integer) of values of X using MainSMPC
 this fucntion return parties in BigDecimal array.
     * the number of parts is the length of pTableXValues
     * @param pSecret   Secret in byte array (The Secret)
     * @param pTableXValues     The X Values for each parts in array integer 
     * @param degree    the polynomial degree
     * @return      Parts  
     */
    public BigDecimal[] generateKeysA(byte[] pSecret,int[] pTableXValues, int degree) throws Exception
    {
        if(pTableXValues.length -1 < degree)
        {
            throw new Exception("You need at less (Degree +1) Parts to get the secret");
        }
        BigDecimal secret = new BigDecimal(new BigInteger(pSecret));
        BigDecimal[] toReturn = new BigDecimal[pTableXValues.length];
        Polynome pol =   new Polynome(degree,secret);
        for(int i = 0; i<pTableXValues.length; i++) {
                toReturn[i] = new BigDecimal("0");
                toReturn[i] = pol.compute(new BigDecimal(pTableXValues[i]));
        }
        return toReturn;
    }
    /**
     * Cacule Secret from the parts pKeys (BigDecimal array) and the 
     * values X (Integer array) and return the secret in byte array
     * the number of parts is the length of pTableXValues and pKeys
     * @param pTableXValues     X Values for Each parts
     * @param pKeys     Parts in BigDecimal
     * @return      the Secret in byte array
     */
    public byte[] generateSecretA(int[] pTableXValues,BigDecimal[] pKeys) throws Exception
    {
        if(pTableXValues.length != pKeys.length)
            throw new Exception("Nbr of Keys (Parts) is not the same with Nbr of X Values.");
        BigDecimal secret = new BigDecimal("0");
        //Table fo LaGrangeValue in the start
        BigDecimal lagrangeValue[] = new BigDecimal[pTableXValues.length];
        for(int i = 0 ;i<pTableXValues.length;i++)
        {
            lagrangeValue[i] = this.calculeLagrangeCoeff(i, pTableXValues);
        }
        for(int i = 0;i < pTableXValues.length ;i++)
        {
            secret = secret.add(lagrangeValue[i].multiply(pKeys[i]));
        }
        return secret.toBigInteger().toByteArray();
    }
    
    /**
     * calculates parts of a secret from an array(BigInteger) of values of X using MainSMPC
 this fucntion create parties in Files
 with createXValuesInSeperatFiles option you can write each X value into seperate File
 the number of parts is the length of pTableXValues and pPartsFilesNamesOut
     * @param pSecret The Secret
     * @param pTableXValues All X Values to Use
     * @param pPartsFilesNamesOut   Name of each parts File name
     * @param createXValuesInSeperatFiles   the option will create all X values in other file in format PartName : XValue
     */
    public void generateKeysC(byte[] pSecret,int[] pTableXValues, String[] pPartsFilesNamesOut,int degree ,boolean createXValuesInSeperatFiles) throws IOException, Exception
    {
        if(pTableXValues.length -1 < degree)
        {
            throw new Exception("You need at less (Degree +1) Parts to get the secret");
        }
        if(createXValuesInSeperatFiles)       
            createValuesIntoFile("SMPC_DATA_XVALUES_KEYS_NAMES.txt", pTableXValues, pPartsFilesNamesOut);
        
        BigDecimal secret = new BigDecimal(new BigInteger(pSecret));
        BigDecimal[] toReturn = new BigDecimal[pTableXValues.length];
        Polynome pol =   new Polynome(degree,secret);
        for(int i = 0; i<pTableXValues.length; i++) {
                toReturn[i] = new BigDecimal("0");
                toReturn[i] = pol.compute(new BigDecimal(pTableXValues[i]));
                bigDecimalToFileBigDece(pPartsFilesNamesOut[i], toReturn[i]);
        }
        
    }
    /**
     * Cacule Secret from the parts, the parts is in files 
     * The parts will be loaded from pPartsFilesName Array (name of each file)
     * and the values X (Integer array) and return the secret in byte array
     * the number of parts is the length of pTableXValues and pPartsFilesNames
     * @param pTableXValues
     * @param pPartsFilesNames
     * @return 
     */
    public byte[] generateSecretC(int[] pTableXValues, String[] pPartsFilesNames) throws Exception
    {
        if(pTableXValues.length != pPartsFilesNames.length)
            throw new Exception("Nbr of Keys (Parts) is not the same with Nbr of X Values.");
        
        for(String toCheck : pPartsFilesNames)
        {
            if(!Files.isReadable(Paths.get(toCheck)))
            {
                throw new Exception("The File "+ toCheck +" do not existe !");
            }
        }
        
        BigDecimal secret = new BigDecimal("0");
        //Table fo LaGrangeValue in the start
        BigDecimal lagrangeValue[] = new BigDecimal[pTableXValues.length];
        for(int i = 0 ;i<pTableXValues.length;i++)
        {
            lagrangeValue[i] = this.calculeLagrangeCoeff(i, pTableXValues);
        }
        for(int i = 0;i < pTableXValues.length ;i++)
        {
            secret = secret.add(lagrangeValue[i].multiply(fileBigDeceToBigDecimal(pPartsFilesNames[i])));
        }
        //return toByteArrays(secret);
        return secret.toBigInteger().toByteArray();
    }
    /**
     * Cacule Secret from the parts, the parts is in files 
     * The parts will be loaded from pPartsFilesName Array (name of each file)
     * and the values X (Integer array) and return the secret in byte array
     * the number of parts is the length of pTableXValues and pPartsFilesNames
     * this function create a file for a secret (named pFileNameSecretOut)
     * @param pTableXValues
     * @param pPartsFilesNames
     * @param pFileNameSecretOut : the  secret out file file name 
     * @return 
     */
    public void generateSecretC(int[] pTableXValues, String[] pPartsFilesNames, String pFileNameSecretOut) throws Exception
    {
        if(pTableXValues.length != pPartsFilesNames.length)
            throw new Exception("Nbr of Keys (Parts) is not the same with Nbr of X Values.");
        
        for(String toCheck : pPartsFilesNames)
        {
            if(!Files.isReadable(Paths.get(toCheck)))
            {
                throw new Exception("The File "+ toCheck +" do not existe !");
            }
        }
        
        BigDecimal secret = new BigDecimal("0");
        //Table fo LaGrangeValue in the start
        BigDecimal lagrangeValue[] = new BigDecimal[pTableXValues.length];
        for(int i = 0 ;i<pTableXValues.length;i++)
        {
            lagrangeValue[i] = this.calculeLagrangeCoeff(i, pTableXValues);
        }
        for(int i = 0;i < pTableXValues.length ;i++)
        {
            secret = secret.add(lagrangeValue[i].multiply(fileBigDeceToBigDecimal(pPartsFilesNames[i])));
        }
        FileOutputStream fos = new FileOutputStream(pFileNameSecretOut);
        fos.write(secret.toBigInteger().toByteArray());
        fos.flush();
        fos.close();
    }
    
        
    /*                     Funcitons                         */
    /**
     * BigDecimal To ByteArrays 
     * not too powerful
     * @param pIn
     * @return 
     */
    public byte[] toByteArrays(BigDecimal pIn)
    {
        return pIn.toString().getBytes(Charset.forName("UTF-8"));
    }
    
    /**
     * Generate X Values (integer)
     * @param pNbrOfXValuesToGenerate : nbr of X Values to generate
     * @param  max  the bound to use it with out max, put -1
     * @return 
     */
    public int[] generateXIntValue(int pNbrOfXValuesToGenerate, int max)
    {
        SecureRandom r = new SecureRandom();
        int[] toReturn = new int[pNbrOfXValuesToGenerate];
        if(max != -1)
            do{
                for(int i =0;i < pNbrOfXValuesToGenerate; i++)
                {
                    do{
                        toReturn[i] = r.nextInt(max);
                    }while(toReturn[i] == 0 );
                }
            }while(!checkPrecisionLimitAdveced(toReturn));
        else
            do{
                for(int i =0;i < pNbrOfXValuesToGenerate; i++)
                {
                    do
                        toReturn[i] = r.nextInt();
                    while(toReturn[i] == 0 );
                }
            }while(!checkPrecisionLimitAdveced(toReturn));
        return toReturn;
    }
    
    /**
     * Generate X Values (BigInteger)
     * @param pNbrOfXValuesToGenerate : nbr of X Values to generate
     * @param maxBytes   the size of randome Big Integer
     * @return 
     */
    public BigInteger[] generateXBigIntegerValue(int pNbrOfXValuesToGenerate, int maxBytes)
    {
        SecureRandom r = new SecureRandom();
        BigInteger[] toReturn = new BigInteger[pNbrOfXValuesToGenerate];
        byte[] tmp = new byte[maxBytes];
        do{
            for(int i =0;i < pNbrOfXValuesToGenerate; i++)
            {
                r.nextBytes(tmp);
                toReturn[i] = new BigInteger(tmp);
            }
        }while(!checkPrecisionLimitAdveced(toReturn));
        return toReturn;
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
                try{
                    p = p.divide(q,MathContext.DECIMAL128);
                }catch(ArithmeticException e) {return null;}
                coeffLaGrange = coeffLaGrange.multiply(p);
            }
        }
        //System.out.println("f("+pXValue+") = "+keys[index].toString()+" ||| " + coeffLaGrange.toPlainString());
        return coeffLaGrange;
    }
    
    /**
     * Lagrange coff = beta(i) = Pi[j/(j-i)] || j = {Keys} - Keys[index]
     * @param pXValue   : the Value of F(pXValue) = pi
     * @param index     : the i index
     * @param keys      : Keys (P1,2,3,... ) liste 
     * @return 
     */
    public BigDecimal calculeLagrangeCoeff(int index,BigInteger[] pXValues)
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
    /**
     *  This function will create text file with information about parts and  x value to each part, and his location
     * @param pFileOutName
     * @param pTableXValues
     * @param pPartsFilesNamesOut
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void createValuesIntoFile(String pFileOutName , int[] pTableXValues, String[] pPartsFilesNamesOut ) throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream(pFileOutName);
        fos.write(("--"+pFileOutName+"--\n").getBytes());
        fos.write(("X Values | Key FileName\n").getBytes());
        for(int i = 0;i< pTableXValues.length;i++)
        {
            fos.write((pTableXValues[i]+"").getBytes());
            fos.write(("\t"+pPartsFilesNamesOut[i]).getBytes());
            fos.write(("\n").getBytes());
        }
        fos.write(("ENDFILE").getBytes());
        fos.flush();
        fos.close();
    }
    /**
     *  do not used this function to save secret
     *  this function use toByteArray functions, it's save BigDecimal to file using String
     *  BigDecimal have .00000, the secret in the start is converted using BigIntger(bytes)
     * @param outFileName
     * @param pBigDecimal
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void bigDecimalToFileBigDece(String outFileName, BigDecimal pBigDecimal) throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream(outFileName);
        fos.write(toByteArrays(pBigDecimal));
        fos.flush();
        fos.close();
    }
    /**
     *  do not used this function to save secret
     *  this function use toByteArray functions, it's save BigDecimal to file using String
     *  BigDecimal have .00000, the secret in the start is converted using BigIntger(bytes)
     * @param pFileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception 
     */
    public BigDecimal fileBigDeceToBigDecimal(String pFileName) throws FileNotFoundException, IOException, Exception
    {
        return new BigDecimal(
                new String(Files.readAllBytes(Paths.get(pFileName)), Charset.forName("UTF-8"))
        );
    }
    /**
     *  do not used this function to save secret
     *  this function use toByteArray functions, it's save BigDecimal to file using String
     *  BigDecimal have .00000, the secret in the start is converted using BigIntger(bytes)
     * @param outFileName
     * @param pbigDecimal
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void BigDecimalToDataFile(String outFileName, BigDecimal pbigDecimal) throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream(outFileName);
        fos.write(toByteArrays(pbigDecimal));
        fos.flush();
        fos.close();
    }
    
    /**
     * This function check if parts are created without lost data because float, 
     * when bigDecimal use function divide, it's can around the result, and that make calculates fault 
     * @param pXValues
     * @return 
     */
    public boolean checkPrecisionLimitAdveced(int[] pXValues)
    {
        for(int i= 0; i < pXValues.length; i++)
        {
            this.tmp = this.calculeLagrangeCoeff(i, pXValues) ;
            if(tmp != null && tmp.precision() < MAX_PRECISION)
            {
                continue;
            }else
                return false;
        }
        return true;
    }
    /**
     * This function check if parts are created without lost data because float, 
     * when bigDecimal use function divide, it's can around the result, and that make calculates fault 
     * @param pXValues
     * @return 
     */
    public boolean checkPrecisionLimitAdveced(BigInteger[] pXValues)
    {
        for(int i= 0; i < pXValues.length; i++)
        {
            this.tmp = this.calculeLagrangeCoeff(i, pXValues) ;
            if(tmp != null && tmp.precision() < MAX_PRECISION)
            {
                continue;
            }else
                return false;
        }
        return true;
    }
    
    public void setPrecision(int pPrecision)
    {
        System.out.println("[+] Change MAX PRECISION to " + pPrecision);
        this.MAX_PRECISION = pPrecision;
    }
}
