package com.glory.test;

public class TestMinusImp implements TestMinus{
    private int getMinus(int a,int b){
        return a-b;
    }
    @Override
    public int minus(int a, int b) {
        return getMinus(a,b);
    }
}
