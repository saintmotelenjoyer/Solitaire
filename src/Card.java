import java.awt.*;

public class Card {

    private char suit;
    private int number;
    private boolean visible=false;
    private int stack=-1;
    private int x;
    private int y;
    private Image front;
    private Image back;


    public void setSuit(char s) {
        this.suit=s;

    }
    public void setImage (Image img) {
        this.front=img;
    }
    public void setBack(Image img) {
        this.back=img;
    }

    public Image getImage() {
        if (visible) return this.front;
        else return this.back;
    }
    public void setNum(int n) {
        this.number=n;

    }
    public int getStack() {
        return this.stack;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public void setX(int x) {
        this.x=x;
    }
    public void setY(int y) {
        this.y=y;
    }
    public void setStack(int n) {
        this.stack=n;
    }
    public void unhide() {
        this.visible=true;
    }

    public void hide() {
        this.visible=false;
    }
    public char getSuit() {
        return this.suit;
    }
    public int getNum() {
        return this.number;
    }
    public boolean visible() {
        return this.visible;
    }
    public String toString() {
        String s="";
        if (this.visible) {
            s+=this.number;
            s+=this.suit;
        }
        return s;
    }
    public boolean equals(Card other) {
        return (this.suit==other.suit && this.number==other.number);
    }

}
