package addPersonnel;

/*
# Name:             Ian Taylor
# CourseID:         SER 422
# Date:             05/24/2021
# Vers:             1.0.0
# Description:      A Handles functionality to add a new person.
*/

public class Person {

    private String fName;
    private String lName;
    private String [] pLang;
    private String [] availability;
    private String [] favMovies;

    public Person(String pFName, String pLName, String [] pPLang, String [] pAvailability, String [] pFavMovie){
        this.fName=pFName;
        this.lName=pLName;
        this.pLang=pPLang;
        this.availability=pAvailability;
        this.favMovies=pFavMovie;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String[] getpLang() {
        return pLang;
    }

    public void setpLang(String[] pLang) {
        this.pLang = pLang;
    }

    public String[] getAvailability() {
        return availability;
    }

    public void setAvailability(String[] availability) {
        this.availability = availability;
    }

    public String[] getFavMovies() {
        return favMovies;
    }

    public void setFavMovies(String[] favMovies) {
        this.favMovies = favMovies;
    }
}
