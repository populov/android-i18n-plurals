This is a clone of [android-i18n-plurals](https://code.google.com/p/android-i18n-plurals/) project, adopted for Gradle build and Maven artifact publishing.

android-i18n-plurals
====================
Android has great built in support for plural forms that represent different quantities. It is well documented in [this section](http://developer.android.com/guide/topics/resources/string-resource.html#Plurals) of API reference.

Unfortunately, it seems that on early Android versions (API Level 10 and below) the plurals support does not cover all languages.

For example if you specify the following block in your strings.xml file for Russian language

```xml
<plurals name="parrot_count">
    <item quantity="one">%d попугай</item>
    <item quantity="few">%d попугая</item>
    <item quantity="many">%d попугаев</item>
    <item quantity="other">%d попугаев</item>
</plurals>
```
it would produce the following results for quantities from 0 to 6:

```
0 попугаев
1 попугай
2 попугаев
3 попугаев
4 попугаев
5 попугаев
6 попугаев
```
As you see, quantities of 2, 3, and 4 are handled incorrectly.

This project provides proper pluralization for all languages.

Use PluralResources class that implements getQuantityString() method. The syntax of this getQuantityString() is the same as APIs Resources [getQuantityString()](http://developer.android.com/reference/android/content/res/Resources.html#getQuantityString%28int,%20int%29), so it is easy to replace in existing projects.

For the example above, the output would be the following:

0 попугаев
1 попугай
2 попугая
3 попугая
4 попугая
5 попугаев
6 попугаев
One of the possible use models is to declare static PluralResources field, and initialize it in Applications onCreate():

```java
public class MyApplication extends Application
{
    public static PluralResources pluralResources;

    @Override
    public void onCreate()
    {
        super.onCreate();

        try {
            pluralResources = new PluralResources( getResources() );
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
```

to use it just call

    MyApplication.pluralResources.getQuantityString(R.plurals.parrot_count, n, n );
(if you use this method do not forget to add MyApplication to your AndroidManifest.xml)

--------
Keywords "zero", "one", "two", "few", "many", and "other" you specify in strings.xml are different plural form types, not the literal values, sometimes has little to do with actual quantities. The meanings of these keywords is different for different languages, see this table for all rules.

--------
In addition to proper pluralization the PluralResources class provides an option for special handling of the 0 quantity. If you specify the case for "zero" in your resource, this would be used for the 0 quantity unconditionally, regardless of whether or not the language you are writing the resource for has special treatment for "zero". That is, for instance, if you have

    <item quantity="zero">No parrots</item>
for English, then this would be used, despite the fact that English does not have special plural form for zero.

--------
Note that Android API 11 and higher has support for all languages, so if your minSdkVersion is 11 then you do not need this.