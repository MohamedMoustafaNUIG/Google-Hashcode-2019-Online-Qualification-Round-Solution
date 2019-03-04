import java.util.ArrayList;

public class Photo {
    public int top = 0;
    private boolean horizontal;
    private int tagNum;
    private ArrayList<String> tags= new ArrayList(tagNum);
    private boolean used = false;
    private int id;



    // constructor
    public Photo() {
        horizontal = true;
        tagNum = 0;
    }

    public void addArray(String item) {
        tags.add(top,item);
        top++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public int getTagNum() {
        return tagNum;
    }

    public void setTagNum(int tagNum) {
        this.tagNum = tagNum;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

}
