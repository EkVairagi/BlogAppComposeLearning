package com.xynderous.blogapp

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.xynderous.blogapp.ui.theme.BlogAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            //App()


            // ListComposable()

            // Counter()

            //LaunchEffectComposable()

            //CoroutineScopeComposable()

            // RememberComposeUpdatedState()

            // DisposableEffectCompose()

            // App2()

            // ProduceStateComposable()
            //RefreshProduceStateComposable()


            DerivedStateComposable()

        }
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun DerivedStateComposable() {

    val tabelOf = remember { mutableStateOf(5) }
    val index = produceState(initialValue = 1) {
        repeat(9){
            delay(1000)
            value += 1
        }
    }

    val message = derivedStateOf {
        "${tabelOf.value} * ${index.value} = ${tabelOf.value * index.value}"
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(1f)
    ) {

        Text(
            text = message.value,
            style = MaterialTheme.typography.bodyLarge
        )


    }

}

@Composable
fun ProduceStateComposable() {

    val state = produceState(initialValue = 0) {
        for (i in 1..10) {
            delay(1000)
            value += 1
        }
    }

    Text(
        text = state.value.toString(),
        style = MaterialTheme.typography.bodyLarge
    )

}


@Composable
fun RefreshProduceStateComposable() {

    val degree = produceState(initialValue = 0) {
        while (true) {
            delay(16)
            value = (value + 10) % 360
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(1f),
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = Icons.Default.Refresh, contentDescription = "",
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(degree.value.toFloat())
                )
                Text(text = "Loading")
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App2() {
    // MediaComposable()

    KeyboardComposable()

    TextField(value = "", onValueChange = {})
}

@Composable
fun KeyboardComposable() {
    val view = LocalView.current
    DisposableEffect(key1 = Unit) {

        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val insets = ViewCompat.getRootWindowInsets(view)
            var isKeyBoardVisible = (insets?.isVisible(WindowInsetsCompat.Type.ime()))
            Log.d("Hello World", isKeyBoardVisible.toString())
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
}

@Composable
fun DisposableEffectCompose() {

    var state = remember { mutableStateOf(false) }

    DisposableEffect(key1 = state.value) {
        Log.d("DisposeEffectComposable", "Started")
        onDispose {
            Log.d("DisposeEffectComposable", "Cleaning Started")

        }
    }


    Button(onClick = { state.value = !state.value }) {
        Text(text = "Change State")
    }
}


@Composable
fun MediaComposable() {
    val context = LocalContext.current
    DisposableEffect(key1 = Unit) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.glass)
        mediaPlayer.start()

        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}


@Composable
fun RememberComposeUpdatedState() {
    var counter = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = Unit) {
        delay(1000)
        counter.value = 10
    }
    CounterComposable(counter.value)
}

@Composable
fun CounterComposable(value: Int) {
    var state = rememberUpdatedState(newValue = value)
    LaunchedEffect(key1 = Unit) {
        delay(5000)
        Log.d("HELLO WORLD", state.value.toString())
    }
    Text(text = value.toString())
}

@Composable
fun CoroutineScopeComposable() {
    var count = remember {
        mutableStateOf(0)
    }
    var scope = rememberCoroutineScope()

    var text = "Counter is running ${count.value}"
    if (count.value == 10) {
        text = "Counter stopped"
    }
    Column {
        Text(text = text)
        Button(onClick = {
            scope.launch {
                Log.d("LaunchEffectComposable", "Started...")
                try {
                    for (i in 1..10) {
                        count.value++
                        delay(1000)
                    }
                } catch (e: Exception) {
                    Log.d("LaunchEffectComposable", e.message.toString())
                }
            }
        }) {
            Text(text = "Start")
        }
    }
}


@Composable
fun LaunchEffectComposable() {

    val count = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = Unit) {
        Log.d("LaunchEffectComposable", "Started...")
        try {
            for (i in 1..10) {
                count.value++
                delay(1000)
            }
        } catch (e: Exception) {
            Log.d("LaunchEffectComposable", e.message.toString())
        }
    }

    var text = "Counter is running ${count.value}"

    if (count.value == 10) {
        text = "Counter stopped"
    }
    Text(text = text)


    /*
        you can't call LaunchedEffect inside a button call because a
        compose function can be call inside a composable function
        we need a independent coroutine scope to call a coroutine
        for that we have remember coroutinescope

        Button(onClick = {
            LaunchedEffect(key1 = Unit){
            }
        }) {
        }
    */

}


@Composable
fun Counter() {
    var count = remember {
        mutableStateOf(0)
    }

    var key = count.value % 3 == 0

    Log.d("KEY", key.toString())

    LaunchedEffect(key1 = key) {
        Log.d("Counter", "Current count: ${count.value} ")
    }

    Button(onClick = { count.value++ }) {
        Text(text = "Increment count")
    }
}


//Side Effect in Jetpack Compose
@Composable
fun ListComposable() {

    val categoryState = remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(key1 = Unit) {
        categoryState.value = fetchCategories()
    }

    LazyColumn {
        items(categoryState.value) {
            Text(text = it)
        }
    }


}

//assuming network call
fun fetchCategories(): List<String> {
    return listOf("one", "two", "three")
}


var countrer = 1

@Composable
fun HasSideEffect() {
    countrer++
    Text(text = "Hello World")
}


@Composable
fun App() {
    var theme = remember { mutableStateOf(false) }

    BlogAppTheme(theme.value) {

        Column(Modifier.background(MaterialTheme.colorScheme.background)) {

            Text(
                text = "Hello World", style = MaterialTheme.typography.bodyMedium
            )


            Button(onClick = {
                theme.value = !theme.value
            }) {

                Text(text = "Change Theme")

            }
        }
    }

}