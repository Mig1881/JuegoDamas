package com.svalero.juegodamas;
import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;
public class Colors {
    public String colored_text (String text, int number_color){
        return (colorize(text, TEXT_COLOR(number_color)));
    }
    public String colored_ground (String text, int number_color){
        return (colorize(text, BACK_COLOR(number_color)));
    }
    public String colored_groundText(String text, int number_color_text, int number_color_ground){
        Attribute[] myFormat = new Attribute[]{TEXT_COLOR(number_color_text), BACK_COLOR(number_color_ground)};
        return (colorize(text, myFormat));
    }
}


