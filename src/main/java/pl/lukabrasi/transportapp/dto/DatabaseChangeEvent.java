package pl.lukabrasi.transportapp.dto;

public class DatabaseChangeEvent {

    private String changeDescription;

    public DatabaseChangeEvent(String changeDescription){
        this.changeDescription = changeDescription;
    }

    public String getChangeDescription(){
        return changeDescription;
    }
}
