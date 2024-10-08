package org.example;

public class Episode {

    private String episodeName;
    private String duration;

    public Episode(String episodeName, String duration){
        this.episodeName = episodeName;
        this.duration = duration;
    }

    public void setEpisodeName(String episodeName){
        this.episodeName = episodeName;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "episodeName='" + episodeName + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
