import java.applet.*;
import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.net.*;
import java.nio.*;
import java.rmi.*;
import java.security.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

import javax.accessibility.*;
import javax.annotation.processing.*;
import javax.crypto.*;
import javax.imageio.*;
import javax.lang.model.*;
import javax.management.*;
import javax.net.*;
import javax.naming.*;
import javax.print.*;
import javax.script.*;
import javax.security.auth.*;
import javax.security.cert.*;
import javax.security.sasl.*;
import javax.smartcardio.*;
import javax.sound.midi.*;
import javax.sound.sampled.*;
import javax.sql.*;
import javax.swing.*;
import javax.tools.*;
import javax.transaction.xa.*;
import javax.xml.*;

import jdk.net.*;
import jdk.nio.*;
import jdk.jfr.*;
import jdk.dynalink.*;
import jdk.jshell.*;
import jdk.javadoc.doclet.*;
import jdk.management.jfr.*;
import jdk.security.jarsigner.*;

import netscape.javascript.*;
import org.ietf.jgss.*;
import org.w3c.dom.*;
import org.xml.sax.*;


public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");
        for (int i = 1; i <= 5; i++) {
            System.out.println("i = " + i);
        }

        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        int n = sc.nextInt();
        int[] arr = new int[n];
        for(int i=0;i<n;i++)arr[i] = sc.nextInt();
        System.out.println(t);
        System.out.println(n);
        System.out.println(
                Arrays.toString(arr)
        );

        var list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        System.out.println(list);
    }
}