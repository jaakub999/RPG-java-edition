package API;

public enum Buff {
    WATER
            {
                @Override
                public String toString() {return "Woda";}
            },
    FIRE
            {
                @Override
                public String toString() {return "Ogień";}
            },
    WIND
            {
                @Override
                public String toString() {return "Wiatr";}
            },
    SOIL
            {
                @Override
                public String toString() {return "Ziemia";}
            },
    NULL
}