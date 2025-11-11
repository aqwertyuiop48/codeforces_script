package org.example

//import org.junit.Test;

import com.google.common.annotations.Beta
import com.google.common.eventbus.AllowConcurrentEvents
import com.google.errorprone.annotations.CanIgnoreReturnValue
import com.google.errorprone.annotations.DoNotCall
import com.google.errorprone.annotations.DoNotMock
import com.google.j2objc.annotations.AutoreleasePool
import jdk.jfr.BooleanFlag
import jdk.jfr.DataAmount
import jdk.jfr.Experimental
import jdk.jfr.Frequency
import org.checkerframework.checker.builder.qual.CalledMethods
import org.checkerframework.checker.calledmethods.qual.*
import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKeyBottom
import org.checkerframework.checker.fenum.qual.AwtAlphaCompositingRule
import org.checkerframework.checker.fenum.qual.AwtColorSpace
import org.checkerframework.checker.fenum.qual.AwtCursorType
import org.checkerframework.checker.fenum.qual.AwtFlowLayout
import org.checkerframework.checker.guieffect.qual.AlwaysSafe
import org.checkerframework.checker.interning.qual.CompareToMethod
import org.checkerframework.checker.interning.qual.EqualsMethod
import org.checkerframework.checker.mustcall.qual.CreatesMustCallFor
import org.checkerframework.checker.mustcall.qual.MustCall
import org.checkerframework.checker.mustcall.qual.MustCallAlias
import org.checkerframework.checker.mustcall.qual.MustCallUnknown
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull
import org.checkerframework.checker.optional.qual.EnsuresPresent
import org.checkerframework.checker.optional.qual.EnsuresPresentIf
import org.checkerframework.checker.signature.qual.*
import org.checkerframework.checker.units.qual.*
import org.checkerframework.common.returnsreceiver.qual.BottomThis
import org.checkerframework.common.subtyping.qual.Bottom
import org.checkerframework.common.value.qual.*
import org.checkerframework.dataflow.qual.AssertMethod
import org.checkerframework.framework.qual.*
import org.intellij.lang.annotations.JdkConstants.*
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.TestOnly
import java.beans.BeanProperty
import java.beans.ConstructorProperties
import java.beans.Transient
import java.io.Serial
import javax.annotation.CheckForSigned
import javax.annotation.CheckReturnValue
import javax.annotation.meta.Exclusive
import javax.annotation.meta.Exhaustive
import javax.annotation.processing.Generated
import javax.management.DescriptorKey
import kotlin.experimental.ExperimentalTypeInference
import kotlin.js.ExperimentalJsExport
import kotlin.time.ExperimentalTime


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")

    for (i in 1..5) {
        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
        println("i = $i")
    }
    main1(1,2)
    println(add(2,300))


    println("Destructuring in Kotlin");
    /****************************************************************************/

    var (a, b, c, d, e) = Triple(Triple(10, 2, 3), 4, 5).let {
        listOf(it.first.first, it.first.second, it.first.third, it.second, it.third)
    }
    println(a+4)

    data class UserData(
        var firstName: String,
        var lastName: String,
        var age: Int,
        var city: String,
        var country: String,
        var email: String,
        var phone: String
    )

    var (firstName, lastName, age, city, country, email, phone) = listOf("John", "Doe", 25, "NYC").let {
        UserData(it[0] as String, (it[1] as String) + (it[0] as String), it[2] as Int, it[3] as String, "USA", "john@example.com", "123-456")
    }
    println("firstName: $firstName, lastName: $lastName, age: $age, city: $city, country: $country, email: $email, phone: $phone")
    println(email)  // john@example.com

    // Reassign values
    firstName = "Jane"
    age = 30
    email = "jane@example.com"

    println(email)  // jane@example.com

    /****************************************************************************/

    var list: List<Any> = listOf(1, "hello", true, 3.14)
    println(list)

    var (a_, b_, c_, d_) = listOf(1, "hello", true, 3.14).let { listOf(it[0] as Int, it[1] as String, it[2] as Boolean, it[3] as Double) }
    println(a_)

    /****************************************************************************/
    var q = run {
        data class Q(var a:Int,var b:String,var c:Boolean,var d:Double)
        Q(1,"hello",true,3.14)
    }

    // mutable properties
    q.a += 5
    q.b = "world"
    println(q.a) // 6
    println(q.b) // world

    /****************************************************************************/
}


/** */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CONSTRUCTOR)
@DoNotMock
@Experimental
@DefaultQualifierForUse
@DefaultQualifierInHierarchy
@InheritedAnnotation
@DslMarker
@ExperimentalStdlibApi
@ExperimentalMultiplatform
@ExperimentalTime
annotation class ConstructorProperties(vararg val value: String)
annotation class ContentType(vararg val value: String)

@java.lang.SafeVarargs
fun <T> main1(vararg args: T?) {
    kotlin.io.println("Nested -> main method!")
}

@OptIn(
    ExperimentalMultiplatform::class,
    ExperimentalMultiplatform::class,
    ExperimentalStdlibApi::class,
    ExperimentalUnsignedTypes::class,
    ExperimentalJsExport::class,
    ExperimentalTime::class,
    ExperimentalTypeInference::class
)
@Deprecated("")
//@Deprecated(message = "test") //   @Override (Method does not override method from its superclass)
@Serial
@TestOnly
@Suppress(names = [])
@Transient
@DescriptorKey("")
@Generated
@DataAmount
@AssertMethod
@AssertNonNullIfNonNull
@AutoreleasePool
@AdjustableOrientation
@AllowConcurrentEvents
@AnnotatedFor // ('void' type may not be annotated)
@BooleanFlag
@BeanProperty
@BoxLayoutAxis
@Beta
@BuilderInference
@CalendarMonth
@CursorType
@CFComment
@CheckForSigned
@CheckReturnValue
@CompareToMethod
@Contract
@CanIgnoreReturnValue
@CreatesMustCallFor
@com.google.errorprone.annotations.CheckReturnValue
@CreatesMustCallFor
@DoNotCall
@EnsuresMinLenIf(result = false, expression = [])
@EnsuresQualifier(expression = [], qualifier = ConstructorProperties::class)
@EnsuresMinLenIf(result = false, expression = [])
@EnsuresQualifierIf(result = false, expression = [], qualifier = ConstructorProperties::class)
@EnsuresCalledMethods(value = [], methods = [])
@EnsuresPresentIf(result = false, expression = [])
@EnsuresPresent
@EnsuresCalledMethodsOnException(value = [], methods = [])
@EnsuresCalledMethodsVarArgs
@EnsuresCalledMethodsIf(result = false, expression = [], methods = [])
@Exclusive
@Exhaustive
@EqualsMethod
@Frequency
fun add(
    a: Int,
    b: Int
): @MustCall @MustCallAlias @MustCallUnknown @A @Acceleration @Angle @Area @AlwaysSafe @ArrayLen @ArrayWithoutPackage @AwtAlphaCompositingRule @ArrayLenRange @AwtColorSpace @AwtCursorType @AwtFlowLayout @BinaryName @BinaryNameOrPrimitiveType @BinaryNameWithoutPackage @BoolVal @BottomVal @Bottom @BottomThis @C @Current @CanonicalName @CanonicalNameOrEmpty @CanonicalNameAndBinaryName @CanonicalNameOrPrimitiveType @ClassGetName @ClassGetSimpleName @CalledMethods @org.checkerframework.checker.calledmethods.qual.CalledMethods @CompilerMessageKeyBottom @CalledMethodsPredicate(
    ""
) @CalledMethodsBottom @DotSeparatedIdentifiers @DoubleVal @DoesNotMatchRegex @DotSeparatedIdentifiersOrPrimitiveType @EnumVal Int {
    return a + b
}