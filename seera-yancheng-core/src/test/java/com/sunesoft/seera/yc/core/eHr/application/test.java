package com.sunesoft.seera.yc.core.eHr.application;

import java.util.Scanner;

/**
 * Created by temp on 2016/6/12.
 */
public class test {

    public static void main(String[] args){

        Scanner scanner=new Scanner(System.in);
        String sum= scanner.nextLine();
        int count=1;
        for(int i=2;i<32767;i++){
            if(checkH(i)){
                count++;
            }
            if(count==Integer.valueOf(sum)){
                System.out.println(i);
                break;
            }
        }
    }

    public static boolean checkH(int sum){

        if(sum==1){
            return true;
        }
        if(sum%2==0){
            if(checkH(sum/2))
                return true;
        }
        if(sum%3==0){
            if(checkH(sum/3))
                return true;
        }
        if(sum%5==0){
            if(checkH(sum/5))
                return true;
        }
        if(sum%7==0){
            if(checkH(sum/7))
                return true;
        }
         return false;
    }
}
