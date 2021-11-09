package com.example.proto_type_1;

public class Routs {
    String Customize_Rout_Number, Customize_Source_Point, Customize_Destination_Point;

    public Routs(String customize_Rout_Number, String customize_Source_Point, String customize_Destination_Point) {
        Customize_Rout_Number = customize_Rout_Number;
        Customize_Source_Point = customize_Source_Point;
        Customize_Destination_Point = customize_Destination_Point;
    }

    public String getCustomize_Rout_Number() {
        return Customize_Rout_Number;
    }

    public String getCustomize_Source_Point() {
        return Customize_Source_Point;
    }

    public String getCustomize_Destination_Point() {
        return Customize_Destination_Point;
    }
}
