/*
 * Created on Apr 24, 2004
 * for MIT 6.170, spring 2004
 * (asteroids)
 */
 

import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The KeyInterpreter is given a target object to forward key events to.
 * It receives key events from the applet, and calls the target's method
 * key___Pressed or key___Released where ___ is the name of the key pressed.
 */
public class KeyInterpreter implements KeyListener {
    //  Map of code to name
    private static HashMap codeNames;
    //  Map of name to code
    private static HashMap keyCodes = buildKeyCodes();
    //  object to forward events to
    private Object target = null;
    // Keys currently held down
    private static HashSet keysDown = new HashSet();

    /**
     * Give key focus to the given object.
     * @param target New target of this KeyInterpreter.
     */
    public void setTarget(Object target) {
        this.target = target;
        Iterator iter = keysDown.iterator();
        while(iter.hasNext()) {
            doKey("Pressed", (Integer)iter.next());
        }
    }
    
    /**
     * Return the target of this KeyInterpreter
     * @return The target object of this KeyInterpreter.
     */
    public Object getTarget() {
        return target;
    }
    
    // The system will call this method when a key is pressed.
    private void keyEvent(KeyEvent event) {
        String type;
        Integer code = new Integer(event.getKeyCode());
                
        /*
         * Some systems will continue to produce key pressed events as long as the key
         * is down. We keep a list of what keys are down, and only call the "Down" method
         * when the key was really just pressed down. Otherwise, we call the "Held" method.
         * The "Held" method should probably not be implemented: you can't trust that all
         * systems will keep giving key pressed events.
         */
        if(event.getID() == KeyEvent.KEY_PRESSED) {
            if(keysDown.contains(code)) {
                type = "Held";
            } else {
                type = "Pressed";
                keysDown.add(code);
            }
        } else if(event.getID() == KeyEvent.KEY_RELEASED) {
            type = "Released";
            keysDown.remove(code);
        } else {
            return;
        }
        doKey(type, code);
    }
    
    // Send a key event to the current keyFocus object
    private void doKey(String type, Integer code) {
        if(target == null) {
            return;
        }

        String name = capitalizedNameForKeyCode(code);
        if(name == null) {
            return;
        }
        
        String methodName = "key" + name + type;
        
        try {
            Method method = target.getClass().getMethod(methodName, null);
            method.invoke(target, null);
        } catch(Exception e) {
            return;
        }
    }

    /*
     * In order to build our reference table, we parse the KeyEvent class
     * and extract the name and value of any field that begins with "VK_".
     */
    private static HashMap buildKeyCodes() {
        HashMap keyCodes = new HashMap();
        codeNames = new HashMap();
        try {
            Field[] fields = KeyEvent.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                if ((fields[i].getModifiers() & Modifier.STATIC) != 0
                    && fields[i].getName().startsWith("VK_")) {
                    String name =
                        fields[i].getName().substring(3).toLowerCase();
                    Integer value = (Integer) fields[i].get(null);
                    keyCodes.put(name, value);
                    codeNames.put(value, name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyCodes;
    }

    private static Integer codeForKeyName(String key) {
        return (Integer) keyCodes.get(key.toLowerCase());
    }

    private static String nameForKeyCode(Integer code) {
        return (String) codeNames.get(code);
    }

    private static String uppercaseNameForKeyCode(Integer code) {
        return ((String) codeNames.get(code)).toUpperCase();
    }

    private static String capitalizedNameForKeyCode(Integer code) {
        String s = (String) codeNames.get(code);
        return s.substring(0, 1).toUpperCase().concat(s.substring(1));
    }
    /* * * Implementation of KeyListener * * */

    public void keyTyped(KeyEvent event) {
        // We don't deal with this type of event, only press and release.
    }
    
    public void keyPressed(KeyEvent event) {
        keyEvent(event);
    }

    public void keyReleased(KeyEvent event) {
        keyEvent(event);
    }

}
