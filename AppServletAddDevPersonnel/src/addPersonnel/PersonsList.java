package addPersonnel;
/*
# Name:			    Ian Taylor
# CourseID: 		SER 422
# Date:			    05/24/2021
# Vers:             1.0.0
# Description:      Handles functionality to add a new person. Implemented using the Singleton
                    Design Pattern.
*/

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PersonsList {

    private static String _filename = null;
    private static PersonsList instanceOfList = new PersonsList();

    //private List <Person> personList = new LinkedList<>();
    private ArrayList<Person> personList = new ArrayList<>();

    private PersonsList(){}

    public void initFileName(String p_filename){
        _filename = p_filename;
    }

    public static PersonsList getInstance(){
        return instanceOfList;
    }

    public void readInJSONArray(){
        JSONArray jsonList = null;
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(_filename))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray jArray = (JSONArray) obj;

            if (jArray != null) {
                for (int i=0;i<jArray.size();i++){
                    personList.add((Person) jArray.get(i));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean addPerson(Person newP){

        readInJSONArray();
        boolean nameExists = false;

        for (Person p : personList){
            if (p.getfName().equals(newP.getfName()) && p.getlName().equals(newP.getlName())) {
                nameExists = true;
                p.setAvailability(newP.getAvailability());
                p.setFavMovies(newP.getFavMovies());
                break;
            }
        }

        if(!nameExists){
            personList.add(newP);
        }
        writeJSONArray();
        return true;
    }

    public void writeJSONArray(){
        //Write JSON file

        JSONArray jsArray = new JSONArray();

        jsArray.add(personList);

        try (FileWriter file = new FileWriter(_filename)) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(jsArray.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int listSize(){
        return personList.size();
    }

    public List<Person> getList(){
        readInJSONArray();
        return personList;
    }
}
