package com.example.psikoappws.presenter.view

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.example.psikoappws.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.psikoappws.data.model.QuoteItem
import com.example.psikoappws.data.model.StoreFavQuote
import com.example.psikoappws.presenter.features.viewModel.quote.QuoteViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import java.time.format.TextStyle
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun QuoteScreen(
    navController: NavController,
    viewModel: QuoteViewModel = hiltViewModel()
) {

   val image = listOf(
       R.drawable.quoteone,
       R.drawable.quotefive,
       R.drawable.quoteseventeen,
       R.drawable.quotesixteen,
       R.drawable.quoteten,
       R.drawable.quotethirteen,
       R.drawable.quotetwelvw,
       R.drawable.quotetwenty,
       R.drawable.quotethree,
   )

    val currentIndex = remember{ mutableStateOf(0) }
    val rnds = (0..5).random()

    val context = LocalContext.current
    val pages = ArrayList<QuoteItem>()
    val pagerState = rememberPagerState(
        initialPage = 1
    )
    LaunchedEffect(Unit){
        while (true){
            yield()
            delay(20000)
            tween<Float>(600)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                animationSpec = tween(600)
            )
        }
    }

    LaunchedEffect(Unit){
        while (true){
            delay(10000)

            currentIndex.value = (currentIndex.value + 1) % image.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE2DED0)),
        contentAlignment = Alignment.Center
    ){

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Image(
                painter = painterResource(image[currentIndex.value]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        HorizontalPager(
            state = pagerState,
            count = viewModel.quoteOfList.value.size,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(0.dp, 20.dp, 0.dp, 20.dp)
        ) { page ->
            Card(modifier = Modifier
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale

                    }
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                }
                .fillMaxWidth()
                .padding(25.dp, 0.dp, 25.dp, 0.dp)
                .background(Color.Transparent),
                shape = RoundedCornerShape(10.dp),
                elevation = 5.dp,
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            ) {

                val qList by remember{
                    viewModel.quoteOfList
                }

                var textOf = remember {
                    viewModel.favText
                }
                var authorOf = remember {
                    viewModel.favAuthor
                }
                val shuffledQuote = qList.shuffled()



                Column(modifier = Modifier
                    .width(350.dp)
                    .height(450.dp)
                    //.background(Color(0xFF738580)),
                    ,verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.TopEnd,
                        modifier = Modifier
                            .fillMaxWidth()//.height(90.dp)
                            .padding(16.dp, 16.dp, 16.dp, 16.dp),

                        ){

                        DisplayToggleButton(
                            onSave = {
                                textOf.value = shuffledQuote[page].text.toString()
                                authorOf.value = shuffledQuote[page].author.toString()
                                viewModel.storeFavQuote(context, textOf.value, authorOf.value)
                            },
                            onDelete = {
                                //viewModel.deleteMyQuote()

                            }
                        )

                    }
                    Spacer(modifier = Modifier.height(60.dp))
                    
                    Box(contentAlignment = Alignment.Center) {
                        val quote = shuffledQuote[page].text
                        Text(text = quote.toString(),
                            fontSize = 22.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(16.dp)
                                .offset(
                                    x = 2.dp,
                                    y = 2.dp
                                )
                                .alpha(1f) //0.75f
                        )
                        Text(
                            text = quote.toString(),
                            fontSize = 22.sp,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)

                        )

                            //modifier = Modifier.padding(16.dp)
                    }

                    Box(contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text ="${
                                if(!shuffledQuote[page].author.equals(null)){
                                    shuffledQuote[page].author
                                    
                            }
                                else{
                                    shuffledQuote[page].author = "Unknown"
                            }
                            }",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    


                }

            }

        }



           /* HorizontalPager(
                modifier = Modifier,
                count = viewModel.quoteOfList.value.size,
                state = pagerState,
                verticalAlignment = Alignment.CenterVertically
            ) { position ->
                // PagerScreen(onBoardingPage = pages[position])
                ListViewOfQuote()
            }*/


       // HorizontalPagerIndicator(
       //     modifier = Modifier
       //         .align(Alignment.BottomCenter)
       //         .padding(0.dp, 0.dp, 0.dp, 16.dp),
       //     pagerState = pagerState
       // )
    }

}


/*
@Composable
fun ListOfQuotes(
    quoteItem: List<QuoteItem>
) {
    LazyRow(contentPadding = PaddingValues(5.dp)){
        items(quoteItem){item ->
            ContentOfQuote(item)

        }
    }

}
*/


fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}

/**
 * Linearly interpolate between [start] and [stop] with [fraction] fraction between them.
 */
fun lerp(start: Int, stop: Int, fraction: Float): Int {
    return start + ((stop - start) * fraction.toDouble()).roundToInt()
}

/**
 * Linearly interpolate between [start] and [stop] with [fraction] fraction between them.
 */
fun lerp(start: Long, stop: Long, fraction: Float): Long {
    return start + ((stop - start) * fraction.toDouble()).roundToLong()
}