package com.lux;

import java.time.LocalDate;

public class Game {
    public static final String NAME = "Sheep";
    public static final String VERSION = "Beta v1.1";
    public static final String FULL_NAME = "Sheep: Java Edition";
    public static final String TITLE = "Sheep";
    public static final String TITLE_EASTER = "ShEASTER";
    public static final String TITLE_CHRISTMAS = "Sheep: Lord Jesus' birthday";
    public static final String TITLE_BOZIC = "Sheep: Bozic";
    public static final String PLATFORM = System.getProperty("os.name");
    public static final int MONTH = LocalDate.now().getMonthValue();
    public static final int DAY = LocalDate.now().getDayOfMonth();
    
    public static boolean isEaster(){ // 16.4. je bilo 2023. | 05.5. bio 2024.
        if (MONTH==4){
            if (Math.abs(DAY-16)<8)
                return true;
        }
        return false;
    }
    public static boolean isChristmas(){ // 24.12. catholic | 07.1. orthodox
        if (MONTH == 12){
            if (Math.abs(DAY-24)<8)
                return true;
        }
        return false;
    }
    public static boolean isBozic(){
        if (MONTH == 1){
            if (Math.abs(DAY-7)<8)
                return true;
        }
        return false;
    }
    public static String getTitle(){
        if (isEaster()) return TITLE_EASTER;
        if (isChristmas()) return TITLE_CHRISTMAS;
        if (isBozic()) return TITLE_BOZIC;
        return TITLE;
    }
}
