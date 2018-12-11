import java.math.BigInteger;

public class day09 {
    static Link circle;

    static class Link {
        int value;
        Link next;
        Link prev;
        boolean current;

        Link(int value) {
            this.value = value;
            next = this;
            prev = this;
        }
        
        Link add(int value) {
            Link c1 = next;
            Link c2 = next.next;
            Link v = c1.setNext(value);
            current = false;
            v.current = true;
            return v;
        }

        Link delete() {
            Link c = prev;
            if (prev == this) {
                return c;
            }
            next.prev = c;
            c.next = next;
            next = prev = null;

            return c.next;
        }

        Link setNext(int value) {
            return setNext(new Link(value));
        }

        Link setNext(Link n) {
            next.prev = n;
            n.next = next;
            n.prev = this;
            next = n;

            return n;
        }

        void print() {
            Link p = this;

            do {
                System.out.print(((p.current) ? "(" : "") + p.value + ((p.current) ? ")" : "") + " ");
                p = p.next;
            } while (p != this);
            System.out.println("");
        }
    }

    static class Player {
        int id;
        BigInteger score = BigInteger.ZERO;

        Player(int id) {
            this.id = id;
        }

        Link play(int marble, Link current) {
            if ((marble % 23) == 0) {
                Link n = current;
                for (int j=0; j < 7; j++) {
                    n = n.prev;
                }

                score = score.add(BigInteger.valueOf(((long)marble + n.value)));
                current.current = false;
                current = n.delete();
                current.current = true;

                return current;
            } else {
                return current.add(marble);
            }
        }
    }

    static void printHiScore(Player[] players, int hiscoreCheck) {
        BigInteger  max = BigInteger.ZERO;
        int id = 0;
        for (Player p : players) {
            if (p.score.compareTo(max) == 1) {
                max = p.score;
                id = p.id;
                
            }
        }
        if (hiscoreCheck == -1) {
            System.out.println("Player "+id+" hiscore="+max);
        } else {
            System.out.println("Player "+id+" hiscore="+max+" "+(max.compareTo(new BigInteger(Integer.toString(hiscoreCheck))) == 0));
        }
    }

    public static void main(String args[]) {
        int nplayers = Integer.parseInt(args[0]);
        int lastMarble = Integer.parseInt(args[1]);
        int hiscore = (args.length > 2) ? Integer.parseInt(args[2]) : -1;
        Link circle = new Link(0);
        Link current = circle;

        current.current = true;

        Player[] players = new Player[nplayers];

        for (int i=0; i < players.length; i++) {
            players[i] = new Player(i+1);
        }

        int p = 0;
        for (int marble = 1; marble <= lastMarble; marble++) {
            Player player = players[p];
            current = player.play(marble, current);
            p = (p + 1) % players.length;
        }

        printHiScore(players, hiscore);
    }
}
    
