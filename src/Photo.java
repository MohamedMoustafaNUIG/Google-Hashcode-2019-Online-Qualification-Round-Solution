import java.util.ArrayList;

public class Photo {
    public int top;
    private boolean horizontal;
    private int tagNum;
    private ArrayList<String> tags= new ArrayList(tagNum);
    private boolean used;
    private int id;

    public Photo() {
        top=0;
        horizontal = true;
        tagNum = 0;
        used=false;
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
