package com.lux.controls;

import java.util.ArrayList;

import com.lux.Item;

// Prevod: Ranac
public class Inventory extends ArrayList<Integer> {

	private static final long serialVersionUID = 1L;

	public Inventory() {
		super();
	}
	
	private String[] createGetDialog(int item) {
		switch (item) {
		case Item.NIKE_SHOES:
			return new String[] {"Pronasao si par Nike patika..ƒƒƒƒƒƒƒƒƒƒƒƒƒƒƒƒ\nUzeo si ihƒƒƒƒƒƒƒƒƒƒƒƒƒƒƒƒƒƒƒ, logicno,ƒƒƒƒƒ posto ti\ntrenutne patike smrde na trulu\nribu!", "Sada mozes cak i da trcis.\nDok hodas DRZI [C] da bi trcao."};
		case Item.SLASH_ABILITY:
			return new String[] {"Unlocked Slash ability:\nPRESS [X] to slash."};
		default:
			return new String[] {"Dobio si: "+Item.getName(item)};
		}
	}
	
	public void addWithDialog(int item) {
		add(item);
		DialogBox.setDialog(createGetDialog(item)); // TODO: Item.getDialogForItem(item);
    	DialogBox.show();
	}
	
	private String[] createOpenDialog() {
		String[] dialog = {"Nemas nista u svom\nrancu."};
		if (size()<1) return dialog;
		dialog[0] = "Stvari u rancu: ";
		int newLine = 30;
		for (int item : this) {
			String dodati = (Item.getName(item)+", ");
			if (dialog[0].length()+dodati.length()>newLine) {
				dialog[0]+=("\n");
				newLine *= 2;
			}
			dialog[0]+=dodati;
			
		}
		dialog[0] = dialog[0].substring(0, dialog[0].length()-2);
		return dialog;
	}
	
	public void showInventory() {
		DialogBox.setDialog(createOpenDialog());
		DialogBox.show();
	}
	
	public void loadInventory(String save) {
		System.out.println("Inventory: {"+save+"}");
    	for (String numberButString : save.split(" ")) {
    		if (numberButString.length()<1) continue;
    		int tr = Integer.valueOf(numberButString);
    		add(tr);
    	}
	}

}
