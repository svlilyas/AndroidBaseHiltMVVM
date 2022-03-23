import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    object Kotlin {
        const val kotlinStdLib =
            "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Kotlin.kotlinStdLib}"
        const val kotlinCoroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.kotlinCoroutinesCore}"
        const val kotlinCoroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.kotlinCoroutinesCore}"

        object StdLib {
            const val common =
                "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.Kotlin.kotlinVersion}"
            const val jdk =
                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Kotlin.kotlinVersion}"
        }

        object Test {
            const val common =
                "org.jetbrains.kotlin:kotlin-test-common:${Versions.Kotlin.kotlinVersion}"
            const val annotations =
                "org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.Kotlin.kotlinVersion}"
            const val junit =
                "org.jetbrains.kotlin:kotlin-test-junit:${Versions.Kotlin.kotlinVersion}"
        }
    }

    object Android {
        const val androidCore =
            "androidx.core:core-ktx:${Versions.Android.androidCore}"
        const val appCompat =
            "androidx.appcompat:appcompat:${Versions.Android.appCompat}"
        const val legacySupport =
            "androidx.legacy:legacy-support-v4:${Versions.Android.legacySupport}"
        const val multidex =
            "androidx.multidex:multidex:${Versions.Android.multiDex}"
        const val materialDesign =
            "com.google.android.material:material:${Versions.Android.materialDesign}"
        const val fragment =
            "androidx.fragment:fragment-ktx:${Versions.Android.fragmentVersion}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.Android.constraintLayout}"
        const val recyclerView =
            "androidx.recyclerview:recyclerview:${Versions.Android.recyclerView}"
        const val recyclerViewSelection =
            "androidx.recyclerview:recyclerview:${Versions.Android.recyclerViewSelection}"
        const val cardView =
            "androidx.cardview:cardview:${Versions.Android.cardView}"
        const val palette =
            "androidx.palette:palette-ktx:${Versions.Android.palette}"
        const val workManger =
            "androidx.work:work-runtime-ktx:${Versions.Android.workManager}"
    }

    object Navigation {
        const val runTimeNavigation =
            "androidx.navigation:navigation-runtime-ktx:${Versions.Navigation.runTimeNavigation}"
        const val navigationFragment =
            "androidx.navigation:navigation-fragment-ktx:${Versions.Navigation.navigationFragment}"
        const val navigationUi =
            "androidx.navigation:navigation-ui-ktx:${Versions.Navigation.navigationUI}"
    }

    object AppCenter {
        const val analytics =
            "com.microsoft.appcenter:appcenter-analytics:${Versions.AppCenter.appCenter}"
        const val crashes =
            "com.microsoft.appcenter:appcenter-crashes:${Versions.AppCenter.appCenter}"
    }

    object LifeCycle {
        const val runTimeLiveCycle =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LifeCycle.runTimeLifeCycle}"
        const val lifeCycleCompiler =
            "androidx.lifecycle:lifecycle-compiler:${Versions.LifeCycle.viewModelState}"
        const val liveData =
            "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LifeCycle.liveData}"
        const val viewModel =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LifeCycle.viewModel}"
        const val viewModelState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.LifeCycle.viewModelState}"
    }

    object DI {
        const val hilt =
            "com.google.dagger:hilt-android:${Versions.DI.hilt}"
        const val hiltWork =
            "androidx.hilt:hilt-work:${Versions.DI.hiltWork}"
        const val hiltCompiler =
            "com.google.dagger:hilt-android-compiler:${Versions.DI.hilt}"
        const val hiltNavigation =
            "androidx.hilt:hilt-navigation-fragment:${Versions.DI.hiltNavigation}"
        const val hiltViewModel =
            "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.DI.hiltViewModel}"
        const val hiltWorkManagerCompiler =
            "androidx.hilt:hilt-compiler:${Versions.DI.hiltCompiler}"
    }

    object ReactiveFunc {
        const val rxJava =
            "io.reactivex.rxjava3:rxjava:${Versions.ReactiveFunc.rxJava}"
        const val rxKotlin =
            "io.reactivex.rxjava3:rxkotlin:${Versions.ReactiveFunc.rxKotlin}"
        const val rxAndroid =
            "io.reactivex.rxjava3:rxandroid:${Versions.ReactiveFunc.rxAndroid}"
    }

    object Network {
        const val gson =
            "com.google.code.gson:gson:${Versions.Network.gson}"
        const val gsonAdapter =
            "com.squareup.retrofit2:converter-gson:${Versions.Network.gsonConverter}"
        const val retrofit =
            "com.squareup.retrofit2:retrofit:${Versions.Network.retrofit}"
        const val rxJavaAdapter =
            "com.squareup.retrofit2:adapter-rxjava3:${Versions.Network.rxJava3Adapter}"
        const val okHttp =
            "com.squareup.okhttp3:okhttp:${Versions.Network.okHttp}"
        const val scalar =
            "com.squareup.retrofit2:converter-scalars:${Versions.Network.scalar}"
        const val loggingInterceptor =
            "com.squareup.okhttp3:logging-interceptor:${Versions.Network.loggingInterceptor}"
    }

    object Security {
        const val crypto =
            "androidx.security:security-crypto:${Versions.Security.crypto}"
    }

    object Tools {
        const val timber =
            "com.jakewharton.timber:timber:${Versions.Tools.timber}"
        const val glide =
            "com.github.bumptech.glide:glide:${Versions.Tools.glide}"
        const val glideOkHttpIntegration =
            "com.github.bumptech.glide:okhttp3-integration:${Versions.Tools.glide}"
        const val glideCompiler =
            "com.github.bumptech.glide:compiler:${Versions.Tools.glide}"
        const val lottie =
            "com.airbnb.android:lottie:${Versions.Tools.lottie}"
        const val drawableToolbox =
            "com.github.duanhong169:drawabletoolbox:${Versions.Tools.drawableToolbox}"
        const val singleRowCalendar =
            "com.michalsvec:single-row-calednar:${Versions.Tools.singleRowCalendar}"
        const val roundedImageView =
            "com.makeramen:roundedimageview:${Versions.Tools.roundedImageView}"
        const val linkBuilder =
            "com.klinkerapps:link_builder:${Versions.Tools.linkBuilder}"
        const val viewPager2 =
            "androidx.viewpager2:viewpager2:${Versions.Tools.viewPager2}"
        const val dotsIndicator =
            "me.relex:circleindicator:${Versions.Tools.dotsIndicator}"
        const val shimmerLayout = "com.facebook.shimmer:shimmer:${Versions.Tools.shimmerLayout}"
        const val eventbus =
            "org.greenrobot:eventbus:${Versions.Tools.eventbus}"
        const val materialSearchBar =
            "com.github.mancj:MaterialSearchBar:${Versions.Tools.materialSearchBar}"
        const val fresco =
            "com.facebook.fresco:fresco:${Versions.Tools.fresco}"
        const val zoomageView =
            "com.jsibbold:zoomage:${Versions.Tools.zoomageView}"
        const val exoPlayer =
            "com.google.android.exoplayer:exoplayer:${Versions.Tools.exoPlayer}"
        const val exoPlayerUi =
            "com.google.android.exoplayer:exoplayer-ui:${Versions.Tools.exoPlayer}"
        const val youtubeExtractor =
            "com.github.HaarigerHarald:android-youtubeExtractor:${Versions.Tools.youtubeExtractor}"
        const val balloon =
            "com.github.skydoves:balloon:${Versions.Tools.balloon}"
        const val rangeSeekBar =
            "com.github.Jay-Goo:RangeSeekBar:${Versions.Tools.rangeSeekBar}"
        const val keyboardListener =
            "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:${Versions.Tools.keyboardListener}"
        const val gson =
            "com.google.code.gson:gson:${Versions.Tools.gson}"

        const val qrCodeGenerator =
            "androidmads.library.qrgenerator.QRGenerator:${Versions.Tools.qrCodeGenerator}"
        const val camView =
            "eu.livoto.labs.android:CAMView:${Versions.Tools.camView}"
    }

    object Dengage {
        const val dengage =
            "com.github.whitehorse-technology:dengage.android.sdk:${Versions.Dengage.dengage}"
    }

    object Huawei {
        const val huaweiLocation =
            "com.huawei.hms:location:${Versions.Huawei.huaweiLocation}"
        const val huaweiPush = "com.huawei.hms:push:${Versions.Huawei.huaweiPush}"
        const val huaweiMaps = "com.huawei.hms:maps:${Versions.Huawei.huaweiMaps}"
        const val hwid = "com.huawei.hms:hwid:${Versions.Huawei.hwid}"
        const val adsIdentifier = "com.huawei.hms:ads-identifier:${Versions.Huawei.adsIdentifier}"
    }

    object Google {
        const val playSevicesMap =
            "com.google.android.gms:play-services-maps:${Versions.Google.playServicesMaps}"
        const val mapUtils =
            "com.google.maps.android:android-maps-utils:${Versions.Google.mapUtils}"
        const val googleLocation =
            "com.google.android.gms:play-services-location:${Versions.Google.playServiceLocation}"
        const val googlePlaces =
            "com.google.android.libraries.places:places:${Versions.Google.playPlaces}"
        const val servicesAuth =
            "com.google.android.gms:play-services-auth:${Versions.Google.servicesAuth}"
        const val authApiPhone =
            "com.google.android.gms:play-services-auth-api-phone:${Versions.Google.authApiPhone}"
        const val tagManager =
            "com.google.android.gms:play-services-tagmanager:${Versions.Google.tagManager}"
        const val analytics =
            "com.google.android.gms:play-services-analytics:${Versions.Google.analytics}"
    }

    object Firebase {
        const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.Firebase.firebaseBom}"
        const val firebaseAnalysisktx = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics"
        const val commonKtx = "com.google.firebase:firebase-common"
        const val perf = "com.google.firebase:firebase-perf-ktx"
        const val firebaseCore = "com.google.firebase:firebase-core"
        const val firebaseMessaging = "com.google.firebase:firebase-messaging"
        const val firebaseAnalytics = "com.google.firebase:firebase-analytics"
        const val firebaseIID = "com.google.firebase:firebase-iid"
    }

    object Adjust {
        const val adjustAndroid = "com.adjust.sdk:adjust-android:${Versions.Adjust.android}"
        const val installreferrer =
            "com.android.installreferrer:installreferrer:${Versions.Adjust.installreferrer}"
        const val webbridge = "com.adjust.sdk:adjust-android-webbridge:${Versions.Adjust.webbridge}"
        const val identifier =
            "com.google.android.gms:play-services-ads-identifier:${Versions.Adjust.identifier}"
    }

    object MaterialDialog {
        const val core = "com.afollestad.material-dialogs:core:${Versions.Dialog.materialDialog}"
        const val lifecycle =
            "com.afollestad.material-dialogs:lifecycle:${Versions.Dialog.materialDialog}"
        const val datetime =
            "com.afollestad.material-dialogs:datetime:${Versions.Dialog.materialDialog}"
        const val input = "com.afollestad.material-dialogs:input:${Versions.Dialog.materialDialog}"
        const val bottomsheets =
            "com.afollestad.material-dialogs:bottomsheets:${Versions.Dialog.materialDialog}"
    }

    object Project {
        fun DependencyHandler.app() = project(mapOf("path" to ":app"))
        fun DependencyHandler.data() = project(mapOf("path" to ":data"))
        fun DependencyHandler.routeAndroid() = project(mapOf("path" to ":route-android"))
        fun DependencyHandler.routeCore() = project(mapOf("path" to ":route-core"))
    }

    object Test {
        const val junit =
            "junit:junit:${Versions.Test.junit}"
        const val androidJunit =
            "androidx.test.ext:junit:${Versions.Test.androidJunit}"
        const val espressoCore =
            "androidx.test.espresso:espresso-core:${Versions.Test.espressoCore}"
        const val truthExt =
            "androidx.test.ext:truth:${Versions.Test.truthExtVersion}"
        const val mockK =
            "io.mockk:mockk:${Versions.Test.mockKVersion}"
        const val coreTesting =
            "androidx.arch.core:core-testing:${Versions.Test.coreTestingVersion}"
    }
}
