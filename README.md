Java online compiler: https://onecompiler.com/java/43q5dssdr

Interview questions:
- Streams definition and example
- Arrays vs list differences in java?
- All Collections in Java
- What is method overloading vs. overriding?
- Static vs non static methods
- Mutable vs immutable objects (final keyword)
- Serialization vs deserialization of objects / Transient field




Answers
<pre>
A static method belongs to the class itself, rather than to any specific object (instance) of the class.
It is declared using the static keyword.
Key properties of static methods
✅ Belong to the class, not objects
✅ Cannot access instance (non-static) variables or methods directly
✅ Often used for:

Utility/helper methods

Factory methods

main method in Java

Common static methods in Java:
Math.max()

Collections.sort()

Integer.parseInt()
</pre>

<hr>

<pre>
Functional Interface:
Exactly one Abstract method
Declared without a body.

Must be implemented by concrete (non-abstract) subclasses or implementing classes.

Used in:

Abstract classes

Interfaces (implicitly public and abstract)
</pre>

<hr>
<pre>
  An immutable class is a class whose instances cannot be changed once created.
All fields are final and set only once via constructor. No setters.
</pre>

<pre>
Serialization is the process of converting an object into a sequence of bytes so that:

The object’s state can be saved to a file,

Sent over a network, or

Stored in a database.

The serialized byte stream can later be deserialized back into a copy of the original object.
  transient field will NOT be serialized.
</pre>


# Ways of executing Java

## Inline
- JBang : https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L51
- Maven executable bash : https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L64
- Maven executable java : https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L78
- JShell : https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main1.yml#L43
- IJava : https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L99
- Java linux / mac : https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L86

## From file
- Shebang: https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L52
- IJava : https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L121
- Direct java : https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L84
- javac and java : https://github.com/aqwertyuiop48/programming_languages/blob/main/.github/workflows/java_mysql.yml#L98
- gradle : https://github.com/aqwertyuiop48/automated-build-android-app-with-github-action/blob/main/.github/workflows/android-ci-generate-apk-aab-download.yml#L46
- maven : https://github.com/aqwertyuiop48/programming_languages/blob/main/.github/workflows/spring_boot_API_maven_java_applications.yml#L48
- JBang: https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L54