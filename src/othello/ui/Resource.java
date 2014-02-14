package othello.ui;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Resource {
	
	private Image plainImg;
	private Image blackImg;
	private Image whiteImg;
	private Image overImg;
	
public Resource() {
	//
	plainImg = (Image)(new ImageIcon(getClass().getResource("/othello/resource/plane.PNG"))).getImage();
	System.out.println(plainImg);
	blackImg = (Image)(new ImageIcon(getClass().getResource("/othello/resource/bake.PNG"))).getImage();
	System.out.println(blackImg);
	whiteImg = (Image)(new ImageIcon(getClass().getResource("/othello/resource/vite.PNG"))).getImage();
	System.out.println(whiteImg);
	overImg = (Image)(new ImageIcon(getClass().getResource("/othello/resource/Over.gif"))).getImage();
	System.out.println(overImg);
}



public Image getPlainImg() {
	return plainImg;
}

public Image getOverImg() {
	return overImg;
}



public void setOverImg(Image overImg) {
	this.overImg = overImg;
}



public void setPlainImg(Image plainImg) {
	this.plainImg = plainImg;
}



public static void main(String[] args) {
	Resource r = new Resource();
}



public Image getBlackImg() {
	return blackImg;
}



public void setBlackImg(Image blackImg) {
	this.blackImg = blackImg;
}



public Image getWhiteImg() {
	return whiteImg;
}



public void setWhiteImg(Image whiteImg) {
	this.whiteImg = whiteImg;
}
}
