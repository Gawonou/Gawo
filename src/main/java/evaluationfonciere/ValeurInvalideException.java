package evaluationfonciere;

/*
 * 
 *  * __NAME__.java
 *  *
 *  * Created on __DATE__, __TIME__
 *  *
 *  * Copyright(c) {YEAR!!!} Kokou.  All Rights Reserved.
 */


/**
 *
 * @author ef791079
 */
public class ValeurInvalideException extends Exception{
    public ValeurInvalideException(){
        super();
    }
    
    public ValeurInvalideException(String msg){
        super(msg);
    }
}
