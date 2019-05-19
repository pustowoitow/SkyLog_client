package com.example.skylog.data;

public class parse_helper {
    public float parse_f5toint(byte byte1, byte byte2)
    {
        float result=((float)(byte1+byte2<<8)-0.5F)/32F;
        return result;
    }

    public float parse_f2toint(byte byte1, byte byte2)
    {
        float result=((float)(byte1+byte2<<8)-0.5F)/256F;
        return result;
    }
}
