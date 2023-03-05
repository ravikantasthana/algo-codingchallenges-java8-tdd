<h3>Difference between <? super T> and <? extends T> in Java</h3>

- <b><? super T></b> denotes an unknown type that is supertype of T (or T itself)
- <b><? extends T></b> denotes an unknown type that is subtype of T (or T itself)


<h2>extends</h2>


The wildcard declaration of List<? extends Number> foo3 means that any of these are legal assignments:

```
List<? extends Number> foo3 = new ArrayList<Number>();  // Number "extends" Number (in this context)
List<? extends Number> foo3 = new ArrayList<Integer>(); // Integer extends Number
List<? extends Number> foo3 = new ArrayList<Double>();  // Double extends Number
```

<h3>Reading</h3> 
Given the above possible assignments, what type of object are you guaranteed to read from List foo3:

<li>You can read a Number because any of the lists that could be assigned to foo3 contain a Number or a subclass of Number.
<li>You can't read an Integer because foo3 could be pointing at a List&lt;Double&gt;
<li>You can't read a Double because foo3 could be pointing at a List&lt;Integer&gt;

<h3>Writing</h3> 
Given the above possible assignments, what type of object could you add to List foo3 that would be legal for all the above possible ArrayList assignments:

<li>You can't add an Integer because foo3 could be pointing at a List&lt;Double&gt;
<li>You can't add a Double because foo3 could be pointing at a List&lt;Integer&gt;
<li>You can't add a Number because foo3 could be pointing at a List&lt;Integer&gt;
<li>You can't add any object to List<? extends T> because you can't guarantee what kind of List it is really pointing to, so you can't guarantee that the object is allowed in that List. The only "guarantee" is that you can only read from it and you'll get a T or subclass of T.

<h2>super</h2>

Now consider List <? super T>.

The wildcard declaration of List<? super Integer> foo3 means that any of these are legal assignments:

```
List<? super Integer> foo3 = new ArrayList<Integer>();  // Integer is a "superclass" of Integer (in this context)
List<? super Integer> foo3 = new ArrayList<Number>();   // Number is a superclass of Integer
List<? super Integer> foo3 = new ArrayList<Object>();   // Object is a superclass of Integer
```

<h3>Reading</h3> 
Given the above possible assignments, what type of object are you guaranteed to receive when you read from List foo3:

<li> You aren't guaranteed an Integer because foo3 could be pointing at a List&lt;Number&gt; or List&lt;Object&gt;
<li> You aren't guaranteed a Number because foo3 could be pointing at a List&lt;Object&gt;

The only guarantee is that you will get an instance of an Object or subclass of Object (but you don't know what subclass).

<h3>Writing</h3>
Given the above possible assignments, what type of object could you add to List foo3 that would be legal for all the above possible ArrayList assignments:

<li> You can add an Integer because an Integer is allowed in any of above lists.
<li> You can add an instance of a subclass of Integer because an instance of a subclass of Integer is allowed in any of the above lists.
<li> You can't add a Double because foo3 could be pointing at an ArrayList&lt;Integer&gt;
<li> You can't add a Number because foo3 could be pointing at an ArrayList&lt;Integer&gt;
<li> You can't add an Object because foo3 could be pointing at an ArrayList&lt;Integer&gt;

<h2>Remember PECS: "Producer Extends, Consumer Super"</h2>

<b>"Producer Extends"</b>

If you need a List to produce T values (you want to read Ts from the list), you need to declare it with ? extends T, e.g. List<? extends Integer>. 
<u>But you cannot add to this list.</u>

<b>"Consumer Super"</b>

If you need a List to consume T values (you want to write Ts into the list), you need to declare it with ? super T, e.g. List<? super Integer>. 

<u>But there are no guarantees what type of object you may read from this list.</u>

If you need to both read from and write to a list, you need to declare it exactly with no wildcards, e.g. List<Integer>.

<b>Example</b>

Note this example from the Java Generics FAQ. Note how the source list src (the producing list) uses extends, and the destination list dest (the consuming list) uses super:

```
public class Collections {
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (int i = 0; i < src.size(); i++)
        dest.set(i, src.get(i));
    }
}
```