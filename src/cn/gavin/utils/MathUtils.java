package cn.gavin.utils;

/**
 * Copyright 2016 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 1/1/16.
 */
public class MathUtils {
    public static long getMaxValueByRiseAndLev(long rise, long lev) {
        long levR = lev / 300 + 1;
        return 350 * rise * levR * (1026 + 270 * levR);
    }

    public static long getLevReduce(long lev) {
        return 300 * ((lev - 1) / 300 + 1) - lev + 1;
    }

    public static long getAvgValueByRiseAndLev(long rse, long lev) {
        return getMaxValueByRiseAndLev(rse, lev) / 3;
    }

    public static long getMonsterHP(long base, long lev, long heroATKRise, Random random) {
        long randomValue = base * lev + random.nextLong(getMaxValueByRiseAndLev(heroATKRise, lev));
        long levR = getLevReduce(lev);
        if (levR > 0) {
            randomValue /= levR;
        }
        if (lev < 1000) {
            switch ((int)lev / 50) {
                case 0:
                    randomValue/=30;
                    break;
                case 1:
                    randomValue/=25;
                    break;
                case 2:
                    randomValue/=20;
                    break;
                case 3:
                    randomValue/=20;
                    break;
                case 4:
                    randomValue/=20;
                    break;
                case 5:
                    randomValue/=15;
                    break;
                case 6:
                    randomValue/=15;
                    break;
                case 7:
                    randomValue/=10;
                    break;
                case 8:
                    randomValue/=5;
                    break;
                case 9:
                    randomValue/=5;
                    break;
                case 10:
                    randomValue/=2;
                    break;
            }
        }else{
            if(lev > 10000){
                randomValue *= (lev/10000);
            }
        }
        if (randomValue > 0) {
            return randomValue;
        } else {
            return base;
        }
    }

    public static long getMonsterAtk(long base, long lev, long heroHPRise, long heroDEFRise, Random random) {
        long hpAndDef = getMaxValueByRiseAndLev(heroDEFRise, lev) + getMaxValueByRiseAndLev(heroHPRise, lev) / 5;
        long randomValue = base * lev + random.nextLong(hpAndDef);
        long levR = getLevReduce(lev);
        if (levR > 0) {
            randomValue /= levR;
        }
        if (lev < 1000) {
            switch ((int)lev / 50) {
                case 0:
                    randomValue/=60;
                    break;
                case 1:
                    randomValue/=50;
                    break;
                case 2:
                    randomValue/=50;
                    break;
                case 3:
                    randomValue/=45;
                    break;
                case 4:
                    randomValue/=30;
                    break;
                case 5:
                    randomValue/=30;
                    break;
                case 6:
                    randomValue/=20;
                    break;
                case 7:
                    randomValue/=10;
                    break;
                case 8:
                    randomValue/=10;
                    break;
                case 9:
                    randomValue/=5;
                    break;
                case 10:
                    randomValue/=5;
                    break;
            }
        }else{
            if(lev > 50000){
                randomValue *= (lev/10000);
            }
        }
        if (randomValue > 0) {
            return randomValue;
        } else {
            return base;
        }
    }

    public static long getAccessoryValue(long lev, long rise, Random random){
        long randomValue = random.nextLong(getMaxValueByRiseAndLev(rise, lev) - getAvgValueByRiseAndLev(rise, lev));
        if (lev < 1000) {
            switch ((int)lev / 50) {
                case 0:
                    randomValue/=120;
                    break;
                case 1:
                    randomValue/=100;
                    break;
                case 2:
                    randomValue/=60;
                    break;
                case 3:
                    randomValue/=50;
                    break;
                case 4:
                    randomValue/=30;
                    break;
                case 5:
                    randomValue/=30;
                    break;
                case 6:
                    randomValue/=20;
                    break;
                case 7:
                    randomValue/=10;
                    break;
                case 8:
                    randomValue/=10;
                    break;
                case 9:
                    randomValue/=5;
                    break;
                case 10:
                    randomValue/=5;
                    break;
            }
        }
        return randomValue;
    }

    public static void main(String... args) {
        Random random = new Random();
        System.out.println(getAccessoryValue(10, 1, random));
        System.out.println(getAccessoryValue(10, 2, random));
        System.out.println(getAccessoryValue(10, 5, random));

    }
}
