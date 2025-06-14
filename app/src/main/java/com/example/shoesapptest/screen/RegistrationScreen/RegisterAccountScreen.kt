package com.example.shoesapptest.screen.RegistrationScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shoesapp.ui.theme.MatuleTheme
import com.example.shoesapptest.R
import com.example.shoesapptest.screen.RegistrationScreen.component.RegisterButton
import com.example.shoesapptest.screen.RegistrationScreen.component.RegistrationTextField
import com.example.shoesapptest.screen.RegistrationScreen.component.TitleAndSubTitle
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun RegisterAccountScreen(
    onNavigationToSigninScreen: () -> Unit,
    viewModel: RegistrationViewModel = koinViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .padding(top = 35.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                IconButton(onClick = onNavigationToSigninScreen) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = null
                    )
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigationToSigninScreen() },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Есть аккаунт? Войти",
                    style = MatuleTheme.typography.bodyRegular16.copy(color = MatuleTheme.colors.text),
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { paddingValues ->
        val state = viewModel.registrationScreenState.value

        LaunchedEffect(state.isSignedIn) {
            if (state.isSignedIn) {
                onNavigationToSigninScreen()
            }
        }

        LaunchedEffect(state.errorMessage) {
            state.errorMessage?.let {
                snackbarHostState.showSnackbar(it)
                viewModel.setErrorMessage(null)
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            TitleAndSubTitle(
                title = "Регистрация",
                subText = "Заполните Свои данные или продолжите через социальные медиа"
            )

            Spacer(modifier = Modifier.height(20.dp))

            RegistrationTextField(
                value = state.name,
                onChangeValue = { viewModel.setName(it) },
                isError = false,
                supportingText = { Text(text = "Неверное имя пользователя") },
                placeholder = { Text(text = stringResource(R.string.PasswordPlaceHolder)) },
                label = { Text(text = "Ваше имя") }
            )

            RegistrationTextField(
                value = state.email,
                onChangeValue = { viewModel.setEmail(it) },
                isError = state.emailHasError,
                supportingText = { Text(text = stringResource(R.string.LoginError)) },
                placeholder = { Text(text = stringResource(R.string.template_email)) },
                label = { Text(text = stringResource(R.string.email)) },
            )

            RegistrationTextField(
                value = state.password,
                onChangeValue = { viewModel.setPassword(it) },
                isError = false,
                isPassword = true,
                supportingText = { Text(text = stringResource(R.string.PasswordError)) },
                placeholder = { Text(text = stringResource(R.string.PasswordPlaceHolder)) },
                label = { Text(text = stringResource(R.string.Password)) }
            )

            SimpleCheckbox()

            RegisterButton(onClick = { viewModel.register { } }) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(R.string.Registration))
                }
            }
        }
    }
}

@Composable
private fun SimpleCheckbox() {
    val isChecked = remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }
        )
        Text(
            text = "Даю согласие на обработку персональных данных",
            style = MatuleTheme.typography.bodyRegular16.copy(color = MatuleTheme.colors.text),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}