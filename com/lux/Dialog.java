package com.lux;

public class Dialog {
    public static final String[] DIALOG_0 = {"* e pa vidi koji sam doktor"};
    public static final String[] DIALOG_1 = {"* page 1......", "* page 2 :))))"};
    public static final String[] DIALOG_2 = {"- E gde si brate, ƒƒƒƒƒšta ima?", "- Ništa? ƒƒƒƒ.ƒ.ƒ.ƒƒ vau brate isto!"}; // prva ovca
    public static final String[] DIALOG_3 = {"ƒƒ gƒƒdƒƒe ƒƒƒƒsƒƒu ƒƒƒƒsƒƒvƒƒiƒ ?", " ƒ. ƒ. ƒ.ƒƒ "}; // gde su svi...
    public static final String[] DIALOG_4 = {"ƒ-ƒƒ Pa gde si ti do sad??","- vidi sƒƒƒƒƒ-sƒtƒƒƒƒƒƒaƒƒƒƒ.ƒƒƒƒ.ƒƒƒƒƒ.ƒƒƒƒƒƒƒƒƒ ƒƒƒah..."};
    public static final String[][] DIALOGS = {DIALOG_0, DIALOG_1, DIALOG_2, DIALOG_3, DIALOG_4};
    
    // Ghost dialogs
    public static final String[] G_DIALOG_0 = {"It's just a ghost..."};
    public static final String[] G_DIALOG_1 = {"Damn... Dude...", "It is just a damn ghost man!"};
    public static final String[][] G_DIALOGS = {G_DIALOG_0, G_DIALOG_1};
    
    public static String[] getDialog(int index){
        return DIALOGS[index];
    }
    public static String[] getGhostDialog(int index){
        return G_DIALOGS[index];
    }
    
    /**
     * Very useful function.
     * Mnogo uma i misljenja mi je trebalo da se setim ovog.
     * @param dialog
     * @return
     */
    public static String[] makeDialog(String[] dialog) {
    	return dialog;
    }
}
