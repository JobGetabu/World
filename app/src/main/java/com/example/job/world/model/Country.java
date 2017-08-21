package com.example.job.world.model;

/**
 * Created by JOB on 8/18/2017.
 */

public class Country {
    private int rank;
    private String name;
    private String population;
    private String flag;

    public Country() {
    }

    public Country(int rank, String name, String population, String flag) {
        this.rank = rank;
        this.name = name;
        this.population = population;
        this.flag = flag;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Country{" +
                "rank=" + rank +
                ", name='" + name + '\'' +
                ", population='" + population + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

}
