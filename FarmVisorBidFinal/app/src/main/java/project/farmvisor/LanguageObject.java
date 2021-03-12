package project.farmvisor;

/**
 * Created by admin on 11/05/2017.
 */

public class LanguageObject {

    int id;
    String language;

    public LanguageObject(int id,String language){
        this.id=id;
        this.language=language;

    }

    public int getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

}
