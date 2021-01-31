package DataBase;

public class Operation {
    private String type;
    private String libelle;
    private Float somme;
    private String date;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Operation(String type, String libelle, Float somme, String date) {
        this.type = type;
        this.libelle = libelle;
        this.somme = somme;
        this.date = date;
    }

    public Operation() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Float getSomme() {
        return somme;
    }

    public void setSomme(Float somme) {
        this.somme = somme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
