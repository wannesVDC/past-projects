package domain;

import java.util.Comparator;

public class Punt{

    private int x;
    private int y;

    public Punt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean equals(Punt _punt) {
        if(_punt == null) return false;
        if(this.x == _punt.x && this.y == _punt.y) return true;
        return false;
    }

    public String toString(){
        return "X: "+this.x +" Y: "+this.y;
    }

    static Comparator<Punt> xComparator = new Comparator<Punt>() {
        @Override
        public int compare(Punt p1, Punt p2) {
            if(p1.x == p2.x) return 0;
            if(p1.x < p2.x) return -1;
            else return 1;
        }

    };

    static Comparator<Punt> yComparator = new Comparator<Punt>() {
        @Override
        public int compare(Punt p1, Punt p2) {
            if(p1.y == p2.y) return 0;
            if(p1.y < p2.y) return -1;
            else return 1;
        }

    };

    public boolean compareTo(Punt _punt) {
        if(java.util.Objects.equals(this, _punt)) return true;
        else return false;
    }
}
