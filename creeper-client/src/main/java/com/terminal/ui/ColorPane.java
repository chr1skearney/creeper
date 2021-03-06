package com.terminal.ui;

import com.intellij.ui.ColorUtil;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorPane extends JTextPane {
    public static final Color D_Black = new Color(0x000000);
    public static final Color D_Red = new Color(0xcd0000);
    public static final Color D_Blue = new Color(0x1e90ff);
    public static final Color D_Magenta = new Color(0xcd00cd);
    public static final Color D_Green = new Color(0x00cd00);
    public static final Color D_Yellow = new Color(0xcdcd00);
    public static final Color D_Cyan = new Color(0x00cdcd);
    public static final Color D_White = new Color(0xe5e5e5);
    public static final Color B_Black = new Color(0x4c4c4c);
    public static final Color B_Red = new Color(0xff0000);
    public static final Color B_Blue = new Color(0x4682b4);
    public static final Color B_Magenta = new Color(0xff00ff);
    public static final Color B_Green = new Color(0x00ff00);
    public static final Color B_Yellow = new Color(0xffff00);
    public static final Color B_Cyan = new Color(0x00FFFF);
    public static final Color B_White = new Color(0xffffff);
    public static final Color cReset = Color.getHSBColor(0.000f, 0.000f, 1.000f);
    static Color colorCurrent = cReset;
    String remaining = "";

    private String lastAppendAnsi;

    public ColorPane() {
        setBackground(Color.BLACK);
        setFont(getTerminalFont());
        enableInputMethods(false);
        setEditable(true);;
        getCaret().setVisible(false);
        setHighlighter(null);
        setFocusable(false);
        //UIManager.put("TextPane.disabledBackground", Color.BLACK);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Stop the event from propagating.
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

    }

    public void append(boolean bold, Color c, String s) {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        if (bold) {
            c = c.brighter().brighter();
        }
        AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        int len = getDocument().getLength(); // same value as getText().length();
        setCaretPosition(len);  // place caret at the end (with no selection)
        setCharacterAttributes(attributeSet, false);
        replaceSelection(s); // there is no selection, so inserts at caret
    }

    public void appendANSI(String s) { // convert ANSI color codes first
        if (s.equals(lastAppendAnsi)) {
            return;
        }
        setText("");
        int aPos = 0;   // current char position in addString
        int aIndex = 0; // index of next Escape sequence
        int mIndex = 0; // index of "m" terminating Escape sequence
        String tmpString = "";
        boolean stillSearching = true; // true until no more Escape sequences
        String addString = remaining + s;
        remaining = "";
        boolean boldOn = false;

        if (addString.length() > 0) {
            aIndex = addString.indexOf("\u001B"); // find first escape
            if (aIndex == -1) { // no escape/color change in this string, so just send it with current color
                append(boldOn, colorCurrent, addString);
                return;
            }
// otherwise There is an escape character in the string, so we must process it

            if (aIndex > 0) { // Escape is not first char, so send text up to first escape
                tmpString = addString.substring(0, aIndex);
                append(boldOn, colorCurrent, tmpString);
                aPos = aIndex;
            }
// aPos is now at the beginning of the first escape sequence

            stillSearching = true;
            while (stillSearching) {
                mIndex = addString.indexOf("m", aPos); // find the end of the escape sequence
                if (mIndex < 0) { // the buffer ends halfway through the ansi string!
                    remaining = addString.substring(aPos, addString.length());
                    stillSearching = false;
                    continue;
                } else {
                    tmpString = addString.substring(aPos, mIndex + 1);
                    colorCurrent = getANSIColor(tmpString);
                    if (tmpString.contains("\u001B[1m")) {
                        boldOn = true;
                    } else if (tmpString.equals("\u001B[0m")) {
                        boldOn = false;
                    }
                }
                aPos = mIndex + 1;
// now we have the color, send text that is in that color (up to next escape)

                aIndex = addString.indexOf("\u001B", aPos);

                if (aIndex == -1) { // if that was the last sequence of the input, send remaining text
                    tmpString = addString.substring(aPos, addString.length());
                    append(boldOn, colorCurrent, tmpString);
                    stillSearching = false;
                    continue; // jump out of loop early, as the whole string has been sent now
                }

                // there is another escape sequence, so send part of the string and prepare for the next
                tmpString = addString.substring(aPos, aIndex);
                aPos = aIndex;
                append(boldOn, colorCurrent, tmpString);

            } // while there's text in the input buffer
        }
        lastAppendAnsi = s;
    }

    public Color getANSIColor(String ANSIColor) {
        if (ANSIColor.equals("\u001B[30m")) {
            return D_Black;
        } else if (ANSIColor.equals("\u001B[31m")) {
            return D_Red;
        } else if (ANSIColor.equals("\u001B[32m")) {
            return D_Green;
        } else if (ANSIColor.equals("\u001B[33m")) {
            return D_Yellow;
        } else if (ANSIColor.equals("\u001B[34m")) {
//            return D_Blue;
            return D_Blue;
        } else if (ANSIColor.equals("\u001B[35m")) {
            return D_Magenta;
        } else if (ANSIColor.equals("\u001B[36m")) {
            return D_Cyan;
        } else if (ANSIColor.equals("\u001B[37m")) {
            return D_White;
        } else if (ANSIColor.equals("\u001B[0;30m")) {
            return D_Black;
        } else if (ANSIColor.equals("\u001B[0;31m")) {
            return D_Red;
        } else if (ANSIColor.equals("\u001B[0;32m")) {
            return D_Green;
        } else if (ANSIColor.equals("\u001B[0;33m")) {
            return D_Yellow;
        } else if (ANSIColor.equals("\u001B[0;34m")) {
            return D_Blue;
        } else if (ANSIColor.equals("\u001B[0;35m")) {
            return D_Magenta;
        } else if (ANSIColor.equals("\u001B[0;36m")) {
            return D_Cyan;
        } else if (ANSIColor.equals("\u001B[0;37m")) {
            return D_White;
        } else if (ANSIColor.equals("\u001B[1;30m")) {
            return B_Black;
        } else if (ANSIColor.equals("\u001B[1;31m")) {
            //return B_Red;
            return B_Red;
        } else if (ANSIColor.equals("\u001B[1;32m")) {
            return B_Green;
        } else if (ANSIColor.equals("\u001B[1;33m")) {
            return B_Yellow;
        } else if (ANSIColor.equals("\u001B[1;34m")) {
           // return B_Blue;
            return B_Blue;
        } else if (ANSIColor.equals("\u001B[1;35m")) {
            return B_Magenta;
        } else if (ANSIColor.equals("\u001B[1;36m")) {
            return B_Cyan;
        } else if (ANSIColor.equals("\u001B[1;37m")) {
            return B_White;
        } else if (ANSIColor.equals("\u001B[0m")) {
            return cReset;
        } else {
            return B_White;
        }
    }

    public static Font getTerminalFont() {
        String fontName;
        if (UIUtil.isWindows) {
            fontName = "Consolas";
        } else if (UIUtil.isMac) {
            fontName = "Menlo";
        } else {
            fontName = "Monospaced";
        }
        return new Font(fontName, Font.PLAIN, 12);
    }
}
