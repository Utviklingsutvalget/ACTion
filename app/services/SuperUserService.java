package services;

import com.avaje.ebean.Ebean;
import models.SuperUser;

import java.util.List;

public class SuperUserService {

    public List<SuperUser> findAll() {
        return Ebean.find(SuperUser.class).findList();
    }
}
