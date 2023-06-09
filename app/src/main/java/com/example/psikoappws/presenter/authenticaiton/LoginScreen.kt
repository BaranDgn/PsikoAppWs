package com.example.psikoappws.presenter.authenticaiton


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.psikoappws.R
import com.example.psikoappws.domain.util.Resource
import com.example.psikoappws.presenter.graph.AuthScreen
import com.example.psikoappws.presenter.graph.Graph
import com.example.psikoappws.ui.theme.Shapes

@Composable
fun LoginScreen(
    navController: NavController,
    signUpClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    //DataStore


    val authResource = viewModel?.loginFlow?.collectAsState()

    val passwordFocusRequester = FocusRequester()
    val focusManager : FocusManager = LocalFocusManager.current

    var email: String by remember{ mutableStateOf("") }
    var password: String by remember{ mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffFBFCF8)),
        contentAlignment = Alignment.Center
    ){


    Column(
        modifier = Modifier

            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.psikologo),
            contentDescription = "login",
            modifier = Modifier
                .size(150.dp)
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
                .shadow(5.dp)
        )


        //Email
        TextField(
            value = email,
            onValueChange = {email = it},
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, CircleShape)
                .focusRequester(focusRequester = FocusRequester())
               ,
            leadingIcon = { Icon(imageVector = InputType.UserName.icon, contentDescription = null)},
            label = { Text(text = InputType.UserName.label) },
            //shape = Shapes.small,
            shape = Shapes.medium,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent),
            singleLine = true,
            keyboardOptions = InputType.UserName.keyboardOptions,
            visualTransformation = InputType.UserName.visualTransformation,
            keyboardActions = KeyboardActions(
                onNext = {passwordFocusRequester.requestFocus()})


        )

        //Password

        TextField(
            value = password,
            onValueChange = {password = it},
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, CircleShape)
                .focusRequester(focusRequester = passwordFocusRequester),
            leadingIcon = { Icon(imageVector = InputType.Password.icon, contentDescription = null)},
            label = { Text(text = InputType.Password.label) },
            //shape = Shapes.small,
            shape = Shapes.medium,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent),
            singleLine = true,
            keyboardOptions = InputType.Password.keyboardOptions,
            visualTransformation = if(passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }),

            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(painter = painterResource(R.drawable.password_eye),
                        contentDescription = null,
                        tint = if(passwordVisibility) Color.DarkGray else Color.Gray)
                }
            }
        )


        //Button
        Button(
            onClick = {
                viewModel.logInUser(email, password)
              //  onClick()
                      },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, CircleShape),
            colors = ButtonDefaults.buttonColors(
                Color(0xFF4E4F50),
                contentColor = Color(0xFFE2DED0)
            )
        ) {
            Text(text = "LOG IN", modifier = Modifier.padding(vertical = 8.dp))
        }

        //Sign up
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(0.dp, 32.dp,0.dp,0.dp)
        ) {
            Text(text = "Don't have an account? ",
                fontSize = 16.sp,
                color =  Color(0xFF4E4F50))

            TextButton(onClick = { signUpClick() }) {
                Text(text = "SIGN UP",
                    fontSize = 16.sp,
                    color = Color(0xFF4E4F50))

            }
        }
    }
        authResource?.value?.let{
            when(it){
                is Resource.Error -> {
                    Toast.makeText(context,"Email or password might be wrong or cannot be empty", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                    //Log.e("loading", "login is loading")
                }
                is Resource.Success -> {
                    LaunchedEffect("",Unit){
                        Toast.makeText(context,"Welcome to Psiko App", Toast.LENGTH_SHORT).show()
                        navController.navigate(Graph.HOME){
                            popUpTo(AuthScreen.Login.route){inclusive = true}

                        //navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}




sealed class InputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
){
    object UserName : InputType(
        label = "Email",
        icon = Icons.Default.Person,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
        visualTransformation = VisualTransformation.None
    )
    object Password : InputType(
        label = "Password",
        icon= Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}