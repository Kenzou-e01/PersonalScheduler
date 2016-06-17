package com.example.paholik.personalscheduler;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class User extends SugarRecord {
    String name;
    String pass;

    public User() {}

    public User(String _name, String _pass, ArrayList<Tag> _tags) {
        name = _name;
        pass = _pass;
        // not necessary now
        /*for(Tag t : _tags) {
            UserTag userTag = new UserTag(this.getId(), t.getId());
            userTag.save();
        }*/
    }

    /**
     * Return list of user's tags of interest
     * */
    public List<Tag> getTags() {
        List<UserTag> userTagList = UserTag.find(UserTag.class, "user_ID = ?", this.getId().toString());

        List<Tag> tags = new ArrayList<>();
        if(userTagList != null) {
            for (UserTag userTag : userTagList) {
                Tag t = Tag.findById(Tag.class, userTag.tagID);
                tags.add(t);
            }
        }

        return tags;
    }

    /**
     * Add new tag to user's tags of interest
     * */
    public void addTag(Tag t) {
        // check if there already exists tag in user's tag list
        List<UserTag> userTagList = UserTag.find(UserTag.class, "user_ID = ? and tag_ID = ?", this.getId().toString(), t.getId().toString());
        if(userTagList == null) {
            UserTag userTag = new UserTag(this.getId(), t.getId());
            userTag.save();
        }
    }
}
