package com.ashwani.averagepixelintensity;

/**
 * Created by ashwa on 7/10/2017.
 */

public class MyColor {

    private int RED;
    private int GREEN;
    private int BLUE;

    private int population;

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getRED() {
        return RED;
    }

    public void setRED(int RED) {
        this.RED = RED;
    }

    public int getGREEN() {
        return GREEN;
    }

    public void setGREEN(int GREEN) {
        this.GREEN = GREEN;
    }

    public int getBLUE() {
        return BLUE;
    }

    public void setBLUE(int BLUE) {
        this.BLUE = BLUE;
    }

    public boolean isEqual(MyColor myColor){
        if(this.RED >= myColor.getRED()-20 &&
                this.RED <= myColor.getRED()+20 &&
                this.GREEN >= myColor.getGREEN()-20 &&
                this.GREEN <= myColor.getGREEN()+20 &&
                this.BLUE >= myColor.getBLUE()-20&&
                this.BLUE <= myColor.getBLUE()+20){
            return true;
        }else{
            return false;
        }
    }
}
