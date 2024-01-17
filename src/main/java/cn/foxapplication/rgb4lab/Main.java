package cn.foxapplication;

import com.formdev.flatlaf.FlatIntelliJLaf;
import lombok.Getter;

import javax.swing.*;

public class Main {
    static{
        FlatIntelliJLaf.setup();
        UIManager.put( "Button.arc", 0 );
        UIManager.put( "Component.arc", 0 );
        UIManager.put( "CheckBox.arc", 0 );
        UIManager.put( "ProgressBar.arc", 0 );
    }
    @Getter
    private static JFrame frame;
    public static void main(String[] args) {
        frame = new JFrame();
        

    }
}