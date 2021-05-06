package com.glory.test;

public class TestHelloImp implements TestHello{
    @Override
    public String hello(String msg) {
        return "Hello, "+msg;
    }
}
