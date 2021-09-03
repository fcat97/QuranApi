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

## How to Use?

You can use as you wish. Here is a sample usage shown

```kotlin
val api = QuranApi(this)
val recyclerView = findViewById<RecyclerView>(R.id.textView)
val adapter = Adapter()
recyclerView.adapter = adapter

api.getSurah(2) {
    runOnUiThread {
        adapter.submitList(it.verses)
    }
}
```

More Advance uses is implemented in App.. Explore it...

>

More features are comming InshaAllah...

Feel free to fork and update. Just give me a PullRequest...
