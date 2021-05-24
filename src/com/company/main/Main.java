package com.company.main;

import com.company.display.Display;
import com.company.game.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        Game tanks = new Game();
        tanks.start();
    }
}
