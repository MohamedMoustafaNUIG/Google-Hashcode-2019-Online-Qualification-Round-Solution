import java.util.ArrayList;

public class Slide {

    private ArrayList<String> tags = new ArrayList();
    private ArrayList<Photo> photos= new ArrayList();
    private int top;
    private boolean used;
    private int id;
    
    public Slide() {
        top=0;
        used=false;
    }
    
    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addPhoto(Photo input) {
        photos.add(top,input);
        top++;

    }
    public int getSize(){
        return photos.size();
    }
    public int getPhotoId(int index){
        return photos.get(index).getId();
    }

    public void setTags()
    {
        if(photos.size()==2)
        {
            ArrayList<String> tags_1=photos.get(0).getTags();
            ArrayList<String> tags_2=photos.get(1).getTags();
            for(String temp:tags_1)
            {
                tags.add(temp);
            }
            for(String temp:tags_2)
            {
                if(tags.contains(temp))
                {
                    continue;
                }else
                    {
                        tags.add(temp);
                    }
            }
        }else
            {
                tags=photos.get(0).getTags();
            }
    }

    public ArrayList<String> getTags()
    {
        return tags;
    }

}
