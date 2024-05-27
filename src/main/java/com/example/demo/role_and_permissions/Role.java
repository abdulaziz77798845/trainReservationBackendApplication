package com.example.demo.role_and_permissions;
import java.util.Arrays;
import java.util.List;


public enum Role {
    ADMIN(Arrays.asList(Permissions.ADD_TRAINS,Permissions.DELETE_TRAINS,Permissions.UPDATE_TRAINS,Permissions.GET_ALL_TRAINS,Permissions.ADD_TRAINS,Permissions.ADD_ADMINS)),
    PASSENGER(Arrays.asList(Permissions.BOOK_TICKET,Permissions.CANCEL_TICKET,Permissions.GET_ALL_TRAINS));

    private List<Permissions> permissions;

    Role(List<Permissions> permissions){
        this.permissions=permissions;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }
}