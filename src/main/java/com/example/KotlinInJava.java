// https://www.youtube.com/watch?v=A74TOX803D0 (freecodecamp)
/*
 Contents:
 - 1. Java keywords : https://www.w3schools.com/java/java_ref_keywords.asp
 - 2. Format specifiers
 - 3. Logger class
 - 4. Java annotations
 - 5. Operators
 - 6. Strings
 - 7. Inputs , if-else, switch
 - 8. Arrays
 - 9. For loop
 - 10. OOPS
 */

package main.java.com.example;

import com.google.common.annotations.Beta;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.DoNotMock;
import com.google.j2objc.annotations.AutoreleasePool;
import com.sun.java.accessibility.util.*;
import com.sun.jdi.*;
import com.sun.management.*;
import com.sun.net.httpserver.*;
import com.sun.nio.sctp.*;
import com.sun.security.auth.*;
import com.sun.security.jgss.*;
import com.sun.source.doctree.*;
import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.tools.attach.*;
import com.sun.tools.javac.*;
import com.sun.tools.jconsole.*;
import java.applet.*;
import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.*;
import java.lang.Deprecated;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.rmi.*;
import java.security.*;
import java.sql.*;
import java.sql.Date;
import java.text.*;
import java.time.*;
import java.time.Period;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.accessibility.*;
import javax.annotation.CheckForNull;
import javax.annotation.CheckForSigned;
import javax.annotation.CheckReturnValue;
import javax.annotation.meta.Exclusive;
import javax.annotation.meta.Exhaustive;
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

import jdk.jfr.Experimental;
import jdk.net.*;
import jdk.nio.*;
import jdk.jfr.*;
import jdk.dynalink.*;
import jdk.jshell.*;
import jdk.javadoc.doclet.*;
import jdk.management.jfr.*;
import jdk.security.jarsigner.*;
import kotlin.*;
import kotlin.js.ExperimentalJsExport;
import kotlin.time.ExperimentalTime;
import netscape.javascript.*;
import org.checkerframework.checker.builder.qual.CalledMethods;
import org.checkerframework.checker.calledmethods.qual.*;
import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKeyBottom;
import org.checkerframework.checker.fenum.qual.AwtAlphaCompositingRule;
import org.checkerframework.checker.fenum.qual.AwtColorSpace;
import org.checkerframework.checker.fenum.qual.AwtCursorType;
import org.checkerframework.checker.fenum.qual.AwtFlowLayout;
import org.checkerframework.checker.guieffect.qual.AlwaysSafe;
import org.checkerframework.checker.interning.qual.CompareToMethod;
import org.checkerframework.checker.interning.qual.EqualsMethod;
import org.checkerframework.checker.mustcall.qual.CreatesMustCallFor;
import org.checkerframework.checker.mustcall.qual.MustCall;
import org.checkerframework.checker.mustcall.qual.MustCallAlias;
import org.checkerframework.checker.mustcall.qual.MustCallUnknown;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.checkerframework.checker.optional.qual.EnsuresPresent;
import org.checkerframework.checker.optional.qual.EnsuresPresentIf;
import org.checkerframework.checker.signature.qual.*;
import org.checkerframework.checker.units.qual.*;
import org.checkerframework.common.returnsreceiver.qual.BottomThis;
import org.checkerframework.common.subtyping.qual.Bottom;
import org.checkerframework.common.value.qual.*;
import org.checkerframework.dataflow.qual.AssertMethod;
import org.checkerframework.framework.qual.*;
import org.ietf.jgss.*;
import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.TestOnly;
//import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.*;

import static java.lang.annotation.ElementType.CONSTRUCTOR;

@Covariant({})
public class KotlinInJava {
    public static final String SOURCE_CLASS = "SOURCE_CLASS";

    public static void main(String[] args) {

    System.out.println("/********************************* 2. Format specifiers **************************************************************/");
        String kotlinScript = """
        println("Hello, Kotlin embedded from Java!")
        """;

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("kts");

        try {
            engine.eval(kotlinScript);
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        System.out.println(add(2,300));
        System.out.print("""
        Inline print
        """);
        System.out.printf("Formatted print; ");
        System.out.printf("Formatted print %s; ","again");
        System.out.format("Formatted print format; ");

        System.out.println(System.out.getClass());

        int age =0; int _$num = 10;
        System.out.printf("%d , %d; " , age, _$num);

        byte byte_ = 100 ; //-128 to 127
        char char_ = 'a';
        String string_ = "Hi!!";
        System.out.println(string_);

        // format specifiers
        // %n -> newline
        System.out.printf(
                "===== Integer Types =====%n" +
                        "decimal: %d, octal: %o, hex lowercase: %x, hex uppercase: %X%n" +
                        "===== Floating Point =====%n" +
                        "float: %f, double: %f, scientific: %e, scientific uppercase: %E, g: %g, hex: %a%n" +
                        "===== Boolean =====%n" +
                        "boolean lowercase: %b, boolean uppercase: %B%n" +
                        "===== Character =====%n" +
                        "char lowercase: %c, char uppercase: %C%n" +
                        "===== String =====%n" +
                        "string lowercase: %s, string uppercase: %S%n" +
                        "===== Object =====%n" +
                        "hashcode lowercase: %h, hashcode uppercase: %H, toString: %s%n" +
                        "===== Percent =====%n" +
                        "literal percent: %% %n" +
                        "===== Flags & Width =====%n" +
                        "left justify: %-10dEND, pad zeros: %010d, always sign: %+d, grouping comma: %,d, negative parentheses: %(d%n" +
                        "===== Width & Precision =====%n" +
                        "width 10: %10sEND, precision 3: %.3f, width+precision: %10.2f%n",

                123,          // %d
                123,          // %o
                123,          // %x
                123,          // %X
                3.14159f,     // %f (float)
                2.71828,      // %f (double)
                2.71828,      // %e
                2.71828,      // %E
                2.71828,      // %g
                2.71828,      // %a
                true,         // %b
                false,        // %B
                'A',          // %c
                'Z',          // %C
                "hello",      // %s
                "world",      // %S
                new Object(), // %h
                new Object(), // %H
                new Object(), // %s
                123,          // %-10d
                123,          // %010d
                123,          // %+d
                12345,        // %,d
                -123,         // %(d
                "Hi",         // %10s
                2.71828,      // %.3f
                2.71828       // %10.2f
        );
        String a = "5";
        System.out.println((double)(Integer.parseInt(a)));

        class nested{
            @SafeVarargs
            final public <T> void main(T... args){
                System.out.println("Nested -> main method!");
            }
        }
        nested Nested = new nested();
        Nested.main();

        /******************************* 3. Logger class ****************************************************************/

        /*
        Quick Reference: Levels in order (highest → lowest)
SEVERE
WARNING
INFO
CONFIG
FINE
FINER
FINEST
ALL (everything)
OFF (nothing)
         */
        Logger logger = Logger.getLogger("");
        Handler[] handlers = logger.getHandlers();
        for (Handler h : handlers) {
            h.setLevel(Level.ALL);
        }
        logger.setLevel(Level.ALL);
        logger.info("logger.info");
        logger.config("logger.config");
        logger.fine("logger.fine");
        logger.finer("logger.finer");
        logger.finest("logger.finest");
        logger.entering(SOURCE_CLASS,SOURCE_CLASS);
        logger.warning("logger.warning");
        logger.severe("logger.severe");

        System.out.println("/*********************************************  5. Operators **************************************************/");
        operators();
    }


    /*********************************************** 4. Java annotations ************************************************/

    @Deprecated @kotlin.Deprecated(message = "test")
//   @Override (Method does not override method from its superclass)
    @Serial @SuppressWarnings({})
    @MustCall @MustCallAlias @MustCallUnknown @TestOnly
    @Suppress(names = {}) @Transient @DescriptorKey(SOURCE_CLASS)
    @Generated({}) @DataAmount
    @AssertMethod @AssertNonNullIfNonNull({}) @AutoreleasePool
    @JdkConstants.AdjustableOrientation @AllowConcurrentEvents @AnnotatedFor({})
    // ('void' type may not be annotated)
    @A @Acceleration @Angle @Area @AlwaysSafe @ArrayLen({}) @ArrayWithoutPackage
    @AwtAlphaCompositingRule @ArrayLenRange @AwtColorSpace @AwtCursorType @AwtFlowLayout
    @BooleanFlag @BeanProperty @BinaryName @BinaryNameOrPrimitiveType @BinaryNameWithoutPackage
    @JdkConstants.BoxLayoutAxis @BoolVal({}) @BottomVal @Bottom @Beta @BuilderInference @BottomThis
    @C @Current @CanonicalName @CanonicalNameOrEmpty @CanonicalNameAndBinaryName @CanonicalNameOrPrimitiveType
    @ClassGetName @ClassGetSimpleName @JdkConstants.CalendarMonth @JdkConstants.CursorType @CFComment({})
    @CheckForNull @CheckForSigned @CheckReturnValue @CalledMethods({}) @org.checkerframework.checker.calledmethods.qual.CalledMethods
    @CompareToMethod @Contract @CompilerMessageKeyBottom @CanIgnoreReturnValue @CalledMethodsPredicate(SOURCE_CLASS) @CalledMethodsBottom @CreatesMustCallFor
    @com.google.errorprone.annotations.CheckReturnValue @CreatesMustCallFor @DotSeparatedIdentifiers @DoNotCall
    @DoubleVal({}) @DoesNotMatchRegex({}) @DotSeparatedIdentifiersOrPrimitiveType @DeprecatedSinceKotlin @EnsuresMinLenIf(result = false, expression = {})
    @EnsuresQualifier(expression = {}, qualifier = ConstructorProperties.class) @EnsuresMinLenIf(result = false, expression = {}) @EnumVal({}) @EnsuresQualifierIf(result = false, expression = {}, qualifier = ConstructorProperties.class)
    @EnsuresCalledMethods(value = {}, methods = {}) @EnsuresPresentIf(result = false, expression = {}) @EnsuresPresent({}) @EnsuresCalledMethodsOnException(value = {}, methods = {}) @EnsuresCalledMethodsVarArgs({}) @EnsuresCalledMethodsIf(result = false, expression = {}, methods = {})
    @ExperimentalMultiplatform @ExperimentalStdlibApi @ExperimentalUnsignedTypes @ExperimentalJsExport @Exclusive @Exhaustive
    @ExperimentalTime @EqualsMethod @Frequency @ContentType({})
    public static int add(int a, int b){
        return a+b;
    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(CONSTRUCTOR)
    @DoNotMock
    @Experimental
    @DefaultQualifierForUse
    @DefaultQualifierInHierarchy
    @InheritedAnnotation
    @DslMarker
    @ExperimentalStdlibApi
    @ExperimentalMultiplatform
    @ExperimentalTime
    public @interface ConstructorProperties {
        String[] value();
    }

    public @interface ContentType {
        String[] value();
    }

    public static void operators() {
        int x = 5;

        // unary operators
        System.out.println(+x);   // Unary plus      -> 5
        System.out.println(-x);   // Unary minus     -> -5
        System.out.println(++x);  // Pre-increment   -> 6 (x becomes 6 before use)
        System.out.println(x++);  // Post-increment  -> prints 6, then x becomes 7
        System.out.println(--x);  // Pre-decrement   -> 6 (x decremented before use)
        System.out.println(x--);  // Post-decrement  -> prints 6, then x becomes 5
        System.out.println(!true); // Logical NOT -> false
        System.out.println(~x); // inverts all bits
        System.out.println((double)x); // typecasting

        // binary operators

         // - arithmetic
        System.out.println(10 + 5);   // Addition
        System.out.println(10 - 5);   // Subtraction
        System.out.println(10 * 5);   // Multiplication
        System.out.println(10 / 5);   // Division
        System.out.println(10 % 3);   // Modulus (remainder)
        // - relational
        System.out.println(10 > 5);   // true
        System.out.println(10 < 5);   // false
        System.out.println(10 >= 10); // true
        System.out.println(10 == 10); // true
        System.out.println(10 != 5);  // true
        // - logical
        System.out.println(true && false); // false (AND)
        System.out.println(true || false); // true  (OR)
        // - bitwise
        int a = 5, b = 3;
        System.out.println(a & b);   // AND
        System.out.println(a | b);   // OR
        System.out.println(a ^ b);   // XOR
        System.out.println(a << 1);  // Left shift
        System.out.println(a >> 1);  // Right shift
        // - assignment
        int y = 10;
        y += 5; // same as y = y + 5
        y -= 3;
        y *= 2;
        y /= 4;
        y %= 2;
        System.out.println(y);

        // ternary operators
        int age = 20;
        String result = (age >= 18) ? "Adult" : "Minor";
        System.out.println(result); // Adult


        System.out.println("/*********************************************  6. Strings **************************************************/");

        String string_ = "hihihihihihihihi";
        String pattern = """
                {0}
                {1}
                {2}
                {3}
                {4}
                {5}
                {6}
                {7}
                {8}
                {9}
                {10}
                {11}
                {12}
                {13}
                {14}
                {15}
                {16}
                """;
        System.out.println(MessageFormat.format(pattern,
         string_.getBytes(StandardCharsets.UTF_8),
                string_.toLowerCase()
                ,string_.toUpperCase()
                , string_.charAt(2)
                , string_.codePointAt(2)
                , string_.codePointCount(0,3)
                , string_.compareTo("qwerty")
                , string_.trim()
                , string_.toCharArray()
                , string_.indexOf("h")
                , (new StringBuilder(string_)).reverse().toString()
                , (new StringBuilder(string_)).toString()
                , (new StringBuilder(string_)).append(3).toString()
                , (new StringBuilder(string_)).length()
                , (new StringBuilder(string_)).insert(4,"123")
                , (new StringBuilder(string_)).length()
                , (new StringBuilder(string_)).substring(4,6)

        ));

        String string1 = "abc", string2 = "abc", String1 = new String("abc"), String2 = new String("abc");
        System.out.println(string1 == string2);
        System.out.println(String1 == String2);
        System.out.println(string1.equals(string2));
        System.out.println(String1.equals(String2));
        System.out.println(string1.hashCode() == string2.hashCode());
        System.out.println(String1.hashCode() == String2.hashCode());


        System.out.println("/*********************************************  7. Inputs , if-else, switch **************************************************/");


        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        System.out.println(num + 33);

        System.out.println(2>3?"Correct":"No");

        System.out.println(switch(3){
            case 1,2,3,4,5 -> "Weekday";
            case 6,7 -> "Weekend";
            default -> throw new IllegalStateException("Unexpected value: " + 3);
        });

        num = 0;
        switch(num){
            case 0:
                System.out.println("Nope0");
                System.out.println("Nope0");
                break;
            case 1:
                System.out.println("Nope1");
                System.out.println("Nope1");
                break;
            case 2:
                System.out.println("Nope2");
                System.out.println("Nope2");
                break;
        }
        switch(num){
            default:
                System.out.println("0 Default");
                break;
            case 1:
                System.out.println("Nope1");
                System.out.println("Nope1");
                break;
            case 2:
                System.out.println("Nope2");
                System.out.println("Nope2");
                break;
        }

        System.out.println("/*********************************************  8. Arrays **************************************************/");

        int[] arr = new int[4]; // [0,0,0,0]
        System.out.println(Arrays.toString(arr).getClass());
        System.out.println(Arrays.stream(arr).boxed().collect(Collectors.toList()).getClass());

        for(int i=0;i<arr.length;i++)arr[i] = (i+1);
        System.out.println(Arrays.toString(arr));

        Arrays.sort(arr);
        Arrays.sort(arr,0,2);
        System.out.println(
                Arrays.toString(Arrays.stream(arr).boxed().collect(Collectors.toList()).reversed()
                        .toArray())
        );
        Arrays.sort(arr);
        System.out.println(Arrays.binarySearch(arr,3));
        System.out.println(Arrays.binarySearch(arr,9));

        System.out.println(Arrays.toString(Arrays.copyOf(arr, arr.length)));
        System.out.println(Arrays.toString(Arrays.copyOfRange(arr, 0,4)));

        System.out.println("/*********************************************  9. For loop **************************************************/");

        for(var i:arr) System.out.println(i);
        for(var i=0;i<arr.length;i++)System.out.println(arr[i]);

        var arrayList = new ArrayList<>(Arrays.stream(arr).boxed().collect(Collectors.toList()));
        arrayList.add(-1); arrayList.remove(2);
        arrayList.set(0,300);
        arrayList.addFirst(4); arrayList.addAll(Arrays.asList(9,0,9,8,7));
        arrayList.addLast(5);
        arrayList.stream().max(Integer::compare);
        System.out.println(Arrays.toString(arrayList.toArray()));

        String formatted = """
                {0}
                {1}
                {2}
                """;

        System.out.println(MessageFormat.format(formatted,
                Arrays.toString(arrayList.stream().skip(3).toArray()),
                Arrays.toString(arrayList.stream().skip(30).toArray()),
               Arrays.toString(arrayList.stream().skip(300).toArray())
        ));

        System.out.println("/*********************************************  10. OOPS **************************************************/");

        /************************ no OOPS concepts involved  ************************/
        class User{
            public String name;
            public LocalDate date;
            public LocalDate birthdate;

            public User(String name , LocalDate birthdate){
                this.birthdate = birthdate;
                this.name = name;
            }

            public User() {}
        }

        User user = new User();
        user.name = "Sreedhar";
        user.birthdate = LocalDate.parse("1999-09-15");
        user.date = LocalDate.now();
        System.out.println(user.name + user.date);
        System.out.println("Age:" + Period.between(user.date , user.birthdate) + Period.between(user.date , user.birthdate).toString().split("-")[1]);

        /************************ Encapsulation ************************/
        class User_Encapsulation {
            private String name;
            private LocalDate birthdate;
            private LocalDate registeredDate;

            // Constructor
            public User_Encapsulation(String name, LocalDate birthdate) {
                this.name = name;
                this.birthdate = birthdate;
                this.registeredDate = LocalDate.now();
            }

            // Getters and Setters
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }

            public LocalDate getBirthdate() { return birthdate; }
            public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

            public LocalDate getRegisteredDate() { return registeredDate; }

            // Behavior: Calculate Age
            public int getAge() {
                return Period.between(birthdate, LocalDate.now()).getYears();
            }
            // Behavior: Display Info
            public String getUserInfo() {
                return "Encapsulation -> User: " + name + ", Age: " + getAge() + ", Registered: " + registeredDate;
            }
        }

        User_Encapsulation user_encapsulation = new User_Encapsulation("Sreedhar",LocalDate.parse("1999-09-15") );
        System.out.println(user_encapsulation.getUserInfo());

        /************************ Abstraction ************************/

        // Abstract class -> defines "what"
        abstract class User_abstraction {
            private String name;
            private LocalDate birthdate;

            public User_abstraction(String name, LocalDate birthdate) {
                this.name = name;
                this.birthdate = birthdate;
            }

            public String getName() { return name; }
            public LocalDate getBirthdate() { return birthdate; }

            // Abstract method -> subclasses must provide implementation
            public abstract int getAge();
            public abstract String getUserInfo();
        }

        // Concrete class -> defines "how"
        class RegisteredUser extends User_abstraction {
            private LocalDate registeredDate;

            public RegisteredUser(String name, LocalDate birthdate) {
                super(name, birthdate);
                this.registeredDate = LocalDate.now();
            }

            @Override
            public int getAge() {
                return Period.between(getBirthdate(), LocalDate.now()).getYears();
            }

            @Override
            public String getUserInfo() {
                return "Abstraction -> User: " + getName() +
                        ", Age: " + getAge() +
                        ", Registered: " + registeredDate;
            }
        }

        User_abstraction user_abstraction = new RegisteredUser("Sreedhar", LocalDate.parse("1999-09-15"));
        System.out.println(user_abstraction.getUserInfo());


        /************************ Inheritance ************************/

        // Parent class (base class)
        class User_inheritance {
            protected String name;
            protected LocalDate birthdate;

            public User_inheritance(String name, LocalDate birthdate) {
                this.name = name;
                this.birthdate = birthdate;
            }

            public String getName() { return name; }
            public LocalDate getBirthdate() { return birthdate; }

            public int getAge() {
                return Period.between(birthdate, LocalDate.now()).getYears();
            }
        }

        // Child class (subclass extends parent class)
        class RegisteredUser_inheritance extends User_inheritance {
            private LocalDate registeredDate;

            public RegisteredUser_inheritance(String name, LocalDate birthdate) {
                super(name, birthdate); // reuse parent constructor
                this.registeredDate = LocalDate.now();
            }

            // Add new behavior
            public String getUserInfo() {
                return "Inheritance -> User: " + getName() +
                        ", Age: " + getAge() +
                        ", Registered: " + registeredDate;
            }
        }

        RegisteredUser_inheritance user_inheritance = new RegisteredUser_inheritance("Sreedhar", LocalDate.parse("1999-09-15"));
        System.out.println(user_inheritance.getUserInfo());


    }
}