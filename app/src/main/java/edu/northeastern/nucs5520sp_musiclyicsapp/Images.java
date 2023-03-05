package edu.northeastern.nucs5520sp_musiclyicsapp;

public class Images {
    private int count;
    private String url;

    public Images(){}

    public Images(String url, int count)
    {
        this.url = url;
        this.count = count;
    }

    public int getCount()
    {
        return count;
    }

    public String getUrl()
    {
        return url;
    }

    public void setCount(int num)
    {
        count = num;
    }

    public void setUrl(String newurl)
    {
        url = newurl;
    }
}
