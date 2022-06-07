import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class Solitaire {
    final static int TABLE_Y=159;
    final static int TABLE1_X=18;
    final static int CARD_WIDTH=65;
    final static int CARD_HEIGHT=89;
    final static int OFFSET_X=74;
    final static int OFFSET_Y=15;
    final static int F_Y=36;
    final static int F_X=250;
    final static int DECK_Y=38;
    final static int DEALT_X=90;
    static ArrayList<Card>deck=createDeck();
    static ArrayList<ArrayList<Card>> table;
    static Image back;
    static int cardmode=3;
    static boolean animationsOn=true;
    static boolean opt=true;
    static  boolean gameWon=false;

    public static void main (String[]args) throws IOException{
        Frame f = new Frame();
        f.setBounds(0, 0, 550, 450);
        f.setBackground(Color.white);
        Canvas test = new Canvas();
        test.setBounds(0, 0, 550, 450);
        test.setBackground(Color.black);
        f.setVisible(true);
        f.add(test);
        test.setVisible(true);
        Graphics g = test.getGraphics();
        Image options =ImageIO.read(new File("src/options.png"));
        Image check=ImageIO.read(new File("src/checkmark.png"));
        Image tabl = ImageIO.read(new File("src/table.png"));
        back=ImageIO.read(new File("src/back.png"));
        for (Card c : deck) c.setBack(back);
        for (int i=0; i<52; i++) {
            String s="src/";
            s+=i%13+1;
            if (i<13) s+="s";
            else if (i<26) s+="d";
            else if (i<39) s+="h";
            else if (i<52) s+="c";
            s+=".png";
            deck.get(i).setImage(ImageIO.read(new File(s)));
        }
        deck=shuffleDeck(deck);
        table = setTable(deck);
        Card[] aces = new Card[4]; Card foo = new Card();
        foo.setNum(0); foo.setSuit('y');
        for (int i=0; i<4; i++) aces[i]=foo;

        test.addMouseListener(new MouseAdapter() {
            public void mouseClicked (MouseEvent e) {
                if (opt) {
                    if (e.getX()>160 && e.getX()<390 && e.getY()>290 && e.getY()<360) opt=false;
                    if (e.getX()>160 && e.getX()<190) {
                        if (e.getY()>155 && e.getY()<180) cardmode=1;
                        else if (e.getY()>200 && e.getY()<225) cardmode=3;
                        else if (e.getY()>245 && e.getY()<270) animationsOn=!animationsOn;
                    }
                }
                else if (!gameWon)if (e.getButton()==1) {
                    if (e.getX()>10 && e.getX()<10+CARD_WIDTH && e.getY()>DECK_Y && e.getY()<DECK_Y+CARD_HEIGHT) {
                        dealCards(deck);
                        if (animationsOn) animate(deck, g);
                        showTable(table, deck, aces, g, tabl);
                    }
                    else if (e.getX()>DEALT_X && e.getX()<DEALT_X+CARD_WIDTH+30 && e.getY()>DECK_Y && e.getY()<DECK_Y+CARD_HEIGHT) {
                        int inde =-1;
                        int xoff=0;
                        for (Card c : deck) {
                            if (cardmode==3 && c.visible() && c.getX()>xoff) {
                                inde=deck.indexOf(c);
                                xoff=c.getX();
                            }
                            else if (c.visible()) inde=deck.indexOf(c);
                        }
                        String s=moveCard(table, deck, aces, deck.get(inde));
                        if (animationsOn) animate(s, table, deck, aces, g, tabl);
                        showTable(table, deck, aces, g, tabl);
                    }
                    else{
                        int al=-1;
                        int index=-1;
                        for (ArrayList<Card> aa : table) for (Card cc : aa) if (cc.visible())
                            if (e.getX()>cc.getX() && e.getX()<cc.getX()+CARD_WIDTH && e.getY()>cc.getY() &&e.getY()<cc.getY()+CARD_HEIGHT) {
                                al=table.indexOf(aa);
                                index=table.get(al).indexOf(cc);
                            }
                        if (al>-1 && index>-1) {
                            String s=moveCard(table, deck, aces, table.get(al).get(index));
                            if (animationsOn) animate(s, table, deck, aces, g, tabl);
                            showTable(table, deck, aces, g, tabl);
                        }
                    }
                }
                else fireworks(g, aces, tabl);
            }
        });
        while (opt) {
            g.drawImage(options, 0, 0, null);
            if (cardmode==1) g.drawImage(check, 161, 150, null);
            else g.drawImage(check, 161, 194, null);
            if (animationsOn) g.drawImage(check, 161, 239, null);
        }
        g.drawImage(tabl, 0, 0, null);
        showTable(table, deck, aces, g, tabl);
        while (!gameWon) {gameWon=checkwin(aces);}
        while (true) fireworks(g, aces, tabl);
    }

    public static ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        for(int i =0; i<52; i++) {
            Card a = new Card();
            if (i/13<1) a.setSuit('s');
            else if (i/13<2) a.setSuit('d');
            else if (i/13<3) a.setSuit('h');
            else a.setSuit('c');
            a.setNum(i%13+1);
            deck.add(a);
        }
        return deck;
    }
    public static ArrayList<Card> shuffleDeck(ArrayList<Card> deck) {
        ArrayList<Card> shuffled = new ArrayList<>();
        while (deck.size()>0) {
            int x=(int)Math.round(Math.random()*(deck.size()-1));
            shuffled.add(deck.get(x));
            deck.remove(x);
        }
        return shuffled;
    }
    public static ArrayList<ArrayList<Card>> setTable(ArrayList<Card> deck) {
        ArrayList<ArrayList<Card>> table = new ArrayList<>();
        ArrayList<Card> c1= new ArrayList<>(); table.add(c1);
        ArrayList<Card> c2= new ArrayList<>(); table.add(c2);
        ArrayList<Card> c3= new ArrayList<>(); table.add(c3);
        ArrayList<Card> c4= new ArrayList<>(); table.add(c4);
        ArrayList<Card> c5= new ArrayList<>(); table.add(c5);
        ArrayList<Card> c6= new ArrayList<>(); table.add(c6);
        ArrayList<Card> c7= new ArrayList<>(); table.add(c7);

        for (int i=0; i<28; i++) {
            Card card =deck.remove(0);
            if (c1.size()<1) {card.setStack(0); c1.add(card);}
            else if (c2.size()<2) {card.setStack(1); c2.add(card);}
            else if(c3.size()<3) {card.setStack(2); c3.add(card);}
            else if (c4.size()<4) {card.setStack(3); c4.add(card);}
            else if (c5.size()<5) {card.setStack(4); c5.add(card);}
            else if (c6.size()<6) {card.setStack(5); c6.add(card);}
            else if (c7.size()<7) {card.setStack(6); c7.add(card);}
        }
        for (ArrayList<Card> s : table) {Card c = s.get(s.size()-1); c.unhide();}
        return table;
    }
    public static void showTable(ArrayList<ArrayList<Card>> table, ArrayList<Card> deck, Card[] aces, Graphics g, Image tabl) {
        //draw table
        g.drawImage(tabl, 0, 0, null);
        if (deck.size()>0) if (!deck.get(deck.size()-1).visible()) g.drawImage(back, 15, DECK_Y, null);
        //draw cards on table
        for (int i = 0; i<7; i++) {
            for (int j = 0; j<table.get(i).size(); j++) {
                g.drawImage(table.get(i).get(j).getImage(), TABLE1_X+OFFSET_X*i, TABLE_Y+OFFSET_Y*j, null);
                if (table.get(i).get(j).visible()) {
                    table.get(i).get(j).setX(TABLE1_X+OFFSET_X*i);
                    table.get(i).get(j).setY(TABLE_Y+OFFSET_Y*j);}
            }
        }
        //draw cards in deck & dealt
        int offset=0;
        if (cardmode==3) {
            for (Card c: deck) if (c.visible()) {
                g.drawImage(c.getImage(), DEALT_X+offset, DECK_Y, null);
                c.setX(DEALT_X+offset);
                c.setY(DECK_Y);
                offset+=10;
            }
        }
        if (cardmode==1) for (Card c: deck) if (c.visible()) {
            g.drawImage(c.getImage(), DEALT_X, DECK_Y, null);
            c.setX(DEALT_X);
            c.setY(DECK_Y);
        }
        //draw cards on foundation
        for (int j= 0; j<4; j++) g.drawImage(aces[j].getImage(), F_X+OFFSET_X*j, F_Y, null);
    }

    public static void animate(ArrayList<Card> deck, Graphics g) {
        if (cardmode==1) {
            int x=90;
            while (x>20) {
                g.drawImage(back, x, DECK_Y, null);
                x-=5;
                try {Thread.sleep(5);}
                catch (Exception InterruptedException) {System.out.print("oops");}}
        }
        else if (cardmode==3) {
            for (Card c: deck) if (!c.visible()) {
                int x=c.getX();
                while (x>20) {
                    g.drawImage(back, x, DECK_Y, null);
                    x-=5;
                    try {Thread.sleep(5);}
                    catch (Exception InterruptedException) {System.out.print("oops");}
                }
                break;
            }
        }
    }

    public static void dealCards(ArrayList<Card> deck) {
        if (cardmode==1) {
            int i=-1;
            for (Card c: deck) {
                if (c.visible()) i=deck.indexOf(c);
                else {c.setX(20);
                    c.setY(DECK_Y);}
                c.hide();
            }
            i++;
            if (i<deck.size()) {deck.get(i).unhide();}
            else {deck.get(0).unhide(); i=0;}
        }
        else if (cardmode==3) {
            int i=0;
            if (deck.size()>8){
                while (!deck.get(i).visible() && i<deck.size()-1) i++;//iterates until first visible card
                if (i>deck.size()-2) i=0;//if none visible start at beginning
                else while(deck.get(i).visible() && i<deck.size()-1) i++;//now iterate past visible cards
                if (i>deck.size()-1) i=0;//and start at beginning if end of deck reached
                if (deck.get(deck.size()-1).visible() && deck.get(deck.size()-2).visible()) {
                    deck.get(deck.size()-1).hide();//fixes an edge case where last few cards don't get hidden
                    deck.get(deck.size()-2).hide();
                    i=0;
                }
                for (int k=0; k<3; k++) if (i+k<deck.size()) deck.get(i+k).unhide();//shows new cards
                for (int m=0; m<i; m++) deck.get(m).hide();//hides cards smaller than i
                if (i<3) {
                    int q=0;//bug fix for random cards that don't get hidden
                    while (!deck.get(q).visible() && q<deck.size()-1) q++;
                    while(deck.get(q).visible() && q<deck.size()-1) q++;
                    while(q<deck.size()) {deck.get(q).hide(); q++;}
                }
            }
            else {//there were some weird errors when deck size<8 so this code is separate
                if (deck.size()<=3) for (Card c: deck) c.unhide();
                else {
                    if (deck.get(deck.size()-1).visible() && deck.get(deck.size()-2).visible()) {
                        deck.get(deck.size()-1).hide();//same edge case as before
                        deck.get(deck.size()-2).hide();
                        i=0;
                    }
                    while (!deck.get(i).visible() && i<deck.size()-1) {i++;}//iterates thru hidden cards
                    if (i>deck.size()-2) {
                        if (deck.get(i).visible()) deck.get(i).hide();
                        i=0;
                    }//if all cards hidden, goes to beginning
                    //there's code in movecard that displays the previous card if all 3 cards are played
                    else while(deck.get(i).visible() && i<deck.size()-1) i++;//if some cards visible, iterates thru visible cards
                    if (i>deck.size()-1) {for (Card m :deck) {m.hide();} i=0;}//if only last few cards in deck visible, hides all cards, goes to beginning
                    int k=0;
                    while (k<3) {if ((i+k)<deck.size()) {deck.get(i+k).unhide();} k++;}
                    while (i+k<deck.size()) {deck.get(i+k).hide(); k++;}//then un-hides 1-3 cards
                    for (int m=0; m<i; m++)  deck.get(m).hide();//hides cards from prev deal(s)
                }
            }
        }
    }

    public static String moveCard(ArrayList<ArrayList<Card>> table, ArrayList<Card> deck, Card[]aces, Card card) {//still debugging
        Card ta=quickCheck(table, card.getSuit(), card.getNum());
        Card de=quickCheck(deck, card.getNum(), card.getSuit());
        String s="";
        if ((ta==null && de==null)) System.out.println("couldn't find card " + card);
        else {
            int gg=-1;
            int jic=-1;
            if (de!=null) {
                card=de; s+="d ";
                jic=deck.indexOf(de);//just in case all 3 cards in the hand are played
            }
            else {card=ta; s+="t ";}
            s+=card.getNum();
            s+=card.getSuit() + " ";
            int useful=card.getStack();//a very useful integer, for doing things with the stack the card was moved from
            if (useful>-1) gg=table.get(useful).indexOf(card);
            int x= doubleCheck(table, card, -1);
            //checking aces
            for (int i=0; i<4; i++) {
                if((aces[i].getNum()>=card.getNum()-1) && (aces[i].getSuit()==card.getSuit() || aces[i].getSuit()=='y')) {
                    if (useful==-1) {
                        x=-2;
                        aces[i]=card;
                        card.setStack(-2);
                        s+="a";
                        s+=i;
                        if (deck.indexOf(card)>0) deck.get(deck.indexOf(card)-1).unhide();
                        deck.remove(card);
                        deck.remove(card);//gotta make SURE
                        break;
                    }
                    else if (useful>-1) {
                        boolean canMove=true;
                        for (Card rh : table.get(useful)) if (rh.visible() && rh.getNum()<card.getNum()) canMove=false;
                        if (canMove) {
                            x=-2;
                            aces[i]=card;
                            for (ArrayList<Card> aaaaaaa : table) aaaaaaa.remove(card);
                            if (table.get(useful).size()>0) table.get(useful).get(table.get(useful).size()-1).unhide();
                            s+="a";
                            s+=i;
                            break;
                        }
                    }
                }
            }
            if (useful>-1) if (gg==table.get(useful).size()-1) gg=-1;
            if(x>-1) {//moving card from one spot to another on table
                boolean dupe = false;
                for (Card foo : table.get(x)) if (foo.visible() && foo.getNum()<=card.getNum()) dupe=true;
                if (!dupe) {
                    table.get(x).add(card);
                    if (de!=null) {deck.remove(card); s+="t" +x; }
                    if (ta!=null) {
                        int z=table.get(useful).size()-1;
                        table.get(useful).remove(card);
                        table.get(useful).remove(card);//gotta make SURE
                        if (z>0) table.get(useful).get(z-1).unhide();
                        if (gg>-1) {
                            moveTrailingCards(table, useful, x, gg);
                            if (table.get(useful).size()-1>=0) table.get(useful).get(table.get(useful).size()-1).unhide();							}
                        s+="t" +x;
                    }
                }else {
                    int d=doubleCheck(table, card, x);
                    if (d>-1) {
                        if (ta!=null) {
                            int z=table.get(useful).size()-1;
                            for (ArrayList<Card> aaaaaaa : table) aaaaaaa.remove(card);
                            if (z>0) table.get(useful).get(z-1).unhide();
                            table.get(d).add(card);
                            if (gg>-1) {
                                moveTrailingCards(table, useful, x, gg);
                                if (table.get(useful).size()-1>=0) table.get(useful).get(table.get(useful).size()-1).unhide();

                            }
                            s+="t" +d;
                        }
                        else if (de!=null) {deck.remove(card); table.get(d).add(card); s+="t" +d; }
                    }
                }
                card.setStack(x);
                boolean allCardsHidden=true;
                for (Card carddd : deck) if (carddd.visible()) allCardsHidden=false;
                if (allCardsHidden && jic>0) deck.get(jic-1).unhide();
            }
            else if (x==-1) {System.out.print("you can't move this card.");}
        }
        return s;
    }
    public static void moveTrailingCards(ArrayList<ArrayList<Card>> table, int from, int to, int i){
        while (i<table.get(from).size()) {
            table.get(to).add(table.get(from).get(i));
            table.get(from).get(i).setStack(to);
            table.get(from).remove(i);
        }
    }
    public static void animate(String s, ArrayList<ArrayList<Card>> table, ArrayList<Card> deck, Card[] aces, Graphics g, Image tabl) {
        //read number and suit of card
        //sample string: "d 12d a4" "t 6s t3"
        //anywhere you see 48 it's for ascii conversion
        //sometimes it glitches :(
        int num=0;
        char suit = 'a';
        Card card=null;
        int x1; int y1;
        boolean trail=false;
        Image img=null;
        if (s.length()<7) System.out.print("can't move card" + s);
        else {
            //find card in deck/table, this is needed to get the image
            if (s.charAt(4)==' ') {num=s.charAt(2)-48; suit=s.charAt(3);}
            else { num=(s.charAt(3)-48)+10; suit=s.charAt(4);}
            card= quickCheck(table, suit, num);
            if (card==null) {
                for (int p=0; p<4; p++) if (aces[p].getNum()==num && aces[p].getSuit()==suit) card=aces[p];
            }
            //get x and y coords it's moving to
            if (s.charAt(6)=='a') {
                x1=F_X+OFFSET_X*(s.charAt(7)-48);
                y1=F_Y;
                for (int p=0; p<4; p++) if (aces[p].getNum()==num && aces[p].getSuit()==suit) card=aces[p];
                String q=""; q+=card.getNum()-1; q+=card.getSuit()+".png";
                if (card.getNum()>1) {
                    try {img=ImageIO.read(new File(q));}
                    catch(Exception IOException) {System.out.print("couldn't find image");}
                }
                card.setStack(-2);
            }
            else if (s.charAt(6)=='t'){
                x1=TABLE1_X+OFFSET_X*(s.charAt(7)-48);
                y1=TABLE_Y + (table.get(s.charAt(7)-48).indexOf(card))*OFFSET_Y;//take note that when this is run card is already 'moved'
                for (Card ca : table.get(s.charAt(7)-48)) if (ca.getSuit()==suit && ca.getNum()==num) card=ca;
                if (s.charAt(0)=='t' && table.get(s.charAt(7)-48).indexOf(card)<table.get(s.charAt(7)-48).size()-1) trail=true;
            }
            else if (s.charAt(5)=='a') {
                x1=F_X+OFFSET_X*(s.charAt(6)-48)+10;
                y1=F_Y;
                for (int p=0; p<4; p++) if (aces[p].getNum()==num && aces[p].getSuit()==suit) card=aces[p];
                String q=""; q+=card.getNum()-1; q+=card.getSuit()+".png";
                if (card.getNum()>1) {
                    try {img=ImageIO.read(new File(q));}
                    catch(Exception IOException) {System.out.print("couldn't find image");}
                }
                card.setStack(-2);
            }
            else {//if char at 5 == t
                x1=TABLE1_X+OFFSET_X*(s.charAt(6)-48);
                y1=TABLE_Y + (table.get(s.charAt(6)-48).indexOf(card))*OFFSET_Y;//take note that when this is run card is already 'moved'
                for (Card ca : table.get(s.charAt(6)-48)) if (ca.getSuit()==suit && ca.getNum()==num) card=ca;
                if (s.charAt(0)=='t' && table.get(s.charAt(6)-48).indexOf(card)<table.get(s.charAt(6)-48).size()-1) trail=true;
            }
            //get x and y coords of where card was, these aren't updated until show table is called so can still be used
            int x=card.getX(); int y=card.getY();
            //make a vector pointing to destination
            double d = Math.sqrt((y1-y)*(y1-y)+(x1-x)*(x1-x));
            double xmove=3*(x1-x)/d;
            double ymove=3*(y1-y)/d;
            double dx=x; double dy=y;
            int counter=0;
            while (Math.abs(x1-x)>5 || Math.abs(y1-y)>5) {
                dx+=xmove; dy+=ymove;
                x=(int) Math.round(dx);
                y= (int) Math.round(dy);
                //modified version of showtable, doesn't update any xy coords and doesn't display cards still being moved
                if (counter%5==0)showTable(table, deck, aces, g, tabl, card, img);
                g.drawImage(card.getImage(), x, y, null);
                if (trail) {//finds other cards in stack, moves them on a delay
                    int sz=table.get(card.getStack()).size();
                    int q = table.get(card.getStack()).indexOf(card)+1;
                    for (int r=q; r<sz; r++) {
                        int p=r;
                        Image im =table.get(card.getStack()).get(p).getImage();
                        if (x>x1) g.drawImage(im, x+((p-q)*5), y+((p-q+1)*OFFSET_Y), null);
                        else g.drawImage(im, x-((p-q)*5), y+((p-q+1)*OFFSET_Y), null);
                    }
                }
                counter++;
                try {Thread.sleep(5);}
                catch (Exception InterruptedException) {System.out.print("oops");}
            }
            x=x1; y=y1;
        }
        showTable(table, deck, aces, g, tabl);
    }

    public static void showTable(ArrayList<ArrayList<Card>> table, ArrayList<Card> deck, Card[] aces, Graphics g, Image tabl, Card card, Image img) {
        //this one doesn't update the x y positions of any of the cards
        //it also should avoid redrawing cards i'm already moving
        //remember this method is being called AFTER the card has moved in all ways but visually
        int stack=card.getStack();//returns stack card is moving to
        //draw table
        g.drawImage(tabl, 0, 0, null);
        if (!deck.isEmpty()) if (!deck.get(deck.size()-1).visible()) g.drawImage(back, 15, DECK_Y, null);
        //draw cards on table
        for (int i = 0; i<7; i++) {
            if (i!=stack) for (int j = 0; j<table.get(i).size(); j++) g.drawImage(table.get(i).get(j).getImage(), TABLE1_X+OFFSET_X*i, TABLE_Y+OFFSET_Y*j, null);
            else {//draws cards until it gets to the index of the card(s) that's moving then stops. if stack=-1 draws as normal
                for (int j=0; j<table.get(stack).indexOf(card); j++) g.drawImage(table.get(i).get(j).getImage(), TABLE1_X+OFFSET_X*i, TABLE_Y+OFFSET_Y*j, null);
            }
        }
        //draw cards in deck & dealt
        int offset=0;
        for (Card c: deck) if (c.visible()) {//this will already draw cards except the one that's being dealt, no moving cards to deck so no need to change
            g.drawImage(c.getImage(), DEALT_X+offset, DECK_Y, null);
            offset+=10;
        }
        //draw cards on foundation
        for (int j= 0; j<4; j++) {//check aces to see if any of them match the card, if so, don't update image
            if (!aces[j].equals(card)) g.drawImage(aces[j].getImage(), F_X+OFFSET_X*j, F_Y, null);
            else if (aces[j].getNum()>1) g.drawImage(img, F_X+OFFSET_X*j, F_Y, null);
        }
    }
    public static Card quickCheck(ArrayList<ArrayList<Card>> table, char suit, int num) {
        for (ArrayList<Card> a: table) for (Card c: a) if (c.getSuit()==suit && c.getNum()==num && c.visible()) return c;
        return null;
    }
    public static Card quickCheck(ArrayList<Card> deck, int num, char suit) {
        for (Card c: deck) if (c.visible() && (c.getSuit()==suit) && c.getNum()==num) return c;
        return null;
    }
    public static int doubleCheck(ArrayList<ArrayList<Card>> table, Card card, int no) {
        int moveTo=-1;
        for (int i = 6; i>=0; i--) {
            if (table.get(i).size()<1&&card.getNum()==13) moveTo=i;
            else for (Card c: table.get(i))
                if (table.indexOf(table.get(i))!=no && c.visible() && c.getNum()==card.getNum()+1 && table.get(i).indexOf(c)==table.get(i).size()-1){ //could have made a card color property but ehhhhhhhh
                    if ((c.getSuit()=='s' || c.getSuit()=='c') && (card.getSuit()=='h' || card.getSuit()=='d')) moveTo=i;
                    if ((c.getSuit()=='h' || c.getSuit()=='d') && (card.getSuit()=='s' || card.getSuit()=='c')) moveTo=i;
                    if ((c.getNum()<=card.getNum()&&c.visible())) moveTo=-1;
                }
        }
        return moveTo;
    }
    public static boolean checkwin(Card[] aces) {
        boolean GameWon=true;
        for (int i =0; i<4; i++) if (aces[i].getNum()<13) GameWon=false;
        return GameWon;
    }
    public static void fireworks (Graphics g, Card[] aces, Image tabl) {
        Image f=null;
        Image e=null;
        Image table=null;
        try {f=ImageIO.read(new File("f.png")); e=ImageIO.read(new File("e.png")); table = ImageIO.read(new File("table.png"));}
        catch (Exception IOException) {System.out.print("check filename");}
        //generate first image somewhere offscreen
        for (int i=0; i<15; i++) {
            int x=(int)(Math.random()*450+50);
            int y= (int)(Math.random()*350+50);
            int x1=(int)(Math.random()*450+50);
            int y1= (int)(Math.random()*350+50);

            for (int j=0; j<10; j++) {
                g.drawImage(e, x, y, x+32, y+36, j*32, 0, (j+1)*32, 36, null);
                g.drawImage(e, x1, y1, x1+32, y1+36, j*32, 0, (j+1)*32, 36, null);
                try {Thread.sleep(5);}
                catch (Exception InterruptedException) {System.out.print("oops");}
                drawwongame(g, aces, tabl);

            }
            try {Thread.sleep(50);}
            catch (Exception InterruptedException) {System.out.print("oops");}

        }

    }
    public static void drawwongame(Graphics g, Card[] aces, Image tabl) {
        g.drawImage(tabl, 0, 0, null);
        for (int j= 0; j<4; j++) g.drawImage(aces[j].getImage(), F_X+OFFSET_X*j, F_Y, null);
    }

}
