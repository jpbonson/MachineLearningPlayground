package com.anji.backgammon;

import java.security.SecureRandom;

public class RobustRandomNumber
{
    public static int random(int a, int b)
    {
        SecureRandom random = new SecureRandom();
        return a+random.nextInt(b);
    }
}
