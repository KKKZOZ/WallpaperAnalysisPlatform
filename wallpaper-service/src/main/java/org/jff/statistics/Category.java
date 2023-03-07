package org.jff.statistics;

import java.util.List;

public class Category {

    public static final String[] categoryList = {"Abstract","Animal","Anime","Everyone","Fantasy","Game",
            "Girls","Landscape","Memes","MMD","Music","Nature","Pixel art","Relaxing","Sci-Fi","Technology",
            "Television","Unspecified","Vehicle"};

    public static List<String> getCategoryList() {
        return List.of(categoryList);
    }

}
