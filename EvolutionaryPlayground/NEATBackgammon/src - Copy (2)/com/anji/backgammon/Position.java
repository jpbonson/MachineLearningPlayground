package com.anji.backgammon;

public class Position
{
        private Color color;
        private int quantityCheckers;
        
        Position()
        {
            this.color = Color.NEUTRAL;
            quantityCheckers = 0;
        }
        
        Position(Color color)
        {
            this.color = color;
            quantityCheckers = 0;
        }
        
        Position(Color color, int quantityCheckers)
        {
            this.color = color;
            this.quantityCheckers = quantityCheckers;
        }
        
        public boolean addChecker(Color color)
        {
            if(this.color != Color.NEUTRAL && this.color != color)
                return false;
            quantityCheckers++;
            if(this.color == Color.NEUTRAL)
                this.color = color;
            return true;
        }
        
        public boolean removeChecker(Color color)
        {
            if(this.color != color)
                return false;
            if(quantityCheckers == 0)
                return false;
            quantityCheckers--;
            if(quantityCheckers == 0)
                this.color = Color.NEUTRAL;
            return true;
        }
        
        public void setColor(Color color)
        {
            this.color = color;
        }
        
        public void setQuantityCheckers(int quant)
        {
            quantityCheckers = quant;
        }
        
        public void setColorsetQuantityCheckers(Color color, int quant)
        {
            this.color = color;
            quantityCheckers = quant;
        }
        
        public Color getColor()
        {
            return color;
        }
        
        public int getQuantityCheckers()
        {
            return quantityCheckers;
        }
        
        @Override
        public String toString()
        {
            return "["+quantityCheckers+"]"+color;
        }
}
