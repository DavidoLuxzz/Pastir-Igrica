package com.lux;

public class Dialog {
    public static final String[] DIALOG_0 = {"* e pa vidi koji sam doktor"};
    public static final String[] DIALOG_1 = {"* page 1......", "* page 2 :))))"};
    public static final String[] DIALOG_2 = {"- E gde si brate ƒƒƒƒƒsta ima?", "- Nista? ƒƒƒƒ.ƒ.ƒ.ƒƒ vau brate isto!"};
    public static final String[] DIALOG_3 = {"ƒƒ gƒƒdƒƒe ƒƒƒƒsƒƒu ƒƒƒƒsƒƒvƒƒiƒ ?", " ƒ. ƒ. ƒ.ƒƒ "};
    public static final String[][] DIALOGS = {DIALOG_0, DIALOG_1, DIALOG_2, DIALOG_3};
    
    // Ghost dialogs
    public static final String[] G_DIALOG_0 = {"It's just a ghost..."};
    public static final String[] G_DIALOG_1 = {"Damn... Dude...", "It is just a fucking ghost man!"};
    public static final String[][] G_DIALOGS = {G_DIALOG_0, G_DIALOG_1};
    
    public static String[] getDialog(int index){
        return DIALOGS[index];
    }
    public static String[] getGhostDialog(int index){
        return G_DIALOGS[index];
    }
}
