package com.example.composejettrivia.screen

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composejettrivia.model.QuestionItem
import com.example.composejettrivia.utils.AppColors

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    var questionIndex= remember {
        mutableStateOf(0)
    }
    var questionsList = viewModel.dataOrException.value.data
    if (viewModel.dataOrException.value.isLoading == true) {
        CircularProgressIndicator()
    } else {
        val question=try {
             questionsList?.get(questionIndex.value)
        }
        catch (e: Exception){
            null
        }
        QuestionsItems(question = question!!,questionIndex=questionIndex, viewModel=viewModel ){
            questionIndex.value= questionIndex.value+1
        }

//        questionsList?.forEach { questionItem ->
//            QuestionsItems(questionItem)
//
//        }
    }
}

@Composable
fun QuestionsItems(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionsViewModel,
    onNextClicked: (Int) -> Unit
) {
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val choiceState = remember(question) { question.choices.toMutableList() }
    val answerState = remember(question) { mutableStateOf<Int?>(null) }
    val questionAnswerState = remember(question) { mutableStateOf<Boolean?>(null) }
    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            questionAnswerState.value = choiceState[it] == question.answer
        }

    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (questionIndex.value>3) showProgressBar(questionIndex.value)
            QuestionTracker(counter = questionIndex.value+1)
            DrawDottedLine(dashEffect)
            Column {
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxHeight(0.3f)
                        .align(alignment = Alignment.Start),
                    color = AppColors.mOffWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )

                //choices
                choiceState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple, AppColors.mOffDarkPurple
                                    )
                                ), shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomStartPercent = 50,
                                    bottomEndPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)

                            },
                            modifier = Modifier.padding(15.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (questionAnswerState.value == true && index == answerState.value) Color.Green.copy(
                                    0.2f
                                ) else Color.Red.copy(0.2f)
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                                            color =if (questionAnswerState.value == true && index == answerState.value) Color.Green.copy(
                                                0.2f
                                            ) else if (questionAnswerState.value == false && index == answerState.value) Color.Red.copy(0.2f)
                                                else Color.Yellow,
                                                fontSize = 17.sp)){
                                append(answerText)
                            }

                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                    }

                }
                Button(onClick = {onNextClicked(questionIndex.value) },
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.mLightBlue
                    )
                ) {
                    Text(text = "Next", modifier = Modifier.padding(6.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 15.sp)

                }
            }
        }

    }
}

@Composable
fun DrawDottedLine(dashEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = dashEffect
        )
    }

}

//@Preview
@Composable
fun QuestionTracker(
    counter: Int = 10, outOff: Int = 100
) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = AppColors.mLightGray, fontWeight = FontWeight.Bold, fontSize = 27.sp
                )
            ) {
                append("Question $counter/")
                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                ) {
                    append("$outOff")
                }

            }


        }
    }, modifier = Modifier.padding(20.dp))

}

@Preview
@Composable
fun showProgressBar(score: Int=3){
    val progressFactor by remember(score) {
        mutableStateOf(score* 0.005f)
    }
    val gradient= Brush.linearGradient(listOf(Color(0xFFC24B43),Color(0xFF5521B1)))
    Row(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    AppColors.mDarkPurple,
                    AppColors.mDarkPurple
                )
            ),
            shape = RoundedCornerShape(34.dp)
        )
        .clip(
            shape = RoundedCornerShape(
                topEndPercent = 50,
                topStartPercent = 50,
                bottomStartPercent = 50,
                bottomEndPercent = 50
            )
        )
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { /*TODO*/ },
        contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )
        ) {

            Text(text = (score*10).toString(),
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.87f)
                .clip(shape = RoundedCornerShape(23.dp))
                .padding(6.dp),
                textAlign = TextAlign.Center,
                color = AppColors.mOffWhite
            )
        }
    }
}