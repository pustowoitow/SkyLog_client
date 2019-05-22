package com.example.skylog.data;

public class parse_helper {
    public float parse_f5toint(byte byte1, byte byte2)
    {
        int intBits=(int)( // NOTE: type cast not necessary for int
                0x00 << 24  |
                        0x00 << 16  |
                        (0xff & byte2) << 8   |
                        (0xff & byte1) << 0
        );
       // intBits=(0<<24)|(0<<16)|((byte2&0x3F)<<8)|byte1 ;
        float result=(float)intBits;//Float.intBitsToFloat(intBits);
        result=result/32F;
        return result;
    }

    public float parse_f2toint(byte byte1, byte byte2)
    {
        float result=((float)(byte1+byte2<<8)-0.5F)/256F;
        return result;
    }
}
