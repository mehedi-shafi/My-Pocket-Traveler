package Utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Models.Travel;

import static java.lang.System.in;

/**
 * Created by shafi on 7/18/2017.
 */

public  class UtilityFunctions {

    public  ArrayList<String> tagSplitter(String tags){
        String [] splits = tags.split(",");
        ArrayList<String> tagList = new ArrayList<>();

        for (String s : splits){
            tagList.add(s);
        }
        return tagList;
    }

    public String getStarBack(int n){
        String star = "";
        for (int i = 0; i < n; i++){
            star += "*";
        }
        return star;
    }

    public int properRatingGet (String aString){
        int toRet = 0;

        for (int i = 0; i < aString.length(); i++){
            if (!isNum(aString.charAt(i)))
                return  0;
        }
        toRet = Integer.parseInt(aString);
        return toRet;
    }

    private boolean isNum (char c){
        if (c >= '0' && c <= '9')
            return true;
        return false;
    }

    public String getTagsFromList(ArrayList<String> tags){
        String ret = "";
        for (String x : tags){
            ret += x + ", ";
        }
        return ret;
    }

    public ArrayList<Travel> getUniqueTravels (ArrayList<Travel> travels){
        ArrayList<Travel> ret = new ArrayList<>();
        boolean add;
        for (int i = 0; i < travels.size(); i++){
            add = true;
            for (int j = 0; j < travels.size(); j++){
                if (i == j)
                    continue;
                if (travels.get(i).getDatabaseID() == travels.get(j).getDatabaseID()){
                    add = false;
                }
            }
            if(add)
              ret.add(travels.get(i));
        }

        return  ret;
    }

    public String getStarBackFromString (String stars){
        int count = Integer.parseInt(stars);
        String ret = "";
        for (int i = 0; i < count; i++){
            ret += "*";
        }
        return ret;
    }
}
