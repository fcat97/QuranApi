![Quran Api](https://github.com/fCat97/QuranApi/blob/main/images/cover.png?raw=true)


# QuranApi [![](https://jitpack.io/v/fcat97/QuranApi.svg)](https://jitpack.io/#fcat97/QuranApi)


A simple Api to implement Quran in Android.

## Add To Project

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency. `version:` [![](https://jitpack.io/v/fcat97/QuranApi.svg)](https://jitpack.io/#fcat97/QuranApi)

```gradle
dependencies {
    implementation 'com.github.fcat97:QuranApi:version'
}
```

If the only need is to add tajeeed colors, use the [TajweedApi](https://github.com/fcat97/TajweedApi): `version:` [![](https://jitpack.io/v/fcat97/TajweedApi.svg)](https://jitpack.io/#fcat97/TajweedApi)

```gradle
dependencies {
    implementation 'com.github.fcat97:tajweedApi:version' // core parser
    implementation 'com.github.fcat97:tajweedApi-android:version' // for android target
}
```

## How to Use?

You can use as you wish. Here is a sample usage shown

```kotlin
val recyclerView = findViewById<RecyclerView>(R.id.textView)
val adapter = Adapter()
recyclerView.adapter = adapter

val api = QuranApi(this)
api.getSurahAsync(2) { adapter.submitList(it.verses) }
```

For details see the [How To](wiki/howTo.md) page.
A fully functional app source is included in this repo.

>

Feel free to fork and update. Just give me a PR...
